package android.moviesapp.data;

import android.moviesapp.domain.Genre;
import android.moviesapp.domain.List;
import android.moviesapp.domain.Movie;
import android.moviesapp.domain.Account;
import android.moviesapp.domain.api.ResponseData;
import android.moviesapp.domain.api.Session;
import android.moviesapp.domain.api.LoginToken;
import android.moviesapp.domain.api.ValidateRequestTokenRequest;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * TheMovieDBService defines the API endpoints of the TheMovieDB API.
 */
public interface TheMovieDBService {
    String ENDPOINT = "https://api.themoviedb.org/";
    String API_KEY = "805531b78efe54a857ebf2cd0d4d1d3b";

    /* movies */
    @GET("/3/trending/movie/week")
    Call<ResponseData<Movie>> trendingMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("/3/movie/popular")
    Call<ResponseData<Movie>> popularMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("/3/movie/top_rated")
    Call<ResponseData<Movie>> topRatedMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("/3/search/movie")
    Call<ResponseData<Movie>> searchMovies(@Query("query") String query, @Query("page") int page, @Query("api_key") String apiKey);


    @GET("/3/account")
    Call<Account> getAccount(@Query("session_id") String sessionId, @Query("api_key") String apiKey);

    @GET("/3/account/{account_id}/lists")
    Call<ResponseData<List>> getAccountLists(
            @Path("account_id") int accountId,
            @Query("session_id") String sessionId,
            @Query("api_key") String apiKey
    );

    @GET("/3/list/{list_id}")
    Call<List> getList(
            @Path("list_id") int listId,
            @Query("api_key") String apiKey
    );

    /* login */
    @GET("/3/authentication/token/new")
    Call<LoginToken> getRequestToken(@Query("api_key") String apiKey);

    @POST("/3/authentication/token/validate_with_login")
    Call<LoginToken> validateRequestToken(
            @Body ValidateRequestTokenRequest validateRequestTokenRequest,
            @Query("api_key") String apiKey
    );

    @POST("/3/authentication/session/new")
    Call<Session> createSession(@Body LoginToken loginToken, @Query("api_key") String apiKey);
    @GET("/3/genre/movie/list")
    Call<GenresResponseData<Genre>> getGenres(@Query("api_key") String apiKey);

}