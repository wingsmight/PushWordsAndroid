package com.example.pushwords.data;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class WordPairStore {
    public static final String PREF_NAME = "WordPairStore";


    private static WordPairStore instance;

    private Context context;
    private ArrayList<WordPair> wordPairs;


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
    }


    public static synchronized WordPairStore getInstance(Context context) {
        if (instance == null){
            synchronized (WordPairStore.class) {
                if(instance == null){
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
        String j = gson.toJson(wordPairs);
        preferencesEditor.putString("MyObject", j);
        preferencesEditor.apply();
    }
    public ArrayList<WordPair> getAll() {
        return wordPairs;
    }
    public ArrayList<WordPair> getLearningOnly() {
        ArrayList<WordPair> learningOnly = new ArrayList<>();
        for (WordPair wordPair : wordPairs) {
            if (wordPair.state == WordPair.State.Learning) {
                learningOnly.add(wordPair);
            }
        }
        return learningOnly;
    }
    public ArrayList<WordPair> getLearnedOnly() {
        ArrayList<WordPair> learnedOnly = new ArrayList<>();
        for (WordPair wordPair : wordPairs) {
            if (wordPair.state == WordPair.State.Learned) {
                learnedOnly.add(wordPair);
            }
        }
        return learnedOnly;
    }
    public ArrayList<WordPair> getForgottenOnly() {
        ArrayList<WordPair> forgottenOnly = new ArrayList<>();
        for (WordPair wordPair : wordPairs) {
            if (wordPair.state == WordPair.State.Forgotten) {
                forgottenOnly.add(wordPair);
            }
        }
        return forgottenOnly;
    }
    public void add(WordPair wordPair) {
        wordPairs.add(wordPair);
    }
}
