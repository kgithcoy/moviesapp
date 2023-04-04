package android.moviesapp.presentation;



import androidx.appcompat.app.AppCompatActivity;

import android.moviesapp.R;
import android.moviesapp.data.AuthRepository;
import android.moviesapp.data.ListRepository;
import android.moviesapp.domain.List;
import android.moviesapp.domain.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final static String TAG = "MovieDetailsActivity";
    private List selectedList;

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

        Spinner spin = findViewById(R.id.movie_details_spinner);
        spin.setOnItemSelectedListener(this);

        var adapter = new ArrayAdapter<List>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        var authRepository = new AuthRepository(this);
        var listRepository = new ListRepository(this);
        authRepository.getAccount(
            AuthRepository.getSession(),
            account -> listRepository.requestAccountLists(
                AuthRepository.getSession(),
                account,
                lists -> lists.forEach(adapter::add),
                this::handleError
            ),
            this::handleError
        );

        Button button = findViewById(R.id.movie_details_add_to_list);
        button.setOnClickListener(view -> {
            if (selectedList == null) {
                return;
            }

            listRepository.addMovieToList(
                selectedList,
                movie,
                AuthRepository.getSession(),
                success -> Toast.makeText(this, "Movie added to list", Toast.LENGTH_SHORT).show(),
                this::handleError
            );
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedList = (List) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    private void handleError(Exception err) {
        Log.e(TAG, "Error: " + err.getMessage());
    }
}