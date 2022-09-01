package com.example.pushwords.handlers.network.dictionaryApis.synonyms;

import androidx.core.util.Consumer;

import java.util.ArrayList;

public interface ISynonymsApi {
    void loadSynonyms(String word, Consumer<ArrayList<ArrayList<String>>> onCompleted);
}
