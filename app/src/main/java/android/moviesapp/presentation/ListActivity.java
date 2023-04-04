package android.moviesapp.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.moviesapp.R;
import android.moviesapp.data.AuthRepository;
import android.moviesapp.data.ListRepository;
import android.moviesapp.domain.Account;
import android.moviesapp.presentation.adapters.ListAdapter;
import android.os.Bundle;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {
    private ListRepository listRepository;
    private AuthRepository authRepository;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        authRepository = new AuthRepository(this);
        listRepository = new ListRepository(this);

        authRepository.getAccount(
            AuthRepository.getSession(),
            this::onAuthSuccess,
            this::handlerError
        );

        RecyclerView recyclerView = findViewById(R.id.lists_recycler_view);
        listAdapter = new ListAdapter();
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void onAuthSuccess(Account account) {
        listRepository.requestAccountLists(
            AuthRepository.getSession(),
            account,
            success -> listAdapter.setData(success),
            this::handlerError
        );
    }

    private void handlerError(Exception err) {
        Toast.makeText(this, getResources().getString(R.string.lists_toast_message_error), Toast.LENGTH_SHORT).show();
    }
}