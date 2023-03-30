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

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movies = Collections.emptyList();
    private Consumer<Integer> onNextPage;
    private int pageCounter = 0;

    /**
     * setData sets the data of the adapter.
     * @param movies List of movies
     */
    public void setData(@NotNull List<Movie> movies) {
        if(movies.isEmpty()) {
            onNextPage.accept(1);
            return;
        }

        this.movies = movies;
        notifyItemRangeChanged(pageCounter * 20, movies.size());
    }

    /**
     * setOnNextPage sets the callback that
     * is called when more items are needed.
     * @param onNextPage Callback
     */
    public void setOnNextPage(Consumer<Integer> onNextPage) {
        this.onNextPage = onNextPage;
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

        var movie = movies.get(position);
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
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_movie_poster);
            title = itemView.findViewById(R.id.item_movie_title);
        }
    }
}
