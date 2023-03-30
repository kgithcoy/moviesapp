package android.moviesapp.adapters;

import android.annotation.SuppressLint;
import android.moviesapp.R;
import android.moviesapp.domain.Movie;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movies = Collections.emptyList();
    private Consumer<Integer> onRequestNextPageCallback;
    private int pageCounter = 0;

    /**
     * setData sets the data of the adapter.
     * @param movies List of movies
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setData(@NotNull List<Movie> movies) {
        if(movies.isEmpty()) {
            onRequestNextPageCallback.accept(1);
            return;
        }

        this.movies = movies;
        notifyDataSetChanged();
    }

    /**
     * setOnRequestNextPageCallback sets the callback that
     * is called when more items are needed.
     * @param onRequestNextPageCallback Callback
     */
    public void setOnRequestNextPageCallback(Consumer<Integer> onRequestNextPageCallback) {
        this.onRequestNextPageCallback = onRequestNextPageCallback;
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
        if (page > pageCounter && onRequestNextPageCallback != null) {
            onRequestNextPageCallback.accept(page);
            pageCounter++;
        }

        var movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        Picasso.get().load(movie.getPosterUrl()).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final TextView title, releaseDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_movie_poster);
            title = itemView.findViewById(R.id.item_movie_title);
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
        }
    }
}
