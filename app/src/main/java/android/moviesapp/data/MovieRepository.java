package android.moviesapp.data;

import android.content.Context;
import android.moviesapp.domain.Movie;
import android.moviesapp.domain.api.ResponseData;
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
     * getMoviesByPopularity returns a LiveData object that
     * contains a list of all movies ordered by popularity.
     * @return list of movies.
     */
    public LiveData<List<Movie>> getMoviesByPopularity() {
        return movieDao.getMoviesByPopularity();
    }

    /**
     * getMoviesByRating returns a LiveData object that
     * contains a list of all movies ordered by rating.
     * @return list of movies.
     */
    public LiveData<List<Movie>> getMoviesByRating() {
        return movieDao.getMoviesByRating();
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
                var response = theMovieDBService.searchMovies(query, page, TheMovieDBService.API_KEY).execute();
                var movies = handleMovieResponse(response);
                new Handler(Looper.getMainLooper()).post(() -> success.accept(movies));
            } catch (Exception err) {
                Log.e(TAG, "Could not load movie items", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    /**
     * requestTrendingMovies tries to load trending movies from the API.
     * @param page page number
     * @param success callback that is executed when the
     *                request is successful.
     * @param error callback that is executed when the
     *              request fails.
     */
    public void requestTrendingMovies(int page, Consumer<List<Movie>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.trendingMovies(page, TheMovieDBService.API_KEY).execute();
                var movies = handleMovieResponse(response);
                new Handler(Looper.getMainLooper()).post(() -> success.accept(movies));
            } catch (Exception err) {
                Log.e(TAG, "Could not load movie items", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    /**
     * requestPopularMovies tries to load popular movies from the API.
     * @param page page number
     * @param success callback that is executed when the
     *                request is successful.
     * @param error callback that is executed when the
     *              request fails.
     */
    public void requestPopularMovies(int page, Consumer<List<Movie>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.popularMovies(page, TheMovieDBService.API_KEY).execute();
                var movies = handleMovieResponse(response);
                new Handler(Looper.getMainLooper()).post(() -> success.accept(movies));
            } catch (Exception err) {
                Log.e(TAG, "Could not load movie items", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    /**
     * requestTopRatedMovies tries to load top rated movies from the API.
     * @param page page number
     * @param success callback that is executed when the
     *                request is successful.
     * @param error callback that is executed when the
     *              request fails.
     */
    public void requestTopRatedMovies(int page, Consumer<List<Movie>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.topRatedMovies(page, TheMovieDBService.API_KEY).execute();
                var movies = handleMovieResponse(response);
                new Handler(Looper.getMainLooper()).post(() -> success.accept(movies));
            } catch (Exception err) {
                Log.e(TAG, "Could not load movie items", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    /**
     * handleMovieResponse handles a movie response from a
     * request and returns a list of movies.
     * @param response response from the API
     * @return list of movies
     */
    private List<Movie> handleMovieResponse(Response<ResponseData<Movie>> response) throws Exception {
        var body = response.body();
        if (!response.isSuccessful() || body == null) {
            throw new Exception("API request failed; code: " + response.code());
        }

        var movies = body.data();
        movieDao.insert(movies);
        return movies;
    }

}
