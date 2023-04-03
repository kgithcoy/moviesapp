package android.moviesapp.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.moviesapp.R;
import android.moviesapp.domain.Movie;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MovieDetailsActivity extends AppCompatActivity {
    private final static String TAG = "MovieDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        var movie = (Movie)getIntent().getSerializableExtra("movie");
        Log.i(TAG, "Movie details opened; id: " + movie.getId());

        // Get views
        TextView title = findViewById(R.id.movie_details_title);

        // Load data into views
        title.setText(movie.getTitle());
    }
}