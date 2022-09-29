package com.wingsmight.pushwords.handlers;

import android.text.TextUtils;
import android.util.Patterns;

public final class Email {
    public static boolean isValid(CharSequence target) {
        return (!TextUtils.isEmpty(target)
                && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
