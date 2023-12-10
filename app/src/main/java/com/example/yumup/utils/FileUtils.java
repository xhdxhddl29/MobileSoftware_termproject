package com.example.yumup.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Pair;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {
    public interface SaveFileCallback {
        void onComplete(Uri uri);
    }

    private Context context;
    public static final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss", Locale.KOREAN);

    public FileUtils(Context context) {
        this.context = context;
    }

    public Pair<File, Uri> createImageFileUri() {
        try {
            File file  = createImageFile();
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            return Pair.create(file, uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public File createImageFile() {
        try {
            String fileName = "IMG" + fileDateFormat.format(new Date());
            File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return new File(path, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveVideoToGallery(Context context, File videoFile, SaveFileCallback saveFileCallback ) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, "Video Title");
        values.put(MediaStore.Video.Media.DESCRIPTION, "Video Description");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");

        // Save the video to the public directory
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + File.separator + "YourVideoFolder");

            ContentResolver resolver = context.getContentResolver();
            Uri collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri insertUri = resolver.insert(collection, values);

            if (insertUri != null) {
                try {
                    try (InputStream inputStream = new FileInputStream(videoFile);
                         OutputStream outputStream = resolver.openOutputStream(insertUri)) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }

                    // 미디어 스캔 트리거
                    MediaScannerConnection.scanFile(
                            context,
                            new String[]{videoFile.getAbsolutePath()},
                            null,
                            (path, uri) -> {
                                // 스캔 완료 시 호출되는 콜백 함수
                                if (uri != null) {
                                    // 성공적으로 갤러리에 추가되었을 때의 처리
                                } else {
                                    // 갤러리에 추가하는데 실패했을 때의 처리
                                }
                            }
                    );

                } catch (IOException e) {
                    e.printStackTrace();
                    // 저장 중 오류 발생 시의 처리
                }
            } else {
                // 갤러리에 추가 실패 시의 처리
            }
            saveFileCallback.onComplete(insertUri);
        } else {
            // For devices running versions below Android Q
            values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
            ContentResolver resolver = context.getContentResolver();
            Uri collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            Uri insertUri = resolver.insert(collection, values);

            if (insertUri != null) {
                try {
                    resolver.openOutputStream(insertUri).close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // 저장 중 오류 발생 시의 처리
                }
            } else {
                // 갤러리에 추가 실패 시의 처리
            }
            saveFileCallback.onComplete(insertUri);

        }
    }

}
