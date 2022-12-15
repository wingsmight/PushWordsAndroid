package com.wingsmight.pushwords.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Parcelable {
    private String text;
    private Language language;


    public Word(String text, Language language) {
        this.text = text;
        this.language = language;
    }

    protected Word(Parcel parcel) {
        text = parcel.readString();
        language = Language.valueOf(parcel.readString());
    }


    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(language.name());
    }

    @Override
    public boolean equals(Object object) {
        Word anotherWord = (Word) object;
        return this.text.equalsIgnoreCase(anotherWord.text) &&
                this.language == anotherWord.language;
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Language getLanguage() {
        return language;
    }

    public Language setLanguage(Language language) {
        return language = language;
    }
}
