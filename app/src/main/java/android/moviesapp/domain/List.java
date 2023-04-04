package android.moviesapp.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "list")
public class List implements Serializable {
    @PrimaryKey
    private int id;
    private String name;
    private String description;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("item_count")
    private int itemCount;

    @Ignore
    private ArrayList<Movie> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public ArrayList<Movie> getItems() {
        return items;
    }

    public void setItems(ArrayList<Movie> items) {
        this.items = items;
    }

    public String getPosterUrl() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

