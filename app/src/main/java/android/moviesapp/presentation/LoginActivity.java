package android.moviesapp.presentation;

import android.moviesapp.R;
import android.moviesapp.data.MovieRepository;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            EditText usernameEditText = findViewById(R.id.username);
            EditText passwordEditText = findViewById(R.id.password);
            Button loginButton = findViewById(R.id.login);
            ProgressBar loadingProgressBar = findViewById(R.id.loading);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    attemptLogin();
                }
            });
        }
}
