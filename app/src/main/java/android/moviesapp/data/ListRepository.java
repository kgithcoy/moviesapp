package android.moviesapp.data;

import android.content.Context;
import android.moviesapp.domain.Account;
import android.moviesapp.domain.Movie;
import android.moviesapp.domain.api.ResponseData;
import android.moviesapp.domain.api.Session;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListRepository {
    private static final String TAG = "MovieRepository";
    private final ListDao listDao;
    private final TheMovieDBService theMovieDBService;

    public ListRepository(Context ctx) {
        var db = Database.getInstance(ctx);
        listDao = db.listDao();

        var client = new Retrofit.Builder()
                .baseUrl(TheMovieDBService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theMovieDBService = client.create(TheMovieDBService.class);
    }

    public void requestAccountLists(Session session, Account account, Consumer<List<android.moviesapp.domain.List>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.getAccountLists(account.getId(), session.sessionId(), TheMovieDBService.API_KEY).execute();
                var body = response.body();
                if (!response.isSuccessful() || body == null) {
                    throw new Exception("API request failed; code: " + response.code());
                }
                new Handler(Looper.getMainLooper()).post(() -> success.accept(body.data()));
            } catch (Exception err) {
                Log.e(TAG, "Could not load account lists", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    public void requestList(int listId, Consumer<android.moviesapp.domain.List> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.getList(listId, TheMovieDBService.API_KEY).execute();
                var list = response.body();
                if (!response.isSuccessful() || list == null) {
                    throw new Exception("API request failed; code: " + response.code());
                }
                new Handler(Looper.getMainLooper()).post(() -> success.accept(list));
            } catch (Exception err) {
                Log.e(TAG, "Could not load list", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    public void addMovieToList(android.moviesapp.domain.List list, Movie movie, Session session, Consumer<android.moviesapp.domain.List> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.addMovieToList(list.getId(), session.sessionId(), TheMovieDBService.API_KEY, Map.of("media_id", movie.getId())).execute();
                if (!response.isSuccessful()) {
                    throw new Exception("API request failed; code: " + response.code());
                }
                new Handler(Looper.getMainLooper()).post(() -> success.accept(list));
            } catch (Exception err) {
                Log.e(TAG, "Could not add movie to list", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }
}
