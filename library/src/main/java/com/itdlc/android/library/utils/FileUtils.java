package com.itdlc.android.library.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by felear on 2018/3/23.
 */

public class FileUtils {

    // 把File对象转化成MultipartBody type "image/png"
    public static MultipartBody filePathsToMultipartBody(List<String> paths) {

        ArrayList<File> files = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            String strPath = paths.get(i);
            files.add(new File(strPath));
        }
        return filesToMultipartBody(files);
    }

    // 把File对象转化成MultipartBody type "image/png"
    public static MultipartBody pathToMultipartBody(String path) {

        File file = new File(path);
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        return filesToMultipartBody(files);
    }

    // 把File对象转化成MultipartBody type "image/png"
    public static MultipartBody filesToMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    // File转化成MultipartBody.Part
    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files, String type) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(type), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    // 获取略缩图
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

}
