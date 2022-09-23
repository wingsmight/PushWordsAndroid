package com.wingsmight.pushwords.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Consumer;

import com.wingsmight.pushwords.ui.learnedTab.TestSettingsTab;

import java.util.ArrayList;
import java.util.Date;

public class WordPair implements Parcelable {
    private String original;
    private String translation;
    private Language originalLanguage;
    private State state = State.None;
    private boolean isPushed = false;
    private boolean isForgotten = false;
    private Date changingDate = new Date();
    private int rememberingCount = 0;

    private transient ArrayList<Consumer<State>> onStateChanged = new ArrayList<>();
    private transient ArrayList<Consumer<Boolean>> onPushedChanged = new ArrayList<>();


    public WordPair(String original, String translation, Language originalLanguage) {
        this.original = original;
        this.translation = translation;
        this.originalLanguage = originalLanguage;
    }
    protected WordPair(Parcel parcel) {
        original = parcel.readString();
        translation = parcel.readString();
        originalLanguage = Language.valueOf(parcel.readString());
        state = (State) parcel.readSerializable();
        isPushed = parcel.readByte() != 0;
        isForgotten = parcel.readByte() != 0;
    }


    public static final Creator<WordPair> CREATOR = new Creator<WordPair>() {
        @Override
        public WordPair createFromParcel(Parcel in) {
            WordPair wordPair = new WordPair(in);
            wordPair.onStateChanged = new ArrayList<>();
            wordPair.onPushedChanged = new ArrayList<>();

            return wordPair;
        }
        @Override
        public WordPair[] newArray(int size) {
            return new WordPair[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(original);
        parcel.writeString(translation);
        parcel.writeString(originalLanguage.name());
        parcel.writeSerializable(state);
        parcel.writeByte((byte) (isPushed ? 1 : 0));
        parcel.writeByte((byte) (isForgotten ? 1 : 0));
    }
    @Override
    public boolean equals(Object object) {
        WordPair anotherWord = (WordPair) object;
        return this.original.equalsIgnoreCase(anotherWord.original)
                && this.translation.equalsIgnoreCase(anotherWord.translation);
    }
    public void remember(int requiredRememberingCount) {
        rememberingCount++;

        if (rememberingCount >= requiredRememberingCount) {
            rememberingCount = 0;
            isForgotten = false;
        }
    }
    public void forgot() {
        isForgotten = true;

        rememberingCount = 0;
    }


    public String getOriginal() {
        return original;
    }
    public void setOriginal(String original) {
        this.original = original;

        setChangingDateToNow();
    }
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;

        setChangingDateToNow();
    }
    public Language getOriginalLanguage() {
        return originalLanguage;
    }
    public Language getTranslationLanguage() {
        return originalLanguage.getOpposite();
    }
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;

        for (Consumer<State> event : onStateChanged) {
            event.accept(state);
        }

        setChangingDateToNow();
    }
    public boolean isPushed() {
        return isPushed;
    }
    public void setPushed(boolean isPushed) {
        this.isPushed = isPushed;

        if (onPushedChanged == null) {
            onPushedChanged = new ArrayList<>();
        }

        for (Consumer<Boolean> event : onPushedChanged) {
            event.accept(isPushed);
        }

        setChangingDateToNow();
    }
    public boolean isForgotten() {
        return isForgotten;
    }
//    public void setForgotten(boolean isForgotten) {
//        this.isForgotten = isForgotten;
//
//        setChangingDateToNow();
//    }
    public Date getChangingDate() {
        return changingDate;
    }
    public void addOnStateChanged(Consumer<State> onStateChanged) {
        if (this.onStateChanged == null)
            this.onStateChanged = new ArrayList<>();

        this.onStateChanged.add(onStateChanged);
    }
    public void addOnPushedChanged(Consumer<Boolean> onPushedChanged) {
        if (this.onPushedChanged == null)
            this.onPushedChanged = new ArrayList<>();

        this.onPushedChanged.add(onPushedChanged);
    }

    private void setChangingDateToNow() {
        changingDate = new Date();
    }


    public enum State {
        None,
        Learning,
        Learned,
    }
}
