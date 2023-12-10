package com.example.yumup.persentation.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumup.BR;
import com.example.yumup.data.DietEntity;
import com.example.yumup.databinding.ItemDietBinding;

import java.util.ArrayList;

public class DietListAdapter extends RecyclerView.Adapter<DietListAdapter.DietListViewHolder> {
    private ArrayList<DietEntity> diets;
    private SelectDietListener selectDietListener;

    public DietListAdapter(ArrayList<DietEntity> diets, SelectDietListener selectDietListener) {
        this.diets = diets;
        this.selectDietListener = selectDietListener;
    }
    public interface SelectDietListener {
        void select(DietEntity diet);
    }

    @NonNull
    @Override
    public DietListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDietBinding binding = ItemDietBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DietListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DietListViewHolder holder, int position) {
        DietEntity diet = diets.get(position);
        holder.binding.setVariable(BR.dietEntity, diet);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDietListener.select(diet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return diets.size();
    }

    public class DietListViewHolder extends RecyclerView.ViewHolder {
        public ItemDietBinding binding;

        public DietListViewHolder(ItemDietBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
