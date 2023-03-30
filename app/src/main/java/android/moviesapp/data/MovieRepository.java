package android.moviesapp.data;

import android.content.Context;
import android.moviesapp.domain.Movie;
import android.moviesapp.domain.api.ResponseData;
import android.moviesapp.presentation.MainActivity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static final String TAG = "MovieRepository";
    private final MovieDao movieDao;
    private final TheMovieDBService theMovieDBService;

    public MovieRepository(Context ctx) {
        var db = Database.getInstance(ctx);
        movieDao = db.movieDao();

        var client = new Retrofit.Builder()
            .baseUrl(TheMovieDBService.ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        theMovieDBService = client.create(TheMovieDBService.class);
    }

    /**
     * getPopularMovies returns a LiveData object that contains a
     * list of all movies.
     * @return list of meals.
     */
    public LiveData<List<Movie>> getPopularMovies() {
        return movieDao.getMoviesOrderByPopularity();
    }

    /**
     * requestMovies tries to refresh the data stored in the
     * database by fetching new data from the API. If the
     * request is successful, the movies in the database
     * are replaced.
     * @param success callback that is executed when the
     *                request is successful.
     * @param error callback that is executed when the
     *              request fails.
     */
    public void requestMovies(int page, Consumer<List<Movie>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                Log.d(TAG, "Loading movie items from API for page " + page);
                var response = theMovieDBService.popularMovies(
                    page,
                    TheMovieDBService.API_KEY
                ).execute();

                var body = response.body();
                if (!response.isSuccessful() || body == null) {
                    throw new Exception("API request failed; code: " + response.code());
                }

                var movies = body.data();
                movieDao.insert(movies);
                new Handler(Looper.getMainLooper()).post(() -> success.accept(movies));
            } catch (Exception err) {
                Log.e(TAG, "Could not load movie items", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    /**
     * searchMovies tries to search for movies by a given query.
     * @param success callback that is executed when the
     *                request is successful.
     * @param error callback that is executed when the
     *              request fails.
     */
    public void searchMovies(String query, int page, Consumer<List<Movie>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                Log.d(TAG, "Searching movies; query: " + query + ", page: " + page);
                var response = theMovieDBService.searchMovies(
                    query,
                    page,
                    TheMovieDBService.API_KEY
                ).execute();

                var body = response.body();
                if (!response.isSuccessful() || body == null) {
                    throw new Exception("API request failed; code: " + response.code());
                }

                new Handler(Looper.getMainLooper()).post(() -> success.accept(body.data()));
            } catch (Exception err) {
                Log.e(TAG, "Could not load movie items", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });

    }
    public void requestTrendingMovies(int page, Consumer<List<Movie>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.trendingMovies(page, TheMovieDBService.API_KEY).execute();
                handleMovieResponse(response, success, error);
            } catch (Exception err) {
                handleError(err, error);
            }
        });
    }

    public void requestPopularMovies(int page, Consumer<List<Movie>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.popularMovies(page, TheMovieDBService.API_KEY).execute();
                handleMovieResponse(response, success, error);
            } catch (Exception err) {
                handleError(err, error);
            }
        });
    }

    public void requestTopRatedMovies(int page, Consumer<List<Movie>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.topRatedMovies(page, TheMovieDBService.API_KEY).execute();
                handleMovieResponse(response, success, error);
            } catch (Exception err) {
                handleError(err, error);
            }
        });
    }

    private void handleMovieResponse(Response<ResponseData<Movie>> response, Consumer<List<Movie>> success, Consumer<Exception> error) {
        var body = response.body();
        if (!response.isSuccessful() || body == null) {
            handleError(new Exception("API request failed; code: " + response.code()), error);
            return;
        }

        var movies = body.data();
        movieDao.insert(movies);

        Log.i(TAG, "Loaded " + movies.size() + " movie items from API");
        new Handler(Looper.getMainLooper()).post(() -> success.accept(movies));
    }

    private void handleError(Exception err, Consumer<Exception> error) {
        Log.e(TAG, "Could not load movie items", err);
        new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
    }


}
