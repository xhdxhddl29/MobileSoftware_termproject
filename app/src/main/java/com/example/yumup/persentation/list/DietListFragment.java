package com.example.yumup.persentation.list;

import static com.example.yumup.persentation.detail.DietDetailActivity.SELECTED_DIET;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yumup.data.DietEntity;
import com.example.yumup.data.YumUpDatabase;
import com.example.yumup.databinding.FragmentDietListBinding;
import com.example.yumup.persentation.detail.DietDetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DietListFragment extends Fragment {
    private FragmentDietListBinding binding;
    private YumUpDatabase database = null;
    private  ArrayList<DietEntity> diets = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDietListBinding binding = FragmentDietListBinding.inflate(inflater, container, false);
        this.binding = binding;
        database = YumUpDatabase.getInstance(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(binding != null) {
            setDietListAdapter();
        }
    }

    private void setDietListAdapter() {
        ArrayList<DietEntity> localDietList = new ArrayList<>(database.dietDao().getAll());
        if(!diets.equals(localDietList)) {
            binding.recyclerviewDiets.setAdapter(
                    new DietListAdapter(
                            localDietList,
                            new DietListAdapter.SelectDietListener() {
                                @Override
                                public void select(DietEntity diet) {
                                    navToDietDetail(diet);
                                }
                            }
                    )
            );
            diets = localDietList;
        }
        if(localDietList.isEmpty()) {
            binding.textviewEmptyDiets.setVisibility(View.VISIBLE);
        } else {
            binding.textviewEmptyDiets.setVisibility(View.GONE);
        }
    }

    private void navToDietDetail(DietEntity diet) {
        Intent intent = new Intent(requireContext(), DietDetailActivity.class);
        intent.putExtra(SELECTED_DIET, new Gson().toJson(diet));
        startActivity(intent);
    }
}