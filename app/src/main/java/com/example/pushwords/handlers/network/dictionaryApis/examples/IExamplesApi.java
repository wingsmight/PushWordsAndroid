package com.example.pushwords.handlers.network.dictionaryApis.examples;

import androidx.core.util.Consumer;

import java.util.ArrayList;

public interface IExamplesApi {
    void loadExamples(String word, Consumer<ArrayList<String>> onCompleted);
}
