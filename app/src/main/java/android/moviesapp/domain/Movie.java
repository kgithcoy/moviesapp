package android.moviesapp.domain;

import java.util.ArrayList;

public class Movie {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String releaseDate;
    private double voteAverage;
    private int voteCount;
    private ArrayList<Review> reviews;
    private ArrayList<Person> cast;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public ArrayList<Person> getCast() {
        return cast;
    }
}
