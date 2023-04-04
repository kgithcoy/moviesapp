package android.moviesapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.moviesapp.domain.Account;
import android.moviesapp.domain.api.LoginToken;
import android.moviesapp.domain.api.Session;
import android.moviesapp.domain.api.ValidateRequestTokenRequest;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private final TheMovieDBService theMovieDBService;
    private static Session session;
    private SharedPreferences preferences;

    public AuthRepository(Context ctx) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(TheMovieDBService.ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        theMovieDBService = retrofit.create(TheMovieDBService.class);

        preferences = ctx.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    public void getLoginToken(Consumer<LoginToken> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.getRequestToken(TheMovieDBService.API_KEY).execute();
                var loginToken = response.body();
                if (!response.isSuccessful() || loginToken == null) {
                    throw new Exception("API request failed; code: " + response.code());
                }

                new Handler(Looper.getMainLooper()).post(() -> success.accept(loginToken));
            } catch (Exception err) {
                Log.e(TAG, "Could not retrieve login token", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    public void login(
        LoginToken token,
        String username,
        String password,
        Consumer<LoginToken> success,
        Consumer<Exception> error
    ) {
        CompletableFuture.runAsync(() -> {
            try {
                var request = new ValidateRequestTokenRequest(username, password, token);
                var response = theMovieDBService.validateRequestToken(request, TheMovieDBService.API_KEY).execute();
                var loginToken = response.body();
                if (!response.isSuccessful() || loginToken == null) {
                    throw new Exception("API request failed; code: " + response.code());
                }

                new Handler(Looper.getMainLooper()).post(() -> success.accept(loginToken));
            } catch (Exception err) {
                Log.e(TAG, "Could not login", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    public void retrieveSession(LoginToken loginToken, Consumer<Session> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.createSession(loginToken, TheMovieDBService.API_KEY).execute();
                var session = response.body();
                if (!response.isSuccessful() || session == null) {
                    throw new Exception("API request failed; code: " + response.code());
                }

                AuthRepository.session = session;
                new Handler(Looper.getMainLooper()).post(() -> success.accept(session));
            } catch (Exception err) {
                Log.e(TAG, "Could not retrieve session id", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    public void storeAccount(String username, String password) {
        CompletableFuture.runAsync(() -> {
            var editor = preferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();
        });
    }

    public void loginWithStoredAccount(LoginToken loginToken, Consumer<LoginToken> success, Consumer<Exception> error) {
        if (!preferences.contains("username") || !preferences.contains("password")) {
            return;
        }

        Log.d(TAG, "User:" + preferences.getString("username", ""));
        login(
            loginToken,
            preferences.getString("username", ""),
            preferences.getString("password", ""),
            success,
            error
        );
    }

    public void getAccount (Session session, Consumer<Account> success , Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.getAccount(TheMovieDBService.API_KEY, session.sessionId()).execute();
                var body = response.body();
                if (!response.isSuccessful() || body == null) {
                    throw new Exception("API request failed; code: " + response.code());
                }

                new Handler(Looper.getMainLooper()).post(() -> success.accept(body));
            } catch (Exception err) {
                Log.e(TAG, "Could not retrieve account", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    public static Session getSession() {
        return session;
    }
}