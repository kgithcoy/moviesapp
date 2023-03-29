package android.moviesapp.data;

import android.moviesapp.domain.Movie;
import android.moviesapp.domain.api.ResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * TheMovieDBService defines the API endpoints of the TheMovieDB API.
 */
public interface TheMovieDBService {
    String ENDPOINT = "https://api.themoviedb.org/";
    String API_KEY = "805531b78efe54a857ebf2cd0d4d1d3b";

    @GET("/3/movie/popular")
    Call<ResponseData<Movie>> popularMovies(@Query("page") int page, @Query("api_key") String apiKey);
}
