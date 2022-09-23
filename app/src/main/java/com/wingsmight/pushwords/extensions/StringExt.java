package com.wingsmight.pushwords.extensions;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.util.Consumer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class StringExt {
    public static void getDrawableFromUrl(String url, Consumer<Drawable> onCompleted) {
        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(input);
                onCompleted.accept(new BitmapDrawable(Resources.getSystem(), bitmap));
            } catch (Exception exception) {
                onCompleted.accept(null);
            }
        }).start();
    }
}
