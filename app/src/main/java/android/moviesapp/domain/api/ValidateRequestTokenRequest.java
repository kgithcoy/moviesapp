package android.moviesapp.domain.api;

import com.google.gson.annotations.SerializedName;

public class ValidateRequestTokenRequest {
    @SerializedName("username")
    private final String username;

    @SerializedName("password")
    private final String password;

    @SerializedName("request_token")
    private final String requestToken;

    public ValidateRequestTokenRequest(String username, String password, LoginToken loginToken) {
        this.username = username;
        this.password = password;
        this.requestToken = loginToken.requestToken();
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String requestToken() {
        return requestToken;
    }
}
