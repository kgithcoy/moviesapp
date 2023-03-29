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
}
