package com.wingsmight.pushwords.handlers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public final class InternalStorage {
    private static final String TAG = "InternalStorage";
    private static final String DEFAULT_DIRECTORY = "DEFAULT_DIRECTORY";


    public static File writeFile(Context context, String fileName, byte[] bytes) {
        File directory = new File(context.getFilesDir(), DEFAULT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            File file = new File(directory, fileName);
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
    public static File readFile(Context context, String fileName) {
        File directory = new File(context.getFilesDir(), DEFAULT_DIRECTORY);
        if (!directory.exists()) {
            return null;
        }

        try {
            File file = new File(directory, fileName);
            if (!file.exists()) {
                return null;
            }

            return file;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

            return null;
        }
    }
    public static void writeFile(Context context, String fileName, String content){
        File directory = new File(context.getFilesDir(), DEFAULT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            File file = new File(directory, fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
