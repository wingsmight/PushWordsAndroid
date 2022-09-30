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
            return 60 * 60 * 1000L;
        }
    },
    Every2Hours {
        public String getDescription() {
            return "Каждые 2 часа";
        }
        public int getIndex() {
            return 1;
        }
        public Long getMilliseconds() {
            return 2 * 60 * 60 * 1000L;
        }
    },
    Every3Hours {
        public String getDescription() {
            return "Каждые 3 часа";
        }
        public int getIndex() {
            return 2;
        }
        public Long getMilliseconds() {
            return 3 * 60 * 60 * 1000L;
        }
    },
    Every4Hours {
        public String getDescription() {
            return "Каждые 4 часа";
        }
        public int getIndex() {
            return 3;
        }
        public Long getMilliseconds() {
            return 4 * 60 * 60 * 1000L;
        }
    },
    EveryDay {
        public String getDescription() {
            return "Каждый день";
        }
        public int getIndex() {
            return 4;
        }
        public Long getMilliseconds() {
            return  24 * 60 * 60 * 1000L;
        }
    },
    EveryMinute {
        public String getDescription() {
            return "Каждую минуту";
        }
        public int getIndex() {
            return 5;
        }
        public Long getMilliseconds() {
            return 1 * 60 * 1000L;
        }
    };


    public abstract String getDescription();
    public abstract int getIndex();
    public abstract Long getMilliseconds();
}
