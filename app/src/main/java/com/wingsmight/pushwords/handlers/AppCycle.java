package com.wingsmight.pushwords.handlers;

import android.content.Context;

import com.wingsmight.pushwords.data.stores.UserStore;
import com.wingsmight.pushwords.data.stores.WordPairStore;
import com.wingsmight.pushwords.data.database.CloudDatabase;

public final class AppCycle {
    public static void quit(Context context) {
        UserStore userStore = UserStore.getInstance(context);
        CloudDatabase.updateUser(userStore.getUser());

        userStore.save();
        WordPairStore.getInstance(context).save();
    }
}
