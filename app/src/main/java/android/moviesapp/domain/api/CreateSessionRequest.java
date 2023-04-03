package android.moviesapp.domain.api;

public class CreateSessionRequest {

    private String request_token;

    public CreateSessionRequest(String requestToken) {
        this.request_token = requestToken;
    }

    // Add getters and setters here
    public String getRequestToken() {
        return request_token;
    }

    public void setRequestToken(String requestToken) {
        this.request_token = requestToken;
    }
}
