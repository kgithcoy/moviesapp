package android.moviesapp.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.moviesapp.R;
import android.moviesapp.presentation.adapters.MovieAdapter;
import android.moviesapp.data.MovieRepository;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private final static String TAG = "MainActivity";
    private MovieRepository movieRepo;
    private LinearLayout searchBarContainer;
    private ScrollView movieListContainer;
    private SearchView searchBar;
    private MovieAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieRepo = new MovieRepository(this);

        // Setup adapter
        MovieAdapter trendingAdapter = new MovieAdapter(),
            popularAdapter = new MovieAdapter(),
            topRatedAdapter = new MovieAdapter();

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
        movieRepo.requestTrendingMovies(1, trendingAdapter::setData, this::handleError);

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
        movieRepo.searchMovies(query, 1, searchAdapter::setData, this::handleError);
        return false;
    }

    private void handleError(Exception err) {
        Toast.makeText(this, getResources().getString(R.string.main_toast_message_error), Toast.LENGTH_SHORT).show();
    }
}