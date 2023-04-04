package android.moviesapp.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.moviesapp.R;
import android.moviesapp.data.ListRepository;
import android.moviesapp.domain.List;
import android.moviesapp.domain.Movie;
import android.moviesapp.presentation.adapters.MovieAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ListMoviesActivity extends AppCompatActivity {
    private static final String TAG = "ListMoviesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

        var list = (List)getIntent().getSerializableExtra("list");
        Log.i(TAG, "ListMovies opened; id: " + list.getId());

        var adapter = new MovieAdapter();
        adapter.setOnMovieClick(this::openMovieDetails);

        RecyclerView recyclerView = findViewById(R.id.list_movies_recycler_view);
        recyclerView.setAdapter(adapter);
        var isVertical = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        recyclerView.setLayoutManager(new GridLayoutManager(this, isVertical ? 4 : 2));

        var listRepository = new ListRepository(this);
        listRepository.requestList(
            list.getId(),
            success -> adapter.setData(success.getItems()),
            this::handleError
        );
    }

    private void handleError(Exception err) {
        Toast.makeText(this, getResources().getString(R.string.list_movies_toast_message_error), Toast.LENGTH_SHORT).show();
    }

    private void openMovieDetails(Movie movie) {
        var intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}