package android.moviesapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenresResponseData<T> {
    @SerializedName("genres")
    private List<T> genres;

    public List<T> data() {
        return genres;
    }
}
