package com.example.pushwords.data;

import android.os.Parcel;
import android.os.Parcelable;

public class WordPair implements Parcelable {
    private String original;
    private String translation;


    public WordPair(String original, String translation) {
        this.original = original;
        this.translation = translation;
    }
    protected WordPair(Parcel parcel) {
        original = parcel.readString();
        translation = parcel.readString();
    }


    public static final Creator<WordPair> CREATOR = new Creator<WordPair>() {
        @Override
        public WordPair createFromParcel(Parcel in) {
            return new WordPair(in);
        }

        @Override
        public WordPair[] newArray(int size) {
            return new WordPair[size];
        }
    };

    public String getOriginal() {
        return original;
    }
    public void setOriginal(String original) {
        this.original = original;
    }
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(original);
        parcel.writeString(translation);
    }
}
