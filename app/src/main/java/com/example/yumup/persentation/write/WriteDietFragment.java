package com.example.yumup.persentation.write;

import static com.example.yumup.data.MealType.toMealTypeName;
import static com.example.yumup.utils.Constants.dateFormat;
import static com.example.yumup.utils.Constants.koreanDateFormat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.yumup.data.DietEntity;
import com.example.yumup.data.YumUpDatabase;
import com.example.yumup.databinding.FragmentWriteDietBinding;
import com.example.yumup.utils.FileUtils;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class WriteDietFragment extends Fragment {
    private FragmentWriteDietBinding binding;
    private YumUpDatabase database;
    private WriteDietViewModel viewModel;

    private File file = null;
    private Boolean selectFromGallery = false;
    private final String[] PERMISSIONS = {Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.CAMERA };
    private  ActivityResultLauncher<String[]> requestPermission = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    int denied = (int) result.values().stream().filter(it -> !it).count();
                    if(denied == 0) {
                        if(selectFromGallery) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                            intent.setType("image/*");
                            fetchPicturesFromGallery.launch(intent);
                        } else {
                            takePicture();
                        }
                    }
                }
            });

    private ActivityResultLauncher<Intent> fetchPicturesFromGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getData() != null) {
                if(result.getData().getData() != null) {
                    viewModel.changeFoodImage(result.getData().getData().toString());
                }
            }
        }
    });

    private ActivityResultLauncher<Uri> cameraActivityLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean saved) {
            if(saved) {
                viewModel.changeFoodImage(file.getAbsolutePath());
            }
        }
    });

    private void takePicture() {
        Pair<File, Uri> fileAndUri = new FileUtils(requireContext()).createImageFileUri();
        file = fileAndUri.first;
        cameraActivityLauncher.launch(fileAndUri.second);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWriteDietBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WriteDietViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = YumUpDatabase.getInstance(requireContext());
        saveDietRecord();
        checkSelectedButton();
        selectPicture();
        selectDate();
    }

    private void saveDietRecord() {
        binding.textviewComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.checkIsInputsEmpty()) {
                    Toast.makeText(requireContext(), "모든 정보를 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    insertDiet();
                    clearInfo();
                    Toast.makeText(requireContext(), "식단 기록을 완료했어요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertDiet() {
        database.dietDao().insert(
                new DietEntity(
                        null,
                        viewModel.foodName.getValue(),
                        viewModel.foodImage.getValue(),
                        viewModel.selectedPlace.getValue(),
                        toMealTypeName(viewModel.selectedTime.getValue()),
                        viewModel.cost.getValue(),
                        generateRandomCalories(),
                        viewModel.selectedDate.getValue()
                )
        );
    }

    private void selectDate() {
        binding.textviewDateValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(requireContext());
                dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                try {
                                    String stringDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                                    String koreanDate = koreanDateFormat.format(dateFormat.parse(stringDate));
                                    viewModel.changeSelectedDate(koreanDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                dialog.show();
            }
        });
    }

    private Float generateRandomCalories() {
        Float calories = new Random().nextFloat() * (800f - 100f) + 100f;
        return Math.round(calories * 100) / 100f;
    }

    private void checkSelectedButton() {
        binding.radiogroupPlace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id == binding.radiobuttonRestaurant1.getId()) {
                    viewModel.changeSelectedPlace(binding.radiobuttonRestaurant1.getText().toString());
                } else if(id == binding.radiobuttonRestaurant2.getId()) {
                    viewModel.changeSelectedPlace(binding.radiobuttonRestaurant2.getText().toString());
                } else if(id == binding.radiobuttonRestaurant3.getId()) {
                    viewModel.changeSelectedPlace(binding.radiobuttonRestaurant3.getText().toString());
                } else if(id == binding.radiobuttonRestaurant4.getId()) {
                    viewModel.changeSelectedPlace(binding.radiobuttonRestaurant4.getText().toString());
                } else {
                    viewModel.changeSelectedPlace("");
                }
            }
        });

        binding.radiogroupMealtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id == binding.radiobuttonBreakfast.getId()) {
                    viewModel.changeSelectedTime(binding.radiobuttonBreakfast.getText().toString());
                } else if(id == binding.radiobuttonLunch.getId()) {
                    viewModel.changeSelectedTime(binding.radiobuttonLunch.getText().toString());
                } else if(id == binding.radiobuttonDinner.getId()) {
                    viewModel.changeSelectedTime(binding.radiobuttonDinner.getText().toString());
                } else if(id == binding.radiobuttonSnack.getId()) {
                    viewModel.changeSelectedTime(binding.radiobuttonSnack.getText().toString());
                } else {
                    viewModel.changeSelectedTime("");
                }
            }
        });
    }

    private void selectPicture() {
        binding.imageviewDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SelectPictureDialogFragment()
                        .setListener(new SelectPictureDialogFragment.DismissBottomSheetListener() {
                            @Override
                            public void selectPicture() {
                                selectFromGallery = true;
                                requestPermission.launch(PERMISSIONS);
                            }

                            @Override
                            public void selectCamera() {
                                selectFromGallery = false;
                                requestPermission.launch(PERMISSIONS);
                            }
                        }).show(getChildFragmentManager(), "");
            }
        });
    }

    private void clearInfo() {
        viewModel.clearInfo();
        binding.radiogroupMealtype.check(-1);
        binding.radiogroupPlace.check(-1);

    }
}