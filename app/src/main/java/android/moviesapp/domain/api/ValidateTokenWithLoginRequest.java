package android.moviesapp.domain.api;

public class ValidateTokenWithLoginRequest {

    private String username;
    private String password;
    private String request_token;

    public ValidateTokenWithLoginRequest(String username, String password, String requestToken) {
        this.username = username;
        this.password = password;
        this.request_token = requestToken;
    }


    // add getters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRequest_token() {
        return request_token;
    }

}
