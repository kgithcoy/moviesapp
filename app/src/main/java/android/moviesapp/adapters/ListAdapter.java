package android.moviesapp.adapters;

import android.content.Context;
import android.moviesapp.R;
import android.moviesapp.domain.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context context;
    private ArrayList<List> lists;

    public ListAdapter(Context context, ArrayList<List> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        List list = lists.get(position);
        holder.name.setText(list.getName());
        holder.description.setText(list.getDescription());
        holder.itemCount.setText(String.valueOf(list.getItemCount()));
        Picasso.get().load(list.getPosterPath()).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView name;
        TextView description;
        TextView itemCount;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.item_list_poster);
            name = itemView.findViewById(R.id.item_list_name);
            description = itemView.findViewById(R.id.item_list_description);
            itemCount = itemView.findViewById(R.id.item_list_count);
        }
    }
}
