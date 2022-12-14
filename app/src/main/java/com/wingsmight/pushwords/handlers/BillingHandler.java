package com.wingsmight.pushwords.handlers;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseData;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.PurchaseState;
import com.wingsmight.pushwords.data.BillingProduct;
import com.wingsmight.pushwords.data.User;
import com.wingsmight.pushwords.data.database.CloudDatabase;
import com.wingsmight.pushwords.data.stores.UserStore;

public class BillingHandler implements BillingProcessor.IBillingHandler {
    private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4vhdHKPp/PFdBhJGcGe0dzsICYvIEW+HdZjxOvJfnTlbZxxfFYtVo2RRgGHGWrMYDjNOGGrnqFjmMs/Th+6WQUTCSoHv7kSl8D8u2Z7cIYgm76CiKdfKQSnDXOyBHgabsk3xidPzupk8nDNDjcP0C/kYRFRo3PKp37MRYoiiKzCqQJzViyDJ7pOMu6pihtqtTS6wLZGYtItHZ2OCsyo5oI9DPzqmZ8ygtVBxvToUmU7hlJwMvfd3WnEjXwb6gGsu75GfRUqSp2/MXG0FUAkTZbdhjsBalhCA1rSME/ACh3gqhm6tMw5lqJjdvz51OAkJiedia+Q/I+ZHeXoGF6i0AQIDAQAB";;


    protected BillingProcessor billingProcessor;

    private Context context;


    public BillingHandler(Context context) {
        this.context = context;

        billingProcessor = new BillingProcessor(context,
                LICENSE_KEY,
                this);
        billingProcessor.initialize();

        if (!isSubscriptionActive()) {
            setSubscriptionActivation(false);
        }
    }


    @Override
    public void onBillingInitialized() { }
    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        if (productId.equals(BillingProduct.Subscription.getId())) {
            setSubscriptionActivation(true);
        }
    }
    @Override
    public void onBillingError(int errorCode, Throwable error) { }
    @Override
    public void onPurchaseHistoryRestored() { }

    public void release() {
        billingProcessor.release();
    }
    public void subscribe(Activity activity, BillingProduct product) {
        billingProcessor.subscribe(activity, product.getId());
    }

    private boolean isSubscriptionActive() {
        PurchaseInfo subscriptionPurchaseInfo = billingProcessor
                .getSubscriptionPurchaseInfo(BillingProduct.Subscription.getId());
        if (subscriptionPurchaseInfo == null) {
            return false;
        }

        PurchaseData subscriptionPurchaseData = subscriptionPurchaseInfo
                .purchaseData;
        if (subscriptionPurchaseData == null) {
            return false;
        }

        PurchaseState subscriptionPurchaseState = subscriptionPurchaseData
                .purchaseState;
        if (subscriptionPurchaseState == null) {
            return false;
        }

        return subscriptionPurchaseState == PurchaseState.PurchasedSuccessfully
                || subscriptionPurchaseState == PurchaseState.Refunded;
    }
    public void setSubscriptionActivation(boolean isActive) {
        User user = UserStore.getInstance(context).getUser();

        if (user == null) {
            return;
        }

        user.setSubscribed(isActive);
        CloudDatabase.updateUser(user);
    }
}