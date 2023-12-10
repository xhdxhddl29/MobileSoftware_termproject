package com.example.yumup.persentation.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.yumup.data.DietEntity;
import com.example.yumup.data.YumUpDatabase;
import com.example.yumup.databinding.ActivityDietDetailBinding;
import com.google.gson.Gson;

public class DietDetailActivity extends AppCompatActivity {
    private YumUpDatabase database;
    private ActivityDietDetailBinding binding;
    private DietDetailViewModel viewModel;
    public static final String SELECTED_DIET = "SELECTED_DIET";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDietDetailBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(DietDetailViewModel.class);
        database = YumUpDatabase.getInstance(this);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
        getSelectedDiet();
        deleteDiet();
    }

    private void getSelectedDiet() {
        try {
            String dietJson = getIntent().getStringExtra(SELECTED_DIET);
            DietEntity diet =new Gson().fromJson(dietJson, DietEntity.class);
            viewModel.changeDiet(diet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteDiet() {
        binding.textviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.diet.getValue() != null) {
                    showDeleteDialog();
                }
            }
        });
    }

    private void showDeleteDialog() {
        new DeleteDialogFragment()
                .setDeleteDietListener(new DeleteDialogFragment.DeleteDietListener() {
                    @Override
                    public void delete() {
                        database.dietDao().delete(viewModel.diet.getValue().id);
                        Toast.makeText(DietDetailActivity.this, "식단을 삭제했어요!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).show(getSupportFragmentManager(), "");
    }
}