package android.moviesapp.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.moviesapp.R;
import android.moviesapp.adapters.MovieAdapter;
import android.moviesapp.data.MovieRepository;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private MovieRepository movieRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup adapter
        var adapter = new MovieAdapter();

        // Setup recycler view
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setAdapter(adapter);
        var isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        recyclerView.setLayoutManager(
            new GridLayoutManager(this, isLandscape ? 2 : 1)
        );

        // Load data
        movieRepo = new MovieRepository(this);
        movieRepo.getPopularMovies().observe(this, adapter::setData);

        // Load the first page
        adapter.setOnRequestNextPageCallback(page ->
            movieRepo.requestMovies(
                page,
                data -> Log.d(TAG, "Loaded " + data.size() + " movie items"),
                error -> Toast.makeText(this, getResources().getString(R.string.main_toast_message_error), Toast.LENGTH_SHORT).show()
            )
        );
    }
}