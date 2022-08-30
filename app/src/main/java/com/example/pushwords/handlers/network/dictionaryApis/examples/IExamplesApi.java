package com.example.pushwords.handlers.network.dictionaryApis.examples;

public interface IExamplesApi {
    String[] getExamples();
    boolean isLoaded();

    void loadExamples(String word);
}
