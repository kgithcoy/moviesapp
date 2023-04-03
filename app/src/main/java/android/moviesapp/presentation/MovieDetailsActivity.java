package android.moviesapp.presentation;



import androidx.appcompat.app.AppCompatActivity;

import android.moviesapp.R;
import android.moviesapp.domain.Movie;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
        ImageView image = findViewById(R.id.movie_details_poster);
        TextView summary = findViewById(R.id.movie_details_summary);
        //TextView reviews = findViewById(R.id.movie_details_reviews);
        // Load data into views
        Picasso.get()
                .load( movie.getBackdropUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(image, new Callback() {
                    public void onSuccess() { }
                    public void onError(Exception err) {
                        // If image can't be loaded from cache,
                        // try to load it from the internet.
                        Picasso.get().load(movie.getBackdropUrl()).into(image);
                    }
                });
        title.setText(movie.getTitle());
        summary.setText(movie.getOverview());
        //reviews.setText(movie.getReviews());

    }
}