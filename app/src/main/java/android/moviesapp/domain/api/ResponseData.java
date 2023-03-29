package android.moviesapp.domain.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * ResponseData is a small wrapper class that handles the
 * response body of the API. The API wraps the result in
 * a 'result' field. This class extracts the actual result
 * data from the response body.
 * @param <T> Type of the result
 */
public class ResponseData<T> {
    @SerializedName("results")
    private List<T> data;

    public List<T> data() {
        return data;
    }
}