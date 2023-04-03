package android.moviesapp.domain.api;

import com.google.gson.annotations.SerializedName;

public class LoginToken {
    @SerializedName("request_token")
    private String requestToken;

    public String requestToken() {
        return requestToken;
    }
}
