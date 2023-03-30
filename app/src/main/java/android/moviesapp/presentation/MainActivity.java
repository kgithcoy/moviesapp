package android.moviesapp.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private final static String TAG = "MainActivity";
    private MovieRepository movieRepo;
    private SearchView searchBar;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup adapter
        adapter = new MovieAdapter();

        // Setup recycler view
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setAdapter(adapter);
        var isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        recyclerView.setLayoutManager(
            new GridLayoutManager(this, isLandscape ? 4 : 2)
        );

        // Load data
        movieRepo = new MovieRepository(this);
        movieRepo.getPopularMovies().observe(this, adapter::setData);

        // Load the first page
        adapter.setOnNextPage(page ->
            movieRepo.requestMovies(
                page,
                data -> Log.d(TAG, "Loaded " + data.size() + " movie items"),
                error -> Toast.makeText(this, getResources().getString(R.string.main_toast_message_error), Toast.LENGTH_SHORT).show()
            )
        );

        // Search bar
        searchBar = findViewById(R.id.main_search_bar);
        searchBar.setOnQueryTextListener(this);
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
            searchBar.setVisibility(SearchView.VISIBLE);
            searchBar.requestFocus();
            var imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchBar.setVisibility(SearchView.GONE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        movieRepo.searchMovies(query, 1, adapter::setData, error ->
            Toast.makeText(this, getResources().getString(R.string.main_toast_message_error), Toast.LENGTH_SHORT).show()
        );
        return false;
    }
}