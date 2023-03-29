package android.moviesapp.domain;

import java.util.ArrayList;

public class List {
    private int id;
    private String name;
    private String description;
    private String posterPath;
    private int itemCount;
    private ArrayList<Movie> items;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getItemCount() {
        return itemCount;
    }

    public ArrayList<Movie> getItems() {
        return items;
    }
}

