package com.wingsmight.pushwords.data;

public enum BillingProduct {
    Subscription {
        public String getId() {
            return "";
        }
    };

    public abstract String getId();
}
