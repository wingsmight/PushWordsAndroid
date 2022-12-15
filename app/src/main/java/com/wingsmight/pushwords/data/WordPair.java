package com.wingsmight.pushwords.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.core.util.Consumer;

import com.wingsmight.pushwords.handlers.RepeatWordsNotification;

import java.util.ArrayList;
import java.util.Date;

public class WordPair implements Parcelable {
    private Word original;
    private Word translation;
    private State state = State.None;
    private boolean isPushed = false;
    private boolean isForgotten = false;
    private Date changingDate = new Date();
    private int rememberingCount = 0;

    private transient ArrayList<Consumer<State>> onStateChanged = new ArrayList<>();
    private transient ArrayList<Consumer<Boolean>> onPushedChanged = new ArrayList<>();


    public WordPair(Word original, Word translation) {
        this.original = original;
        this.translation = translation;
    }

    protected WordPair(Parcel parcel) {
        original = parcel.readParcelable(Word.class.getClassLoader());
        translation = parcel.readParcelable(Word.class.getClassLoader());
        state = (State) parcel.readSerializable();
        isPushed = parcel.readByte() != 0;
        isForgotten = parcel.readByte() != 0;
        changingDate = new Date(parcel.readLong());
        rememberingCount = parcel.readInt();
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
        parcel.writeParcelable(original, 0);
        parcel.writeParcelable(translation, 0);
        parcel.writeSerializable(state);
        parcel.writeByte((byte) (isPushed ? 1 : 0));
        parcel.writeByte((byte) (isForgotten ? 1 : 0));
        parcel.writeLong(changingDate.getTime());
        parcel.writeInt(rememberingCount);
    }

    @Override
    public boolean equals(Object object) {
        WordPair anotherWordPair = (WordPair) object;
        return this.original.equals(anotherWordPair.original) &&
                this.translation.equals(anotherWordPair.translation);
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

    @Override
    public int hashCode() {
        return original.hashCode();
    }


    public Word getOriginal() {
        return original;
    }

    public void setOriginal(Word original) {
        this.original = original;

        setChangingDateToNow();
    }

    public Word getTranslation() {
        return translation;
    }

    public void setTranslation(Word translation) {
        this.translation = translation;

        setChangingDateToNow();
    }

    public Language getOriginalLanguage() {
        return original.getLanguage();
    }

    public Language getTranslationLanguage() {
        return translation.getLanguage();
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

    public void setPushed(boolean isPushed, Context context) {
        this.isPushed = isPushed;

        if (onPushedChanged == null) {
            onPushedChanged = new ArrayList<>();
        }

        for (Consumer<Boolean> event : onPushedChanged) {
            event.accept(isPushed);
        }

        setChangingDateToNow();

        RepeatWordsNotification repeatWordsNotification = new RepeatWordsNotification(context);
        if (isPushed) {
            repeatWordsNotification.send(this);
        } else {
            repeatWordsNotification.cancel(this);
        }
    }

    public boolean isForgotten() {
        return isForgotten;
    }

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
