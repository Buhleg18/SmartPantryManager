package com.richfield.smartpantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    private List<PantryItem> items;
    private final OnItemClickListener listener;

    public PantryAdapter(List<PantryItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<PantryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);
        // Capitalise first letter for display
        String displayName = item.getName().substring(0, 1).toUpperCase(Locale.ROOT)
                + item.getName().substring(1);
        holder.tvName.setText(displayName);
        holder.tvQty.setText(String.format(Locale.getDefault(), "%.1f %s", item.getQuantity(), item.getUnit()));
        if (item.getExpiry() != null && !item.getExpiry().isEmpty()) {
            holder.tvExpiry.setVisibility(View.VISIBLE);
            holder.tvExpiry.setText("Exp: " + item.getExpiry());
        } else {
            holder.tvExpiry.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvExpiry;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQty = itemView.findViewById(R.id.tvItemQty);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
        }
    }
}
