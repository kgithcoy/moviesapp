package android.moviesapp.data;

import android.content.Context;
import android.moviesapp.domain.Movie;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static final String TAG = "MovieRepository";
    private final TheMovieDBService theMovieDBService;

    public MovieRepository(Context ctx) {
        var db = Database.getInstance(ctx);

        var client = new Retrofit.Builder()
                .baseUrl(TheMovieDBService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theMovieDBService = client.create(TheMovieDBService.class);
    }

    /**
     * refreshMovies tries to refresh the data stored in the
     * database by fetching new data from the API. If the
     * request is successful, the database is cleared and
     * the retrieved data is inserted.
     * @param success callback that is executed when the
     *                request is successful.
     * @param error callback that is executed when the
     *              request fails.
     */
    public void refreshMovies(Consumer<List<Movie>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.popularMovies(
                    1,
                    TheMovieDBService.API_KEY
                ).execute();

                var body = response.body();
                if (!response.isSuccessful() || body == null) {
                    throw new Exception("API request failed");
                }

                var meals = body.data();
                Log.i(TAG, "Refreshed local database");
                new Handler(Looper.getMainLooper()).post(() -> success.accept(meals));
            } catch (Exception err) {
                Log.e(TAG, "Could not refresh local database", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }
}
