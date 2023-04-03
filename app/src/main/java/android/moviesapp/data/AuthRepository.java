package android.moviesapp.data;

import android.content.Context;
import android.moviesapp.domain.Movie;
import android.moviesapp.domain.api.CreateSessionRequest;
import android.moviesapp.domain.api.ResponseSession;
import android.moviesapp.domain.api.ResponseToken;
import android.moviesapp.domain.api.ValidateTokenWithLoginRequest;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository {

    private final TheMovieDBService theMovieDBService;

    public AuthRepository(Context ctx) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDBService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theMovieDBService = retrofit.create(TheMovieDBService.class);

        theMovieDBService.getRequestToken(TheMovieDBService.API_KEY).enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                if (response.isSuccessful()) {
                    ResponseToken tokenResponse = response.body();
                    String requestToken = tokenResponse.getRequestToken();

                    createSession(requestToken);
                } else {
                    // Handle the error in the response
                }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {
                // Handle the error in the request
            }
        });
    }

    private void createSession(String requestToken) {
        CreateSessionRequest createSessionRequest = new CreateSessionRequest(requestToken);
        theMovieDBService.createSession(TheMovieDBService.API_KEY, createSessionRequest).enqueue(new Callback<ResponseSession>() {
            @Override
            public void onResponse(Call<ResponseSession> call, Response<ResponseSession> response) {
                if (response.isSuccessful()) {
                    ResponseSession sessionResponse = response.body();
                    String sessionId = sessionResponse.getSessionId();
                    // Use the sessionId for authenticated requests
                } else {
                    // Handle the error in the response
                }
            }

            @Override
            public void onFailure(Call<ResponseSession> call, Throwable t) {
                // Handle the error in the request
            }
        });
    }

    private void validateRequestTokenWithLogin(String requestToken, String username, String password) {

        ValidateTokenWithLoginRequest ValidateTokenWithLoginRequest = new ValidateTokenWithLoginRequest(requestToken, username, password);

        theMovieDBService.validateRequestTokenWithLogin(TheMovieDBService.API_KEY, ValidateTokenWithLoginRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Request token validated, proceed to create session


                    createSession(requestToken);
                } else {
                    // Handle the error in the response
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle the error in the request
            }
        });
    }


}






}
