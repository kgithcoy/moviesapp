package android.moviesapp.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.moviesapp.R;
import android.moviesapp.data.MovieRepository;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        var repo = new MovieRepository(this);
        repo.refreshMovies(
            success -> Log.d("A", "success"),
            err -> Log.d("A", "error")
        );
    }
}