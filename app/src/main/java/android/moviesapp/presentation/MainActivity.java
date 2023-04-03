package android.moviesapp.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.moviesapp.R;
import android.moviesapp.domain.Movie;
import android.moviesapp.presentation.adapters.MovieAdapter;
import android.moviesapp.data.MovieRepository;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private final static String TAG = "MainActivity";
    private MovieRepository movieRepo;
    private LinearLayout searchBarContainer;
    private ScrollView movieListContainer;
    private SearchView searchBar;
    private MovieAdapter searchAdapter;

    private boolean toastShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieRepo = new MovieRepository(this);

        // Setup adapter
        MovieAdapter trendingAdapter = new MovieAdapter(),
            popularAdapter = new MovieAdapter(),
            topRatedAdapter = new MovieAdapter();
        trendingAdapter.setOnMovieClick(this::openMovieDetails);
        popularAdapter.setOnMovieClick(this::openMovieDetails);
        topRatedAdapter.setOnMovieClick(this::openMovieDetails);

        // Setup recycler view
        RecyclerView recyclerViewSearch = findViewById(R.id.main_recycler_view_search),
            recyclerViewPopular = findViewById(R.id.main_recycler_view_popular),
            recyclerViewTrending = findViewById(R.id.main_recycler_view_trending),
            recyclerViewTopRated = findViewById(R.id.main_recycler_view_top_rated);

        recyclerViewPopular.setAdapter(popularAdapter);
        recyclerViewPopular.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        recyclerViewTrending.setAdapter(trendingAdapter);
        recyclerViewTrending.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        recyclerViewTopRated.setAdapter(topRatedAdapter);
        recyclerViewTopRated.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        // Load data
        movieRepo.requestTrendingMovies(1, data -> {
            trendingAdapter.setData(data);
            toastShown = false;
        }, this::handleError);

        movieRepo.getMoviesByPopularity().observe(this, popularAdapter::setData);
        popularAdapter.setOnNextPage(page ->
            movieRepo.requestPopularMovies(page, data -> Log.d(TAG, "Loaded " + data.size() + " movie items"), this::handleError)
        );

        movieRepo.getMoviesByRating().observe(this, topRatedAdapter::setData);
        topRatedAdapter.setOnNextPage(page ->
            movieRepo.requestTopRatedMovies(page, data -> Log.d(TAG, "Loaded " + data.size() + " movie items"), this::handleError)
        );

        // Search bar
        searchBarContainer = findViewById(R.id.main_search_bar_container);
        movieListContainer = findViewById(R.id.main_movie_lists_container);
        searchBar = findViewById(R.id.main_search_bar);
        searchBar.setOnQueryTextListener(this);

        searchAdapter = new MovieAdapter();
        searchAdapter.setOnMovieClick(this::openMovieDetails);
        recyclerViewSearch.setAdapter(searchAdapter);
        var isVertical = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        recyclerViewSearch.setLayoutManager(new GridLayoutManager(this, isVertical ? 4 : 2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Set options menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // When search icon is clicked, show and focus searchbar
        if (item.getItemId() == R.id.action_search) {
            searchBarContainer.setVisibility(SearchView.VISIBLE);
            movieListContainer.setVisibility(SearchView.GONE);
            searchBar.requestFocus();
            var imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchBarContainer.setVisibility(SearchView.GONE);
        movieListContainer.setVisibility(SearchView.VISIBLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query.isEmpty()) return false;
        movieRepo.searchMovies(query, 1, data -> {
            searchAdapter.setData(data);
            toastShown = false;
        }, this::handleError);
        return false;
    }

    private void handleError(Exception err) {
        if (!toastShown) {
            Toast.makeText(this, getResources().getString(R.string.main_toast_message_error), Toast.LENGTH_SHORT).show();
            toastShown = true;
        }
        // Hide the trending recycler view
        RecyclerView recyclerViewTrending = findViewById(R.id.main_recycler_view_trending);
        recyclerViewTrending.setVisibility(View.GONE);

        TextView trendingLabel = findViewById(R.id.main_trending_label);
        trendingLabel.setVisibility(View.GONE);

    }

    private void openMovieDetails(Movie movie) {
        var intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}