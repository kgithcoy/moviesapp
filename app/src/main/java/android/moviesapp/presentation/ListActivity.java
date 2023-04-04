package android.moviesapp.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.moviesapp.R;
import android.moviesapp.data.AuthRepository;
import android.moviesapp.data.ListRepository;
import android.moviesapp.domain.Account;
import android.moviesapp.presentation.adapters.ListAdapter;
import android.os.Bundle;
import android.widget.Toast;

public class ListActivity extends AppCompatActivity {
    private ListRepository listRepository;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        var authRepository = new AuthRepository(this);
        listRepository = new ListRepository(this);

        authRepository.getAccount(
            AuthRepository.getSession(),
            this::onAuthSuccess,
            this::handlerError
        );

        RecyclerView recyclerView = findViewById(R.id.lists_recycler_view);
        listAdapter = new ListAdapter();
        listAdapter.setOnListClick(list -> {
            var intent = new Intent(this, ListMoviesActivity.class);
            intent.putExtra("list", list);
            startActivity(intent);
        });
        listAdapter.setOnListCopyClick(list -> {
            var clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            var clip = ClipData.newPlainText("movie-list", "https://www.themoviedb.org/list/" + list.getId());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, getResources().getString(R.string.lists_toast_message_copied), Toast.LENGTH_SHORT).show();
        });
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