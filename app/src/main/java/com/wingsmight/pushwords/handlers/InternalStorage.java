package com.wingsmight.pushwords.handlers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public final class InternalStorage {
    public static final String LEARNING_CATEGORIES_DIRECTORY = "LearningCategories";

    private static final String TAG = "InternalStorage";


    public static File writeFile(Context context, String fileName, byte[] bytes) {
        File file = new File(context.getFilesDir(), fileName);

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }

            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();

            return file;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

            return null;
        }
    }
    public static boolean exists(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);

        return file.exists();
    }
    public static File[] getFiles(Context context, String directoryName) {
        File directory = new File(context.getFilesDir(), directoryName);
        if (!directory.exists()) {
            return new File[0];
        }

        return directory.listFiles();
    }
    public static File readFile(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            return null;
        }

        return file;
    }
}
