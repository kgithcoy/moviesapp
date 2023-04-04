package android.moviesapp.data;

import android.moviesapp.domain.Movie;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListDao {
    @Query("SELECT * FROM list")
    LiveData<List<android.moviesapp.domain.List>> getLists();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<android.moviesapp.domain.List> list);

    @Query("DELETE FROM list")
    void clear();
}
