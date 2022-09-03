package com.example.pushwords.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Consumer;

public class WordPair implements Parcelable {
    private String original;
    private String translation;
    public State state = State.None;
    public boolean isPushed = false;

    private Consumer<State> onStateChanged = state -> { };
    private Consumer<Boolean> onPushedChanged = state -> { };


    public WordPair(String original, String translation) {
        this.original = original;
        this.translation = translation;
    }
    protected WordPair(Parcel parcel) {
        original = parcel.readString();
        translation = parcel.readString();
        state = (State) parcel.readSerializable();
        isPushed = parcel.readByte() != 0;
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
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(original);
        parcel.writeString(translation);
        parcel.writeSerializable(state);
        parcel.writeByte((byte) (isPushed ? 1 : 0));
    }

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
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;

        onStateChanged.accept(state);
    }
    public boolean isPushed() {
        return isPushed;
    }
    public void setPushed(boolean pushed) {
        isPushed = pushed;

        onPushedChanged.accept(pushed);
    }
    public void setOnStateChanged(Consumer<State> onStateChanged) {
        this.onStateChanged = onStateChanged;
    }
    public void setOnPushedChanged(Consumer<Boolean> onPushedChanged) {
        this.onPushedChanged = onPushedChanged;
    }


    public enum State {
        None,
        Learning,
        Learned,
        Forgotten
    }
}
