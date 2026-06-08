package com.example.juliecreations.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.juliecreations.R;
import com.example.juliecreations.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

    private List<Product> items = new ArrayList<>();
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public void setListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Product> list) {
        items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Product p = items.get(pos);
        h.tvTitle.setText(p.getTitle());
        h.tvPrice.setText(p.getPrice() + " €");
        h.tvCategory.setText(p.getCategory());

        String imageUrl = "http://10.0.2.2:8000/uploads/products/" + p.getImageName();
        Glide.with(h.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(h.ivImage);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onProductClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvPrice, tvCategory;

        VH(View v) {
            super(v);
            ivImage = v.findViewById(R.id.ivProductImage);
            tvTitle = v.findViewById(R.id.tvProductTitle);
            tvPrice = v.findViewById(R.id.tvProductPrice);
            tvCategory = v.findViewById(R.id.tvProductCategory);
        }
    }
}