package android.moviesapp.domain.api;

import com.google.gson.annotations.SerializedName;

public class Session {
    @SerializedName("session_id")
    private String sessionId;

    public String sessionId() {
        return sessionId;
    }
}

