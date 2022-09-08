package com.wingsmight.pushwords.data;

public enum NotificationFrequency {
    EveryHour {
        public String getDescription() {
            return "Каждый час";
        }
        public int getIndex() {
            return 0;
        }
        public Long getMilliseconds() {
            return 24 * 60 * 60 * 1000L;
        }
    },
    EveryDay {
        public String getDescription() {
            return "Каждый день";
        }
        public int getIndex() {
            return 1;
        }
        public Long getMilliseconds() {
            return 60 * 60 * 1000L;
        }
    };


    public abstract String getDescription();
    public abstract int getIndex();
    public abstract Long getMilliseconds();
}
