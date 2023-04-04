package android.moviesapp.data;

import android.moviesapp.domain.Genre;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GenreDao {
    @Query("SELECT * FROM genre")
    LiveData<List<Genre>> getGenres();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Genre> genres);

    @Query("DELETE FROM genre")
    void clear();
}
