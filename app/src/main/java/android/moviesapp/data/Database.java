package android.moviesapp.data;

import android.content.Context;
import android.moviesapp.domain.Genre;
import android.moviesapp.domain.Movie;
import android.moviesapp.domain.List;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@androidx.room.Database(entities = {Movie.class, Genre.class, List.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {
    private static volatile Database instance;
    public abstract MovieDao movieDao();
    public abstract GenreDao genreDao();

    public abstract ListDao listDao();

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
                            "movies_app.db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }

}
