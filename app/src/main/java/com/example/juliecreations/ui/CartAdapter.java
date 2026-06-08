package com.example.juliecreations.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.juliecreations.R;
import com.example.juliecreations.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    private List<CartItem> items = new ArrayList<>();
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public void setListener(OnCartChangeListener listener) {
        this.listener = listener;
    }

    public void setItems(List<CartItem> list) {
        items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        CartItem item = items.get(pos);
        h.tvName.setText(item.getName());
        h.tvPrice.setText(item.getPrice() + " €");
        h.tvQuantity.setText("Quantité : " + item.getQuantity());

        if (item.getInstructions() != null && !item.getInstructions().isEmpty()) {
            h.tvInstructions.setText("📝 " + item.getInstructions());
            h.tvInstructions.setVisibility(View.VISIBLE);
        } else {
            h.tvInstructions.setVisibility(View.GONE);
        }

        h.btnRemove.setOnClickListener(v -> {
            items.remove(pos);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, items.size());
            if (listener != null) listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<CartItem> getItems() {
        return items;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity, tvInstructions;
        Button btnRemove;

        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvCartItemName);
            tvPrice = v.findViewById(R.id.tvCartItemPrice);
            tvQuantity = v.findViewById(R.id.tvCartItemQuantity);
            tvInstructions = v.findViewById(R.id.tvCartItemInstructions);
            btnRemove = v.findViewById(R.id.btnRemoveFromCart);
        }
    }
}