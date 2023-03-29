package android.moviesapp.data;

import android.content.Context;
import android.moviesapp.domain.List;
import android.moviesapp.domain.Movie;


import androidx.room.Room;
import androidx.room.RoomDatabase;


@androidx.room.Database(entities = {Movie.class, List.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static volatile Database instance;


    public static Database getInstance(Context ctx) {
        if (instance == null) {
            // Double lock to prevent multiple threads from
            // creating a new database instance at the same
            // time (sqlite doesn't support multiple clients).
            synchronized (Database.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            ctx.getApplicationContext(),
                            Database.class,
                            "share_a_meal.db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}
