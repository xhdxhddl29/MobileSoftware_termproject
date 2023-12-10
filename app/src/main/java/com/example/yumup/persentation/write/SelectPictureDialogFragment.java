package com.example.yumup.persentation.write;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.yumup.databinding.FragmentSelectPictureDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SelectPictureDialogFragment extends BottomSheetDialogFragment {
    public interface DismissBottomSheetListener {
        void selectPicture();
        void selectCamera();
    }
    private FragmentSelectPictureDialogBinding binding;
    private DismissBottomSheetListener dismissBottomSheetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectPictureDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.textviewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissBottomSheetListener.selectPicture();
                dismiss();
            }
        });
        binding.textviewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissBottomSheetListener.selectCamera();
                dismiss();
            }
        });
    }

    public SelectPictureDialogFragment setListener(DismissBottomSheetListener listener) {
        this.dismissBottomSheetListener = listener;
        return this;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}