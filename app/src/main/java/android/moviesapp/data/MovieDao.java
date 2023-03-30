package android.moviesapp.data;

import android.moviesapp.domain.Movie;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * MovieDao defines the database operations that can be
 * performed on the Movie table.
 */
@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    LiveData<List<Movie>> getMoviesByPopularity();

    @Query("SELECT * FROM movie ORDER BY voteAverage DESC")
    LiveData<List<Movie>> getMoviesByRating();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Movie> movie);

    @Query("DELETE FROM movie")
    void clear();
}
