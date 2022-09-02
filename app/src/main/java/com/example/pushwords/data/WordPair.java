package com.example.pushwords.data;

public class WordPair {
    private String original;
    private String translation;


    public WordPair(String original, String translation) {
        this.original = original;
        this.translation = translation;
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
}
