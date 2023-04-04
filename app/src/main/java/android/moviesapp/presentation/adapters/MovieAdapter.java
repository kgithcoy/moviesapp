package android.moviesapp.presentation.adapters;

import android.annotation.SuppressLint;
import android.moviesapp.R;
import android.moviesapp.domain.Movie;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movies = Collections.emptyList();
    private Consumer<Integer> onNextPage;
    private Consumer<Movie> onMovieClick;
    private int pageCounter = 0;
    private int selectedGenreId = -1;


    public void setData(@NotNull List<Movie> movies) {
        if(movies.isEmpty()) {
            onNextPage.accept(1);
            return;
        }

        this.movies = movies;
        filterByGenre(selectedGenreId);
        notifyItemRangeChanged(pageCounter * 20, movies.size());
    }

    public void filterByGenre(int genreId) {
        selectedGenreId = genreId;
        if (genreId == -1) {
            filteredMovies = movies;
        } else {
            filteredMovies = new ArrayList<>();
            for (Movie movie : movies) {
                if (movie.getGenreIds().contains(genreId)) {
                    filteredMovies.add(movie);
                }
            }
        }
        notifyDataSetChanged();
    }
    private List<Movie> filteredMovies = Collections.emptyList();

    /**
     * setOnNextPage sets the callback that
     * is called when more items are needed.
     * @param onNextPage Callback
     */
    public void setOnNextPage(Consumer<Integer> onNextPage) {
        this.onNextPage = onNextPage;
    }

    /**
     * setOnMovieClick sets the callback that
     * is called when a movie is clicked.
     * @param onMovieClick Callback
     */
    public void setOnMovieClick(Consumer<Movie> onMovieClick) {
        this.onMovieClick = onMovieClick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = View.inflate(parent.getContext(), R.layout.item_movie, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int page = 1 + (1 + position / 20);
        if (page > pageCounter && onNextPage != null) {
            onNextPage.accept(page);
            pageCounter++;
        }

        var movie = filteredMovies.get(position);
        holder.title.setText(movie.getTitle());
        Picasso.get()
                .load(movie.getPosterUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.poster, new Callback() {
                    public void onSuccess() { }
                    public void onError(Exception err) {
                        // If image can't be loaded from cache,
                        // try to load it from the internet.
                        Picasso.get().load(movie.getPosterUrl()).into(holder.poster);
                    }
                });

        holder.setOnClickListener(() -> onMovieClick.accept(movie));
    }

    @Override
    public int getItemCount() {
        return filteredMovies.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final TextView title;
        private Runnable onClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_movie_poster);
            title = itemView.findViewById(R.id.item_movie_title);
            itemView.setOnClickListener(v -> onClick.run());
        }

        public void setOnClickListener(Runnable onClick) {
            this.onClick = onClick;
        }
    }
}
