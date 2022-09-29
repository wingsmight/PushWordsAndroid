package com.wingsmight.pushwords;

import android.app.Application;

import com.wingsmight.pushwords.data.stores.UserStore;
import com.wingsmight.pushwords.handlers.BillingHandler;

public class PushWordsApplication extends Application {
    private BillingHandler billingHandler;


    @Override
    public void onCreate() {
        super.onCreate();

        UserStore.getInstance(this);
        billingHandler = new BillingHandler(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (billingHandler != null) {
            billingHandler.release();
        }
    }


    public BillingHandler getBillingHandler() {
        return billingHandler;
    }
}