package com.example.pushwords.handlers.network.dictionaryApis.synonyms;

import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnglishSynonymsApi implements ISynonymsApi {
    private final String baseUrl = "https://www.wordreference.com/synonyms/";


    private Context context;
    private String currentWord;


    public EnglishSynonymsApi(Context context) {
        this.context = context;
    }


    public void loadSynonyms(String word, Consumer<ArrayList<ArrayList<String>>> onCompleted) {
        currentWord = word;

        String handledRaw = word.trim();
        handledRaw = handledRaw.replaceAll(" ", "%20");

        if (handledRaw.isEmpty()) {
            return;
        }

        ArrayList<ArrayList<String>> synonymLists = new ArrayList<ArrayList<String>>();
        ArrayList<String> synonymRows = new ArrayList<String>();
        String url = baseUrl + handledRaw;

        Ion.with(context).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String fullHtml) {
                String startSynonymRowPattern = "text-decoration: underline;'>Synonyms:</div><div style='margin-left: 40px;'>";
                String endSynonymRowPattern = "</div>";
                int maxSynonymListLength = 2;
                int maxSynonymInListLength = 3;

                String regexString = Pattern.quote(startSynonymRowPattern) + "(.*?)" + Pattern.quote(endSynonymRowPattern);

                Pattern pattern = Pattern.compile(regexString);
                Matcher matcher = pattern.matcher(fullHtml);

                while (matcher.find() && synonymRows.size() < maxSynonymListLength) {
                    String synonymRow = matcher.group(1);

                    synonymRows.add(synonymRow);
                }

                for (String synonymRow : synonymRows) {
                    String startSynonymPattern = "<span>";
                    String endSynonymPattern = "</span>";

                    regexString = Pattern.quote(startSynonymPattern) + "(.*?)" + Pattern.quote(endSynonymPattern);

                    pattern = Pattern.compile(regexString);
                    matcher = pattern.matcher(synonymRow);

                    ArrayList<String> synonyms = new ArrayList<>();
                    while (matcher.find() && synonyms.size() < maxSynonymInListLength) {
                        String synonym = matcher.group(1);

                        synonyms.add(synonym);
                    }

                    synonymLists.add(synonyms);
                }

                Log.i("EnglishSynonymsApi", synonymLists + " for " + word);

                if (currentWord.equals(word)) {
                    onCompleted.accept(synonymLists);
                }
            }
        });
    }
}
