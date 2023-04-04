package android.moviesapp.presentation.adapters;

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

    public void setData(List<android.moviesapp.domain.List> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    public void setOnListClick(Consumer<android.moviesapp.domain.List> onListClick) {
        this.onListClick = onListClick;
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
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private Runnable onClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_list_name);
            itemView.setOnClickListener(v -> onClick.run());
        }

        public void setOnClickListener(Runnable onClick) {
            this.onClick = onClick;
        }
    }
}
