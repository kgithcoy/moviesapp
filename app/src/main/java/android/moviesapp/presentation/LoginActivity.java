package android.moviesapp.presentation;

import android.content.Intent;
import android.moviesapp.R;
import android.moviesapp.data.AuthRepository;
import android.moviesapp.domain.api.LoginToken;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private LoginToken loginToken;
    private AuthRepository authRepository;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authRepository = new AuthRepository(this);
        authRepository.getLoginToken(
            this::onLoginTokenSuccess,
            this::handleError
        );

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(v ->
            authRepository.login(
                loginToken,
                username.getText().toString(),
                password.getText().toString(),
                this::onLoginSuccess,
                this::handleError
            )
        );

        Button skipButton = findViewById(R.id.continue_without_login);
        skipButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void onLoginTokenSuccess(LoginToken loginToken) {
        this.loginToken = loginToken;
        authRepository.loginWithStoredAccount(
            loginToken,
            this::onLoginSuccess,
            error -> Log.d(TAG, "Login with stored account failed")
        );
    }

    private void onLoginSuccess(LoginToken loginToken) {
       authRepository.retrieveSession(
           loginToken,
           success -> {
               Log.d(TAG, "Session ID: " + success.sessionId());
               if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                   authRepository.storeAccount(
                       username.getText().toString(),
                       password.getText().toString()
                   );
               }
               startActivity(new Intent(this, MainActivity.class));
               finish();
           },
           this::handleError
       );
    }

    private void handleError(Exception err) {
        Toast.makeText(this, getResources().getString(R.string.login_toast_message_error), Toast.LENGTH_SHORT).show();
    }
}
