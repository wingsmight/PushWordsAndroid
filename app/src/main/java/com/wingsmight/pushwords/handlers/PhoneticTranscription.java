package com.wingsmight.pushwords.handlers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public final class PhoneticTranscription {
    private static final String MISSING_TRANSCRIPTION = "?";
    private static final String FILE_DIRECTORY = "PhoneticTranscriptions";
    private static final String WORD_TRANSCRIPTION_FILE_PATH = "Words.json";
    private static final String LETTER_MAP_FILE_PATH = "LetterMap.json";

    private Map<String, String> wordTranscriptions;
    private Map<String, String> letterMap;

    private static PhoneticTranscription instance;

    private PhoneticTranscription(Context context) {
        try {
            InputStream stream = context.getAssets().open(FILE_DIRECTORY + "/" + WORD_TRANSCRIPTION_FILE_PATH);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            String wordTranscriptionFileText = new String(buffer);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Map.class,
                            (JsonDeserializer<Map<String, String>>) (json1, typeOfT, context1) ->
                                    new Gson().fromJson(json1, typeOfT))
                    .create();

            try {
                wordTranscriptions = gson
                        .fromJson(wordTranscriptionFileText, new TypeToken<Map<String, String>>() {
                        }.getType());
            } catch (JsonSyntaxException exception) {
                exception.printStackTrace();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            InputStream stream = context
                    .getAssets()
                    .open(FILE_DIRECTORY + "/" + LETTER_MAP_FILE_PATH);

            InputStreamReader inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_16);

            String letterMapText;
            try (Scanner scanner = new Scanner(inputStreamReader)) {
                letterMapText = scanner.useDelimiter("\\A").next();
            }
            inputStreamReader.close();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Map.class,
                            (JsonDeserializer<Map<String, String>>) (json1, typeOfT, context1) ->
                                    new Gson().fromJson(json1, typeOfT))
                    .create();

            try {
                letterMap = gson
                        .fromJson(letterMapText, new TypeToken<Map<String, String>>() {
                        }.getType());
            } catch (JsonSyntaxException exception) {
                exception.printStackTrace();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        wordTranscriptions = replaceKeys(wordTranscriptions, letterMap);
    }

    public static PhoneticTranscription getInstance(Context context) {
        if (instance == null)
            instance = new PhoneticTranscription(context);

        return instance;
    }

    public String get(String word) {
        String transcription = wordTranscriptions.get(word.toLowerCase());
        if (transcription == null ||
                transcription.isEmpty()) {
            transcription = MISSING_TRANSCRIPTION;
        }

        return "[" + transcription + "]";
    }

    private Map<String, String> replaceKeys(Map<String, String> rawWordTranscriptions, Map<String, String> letterMap) {
        for (Map.Entry<String, String> wordTranscriptionPair : rawWordTranscriptions.entrySet()) {
            String replacedTranscription = wordTranscriptionPair.getValue();

            for (Map.Entry<String, String> letterPair : letterMap.entrySet()) {
                replacedTranscription = replacedTranscription.replace(letterPair.getKey(), letterPair.getValue());
            }

            wordTranscriptionPair.setValue(replacedTranscription);
        }

        return rawWordTranscriptions;
    }
}
