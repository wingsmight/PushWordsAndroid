package com.wingsmight.pushwords.data;

public enum Language {
    English {
        public String getShort() {
            return "en";
        }
        public Language getOpposite() {
            return Russian;
        }
        public String getDescription() {
            return "Английский";
        }
    }, Russian {
        public String getShort() {
            return "ru";
        }
        public Language getOpposite() {
            return English;
        }
        public String getDescription() {
            return "Русский";
        }
    };

    public abstract String getShort();
    public abstract Language getOpposite();
    public abstract String getDescription();
}
