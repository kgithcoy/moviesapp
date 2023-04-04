package android.moviesapp.presentation.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.moviesapp.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<android.moviesapp.domain.List> lists = Collections.emptyList();
    private Consumer<android.moviesapp.domain.List> onListClick;
    private Consumer<android.moviesapp.domain.List> onListCopyClick;

    public void setData(List<android.moviesapp.domain.List> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    public void setOnListClick(Consumer<android.moviesapp.domain.List> onListClick) {
        this.onListClick = onListClick;
    }

    public void setOnListCopyClick(Consumer<android.moviesapp.domain.List> onListCopyClick) {
        this.onListCopyClick = onListCopyClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        var list = lists.get(position);
        holder.name.setText(list.getName());
        holder.setOnClickListener(() -> onListClick.accept(list));
        holder.setOnCopyClickListener(() -> onListCopyClick.accept(list));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private Runnable onClick;
        private Runnable onCopyClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_list_name);
            itemView.setOnClickListener(v -> onClick.run());
            itemView.findViewById(R.id.item_list_copy).setOnClickListener(v -> onCopyClick.run());
        }

        public void setOnClickListener(Runnable onClick) {
            this.onClick = onClick;
        }

        public void setOnCopyClickListener(Runnable onCopyClick) {
            this.onCopyClick = onCopyClick;
        }
    }
}
