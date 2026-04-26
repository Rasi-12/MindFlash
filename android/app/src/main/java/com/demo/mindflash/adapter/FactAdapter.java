package com.demo.mindflash.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.demo.mindflash.R;
import com.demo.mindflash.model.Fact;
import java.util.List;

public class FactAdapter extends RecyclerView.Adapter<FactAdapter.FactViewHolder> {

    public interface OnDeleteListener {
        void onDelete(Fact fact);
    }

    private final List<Fact> facts;
    private final OnDeleteListener deleteListener;

    public FactAdapter(List<Fact> facts, OnDeleteListener deleteListener) {
        this.facts = facts;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public FactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_fact, parent, false);
        return new FactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FactViewHolder holder, int position) {
        Fact fact = facts.get(position);
        holder.tvText.setText(fact.getText());
        holder.tvCategory.setText(fact.getCategory());

        if (deleteListener != null) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(fact));
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return facts.size(); }

    static class FactViewHolder extends RecyclerView.ViewHolder {
        TextView tvText, tvCategory;
        ImageButton btnDelete;

        FactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvFactText);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
