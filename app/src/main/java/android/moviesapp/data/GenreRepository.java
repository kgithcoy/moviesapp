package android.moviesapp.data;

import android.content.Context;
import android.moviesapp.domain.Genre;
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

public class GenreRepository {
    private static final String TAG = "GenreRepository";
    private final GenreDao genreDao;
    private final TheMovieDBService theMovieDBService;

    public GenreRepository(Context ctx) {
        var db = Database.getInstance(ctx);
        genreDao = db.genreDao();

        var client = new Retrofit.Builder()
                .baseUrl(TheMovieDBService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theMovieDBService = client.create(TheMovieDBService.class);
    }

    public LiveData<List<Genre>> getGenres() {
        return genreDao.getGenres();
    }

    public void requestGenres(Consumer<List<Genre>> success, Consumer<Exception> error) {
        CompletableFuture.runAsync(() -> {
            try {
                var response = theMovieDBService.getGenres(TheMovieDBService.API_KEY).execute();
                var genres = handleGenreResponse(response);
                new Handler(Looper.getMainLooper()).post(() -> success.accept(genres));
            } catch (Exception err) {
                Log.e(TAG, "Could not load genre items", err);
                new Handler(Looper.getMainLooper()).post(() -> error.accept(err));
            }
        });
    }

    private List<Genre> handleGenreResponse(Response<GenresResponseData<Genre>> response) throws Exception {
        var body = response.body();
        if (!response.isSuccessful() || body == null) {
            throw new Exception("API request failed; code: " + response.code());
        }

        var genres = body.data();
        genreDao.insert(genres);
        return genres;
    }
}
