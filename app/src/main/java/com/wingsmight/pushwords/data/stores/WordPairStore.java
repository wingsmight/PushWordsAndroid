package com.wingsmight.pushwords.data.stores;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.util.Consumer;

import com.google.gson.Gson;
import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.WordPair;

import java.util.ArrayList;
import java.util.Arrays;

public class WordPairStore {
    public static final String PREF_NAME = "WordPairStore";


    private static WordPairStore instance;

    private Context context;
    private ArrayList<WordPair> wordPairs;
    private Consumer<WordPair> onAdded = wordPair -> { };
    private ArrayList<Consumer<WordPair.State>> onStateChangedList = new ArrayList<>();
    private ArrayList<Consumer<Boolean>> onPushedChangedList = new ArrayList<>();


    private WordPairStore(Context context) {
        this.context = context;

        SharedPreferences preferences = context.getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        Gson gson = new Gson();
        String objectAsJson = preferences.getString(WordPairStore.PREF_NAME, null);
        if (objectAsJson == null || objectAsJson.isEmpty()) {
            wordPairs = new ArrayList<>();
        } else {
            wordPairs = new ArrayList<>(Arrays.asList(gson.fromJson(objectAsJson, WordPair[].class)));
        }

        for (WordPair wordPair : wordPairs) {
            wordPair.addOnStateChanged(state -> {
                for (Consumer<WordPair.State> onStateChanged : onStateChangedList) {
                    onStateChanged.accept(state);
                }
            });

            wordPair.addOnPushedChanged(isPushed -> {
                for (Consumer<Boolean> onPushedChanged : onPushedChangedList) {
                    onPushedChanged.accept(isPushed);
                }
            });
        }
    }


    public static synchronized WordPairStore getInstance(Context context) {
        if (instance == null) {
            synchronized (WordPairStore.class) {
                if(instance == null) {
                    instance = new WordPairStore(context);
                }
            }
        }
        return instance;
    }
    public void save() {
        SharedPreferences preferences = context.getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        Gson gson = new Gson();
        String objectAsJson = gson.toJson(wordPairs);
        preferencesEditor.putString(PREF_NAME, objectAsJson);
        preferencesEditor.apply();
    }
    public void add(WordPair wordPair) {
        if (wordPairs.contains(wordPair)) {
            return;
        }

        wordPairs.add(wordPair);

        onAdded.accept(wordPair);

        wordPair.addOnStateChanged(state -> {
            for (Consumer<WordPair.State> onStateChanged : onStateChangedList) {
                onStateChanged.accept(state);
            }
        });

        wordPair.addOnPushedChanged(isPushed -> {
            for (Consumer<Boolean> onPushedChanged : onPushedChangedList) {
                onPushedChanged.accept(isPushed);
            }
        });
    }
    public WordPair get(WordPair wordPair) {
        for (WordPair existedWordPair : wordPairs) {
            if (existedWordPair.equals(wordPair)) {
                return existedWordPair;
            }
        }

        return wordPair;
    }

    public ArrayList<WordPair> getAll() {
        return wordPairs;
    }
    public ArrayList<WordPair> getLearningOnly() {
        ArrayList<WordPair> learningOnly = new ArrayList<>();
        for (WordPair wordPair : wordPairs) {
            if (wordPair.getState() == WordPair.State.Learning) {
                learningOnly.add(wordPair);
            }
        }
        return learningOnly;
    }
    public ArrayList<WordPair> getLearnedOnly() {
        ArrayList<WordPair> learnedOnly = new ArrayList<>();
        for (WordPair wordPair : wordPairs) {
            if (wordPair.getState() == WordPair.State.Learned) {
                learnedOnly.add(wordPair);
            }
        }
        return learnedOnly;
    }
    public ArrayList<WordPair> getForgottenOnly() {
        ArrayList<WordPair> forgottenOnly = new ArrayList<>();
        for (WordPair wordPair : wordPairs) {
            if (wordPair.getState() == WordPair.State.Forgotten) {
                forgottenOnly.add(wordPair);
            }
        }
        return forgottenOnly;
    }
    public ArrayList<WordPair> getPushedOnly() {
        ArrayList<WordPair> pushedOnly = new ArrayList<>();
        for (WordPair wordPair : wordPairs) {
            if (wordPair.isPushed()) {
                pushedOnly.add(wordPair);
            }
        }
        return pushedOnly;
    }
    public void setOnStateChanged(Consumer<WordPair.State> onStateChanged) {
        this.onStateChangedList.add(onStateChanged);
    }
    public void setOnPushedChanged(Consumer<Boolean> onPushedChanged) {
        this.onPushedChangedList.add(onPushedChanged);
    }
}
