package com.example.pushwords.handlers.network.dictionaryApis.synonyms;

import android.content.Context;
import android.os.AsyncTask;

import androidx.core.util.Consumer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RussianSynonymsApi implements ISynonymsApi {
    private final String baseUrl = "https://synonymonline.ru/";


    private Context context;
    private RetrieveSiteData site = new RetrieveSiteData("");
    private Consumer<ArrayList<ArrayList<String>>> onCompleted;
    private String currentWord = "";


    public RussianSynonymsApi(Context context) {
        this.context = context;
    }

    private void onSuccess(String proceedWord, String fullHtml) {
        String startSynonymRowPattern = "<ol class=\"list-words list-columns\"> <li>";
        String endSynonymRowPattern = "</li> </ol>";
        int maxSynonymListLength = 2;
        int maxSynonymInListLength = 3;

        String regexString = Pattern.quote(startSynonymRowPattern) + "(.*?)" + Pattern.quote(endSynonymRowPattern);

        Pattern pattern = Pattern.compile(regexString);
        Matcher matcher = pattern.matcher(fullHtml);

        matcher.find();
        String synonymRow = matcher.group(1);
        String[] synonyms = synonymRow.split("</li><li>");

        ArrayList<ArrayList<String>> synonymLists = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < maxSynonymListLength; i++) {
            synonymLists.add(new ArrayList<>());

            for (int j = 0; j < maxSynonymInListLength; j++) {
                int fullIndex = (maxSynonymInListLength * i) + j;

                if (fullIndex >= synonyms.length)
                    break;

                synonymLists.get(i).add(synonyms[fullIndex]);
            }
        }

        if (currentWord.equals(proceedWord)) {
            onCompleted.accept(synonymLists);
        }
    }


    public void loadSynonyms(String word, Consumer<ArrayList<ArrayList<String>>> onCompleted) {
        currentWord = word;

        if (word.isEmpty()) {
            return;
        }

        String handledRaw = word.toLowerCase().trim();
        if (handledRaw.isEmpty()) {
            return;
        }

        String url = (baseUrl + Character.toUpperCase(handledRaw.charAt(0)) + "/" + handledRaw);

        this.onCompleted = onCompleted;

        site.cancel(true);
        RetrieveSiteData site = new RetrieveSiteData(word);
        site.execute(url);
    }

    private class RetrieveSiteData extends AsyncTask<String, Void, String> {
        private String proceedWord;


        public RetrieveSiteData(String proceedWord) {
            this.proceedWord = proceedWord;
        }


        @Override
        protected String doInBackground(String... urls) {
            StringBuilder builder = new StringBuilder(100000);

            for (String url : urls) {
                try {
                    URLConnection connection = (new URL(url)).openConnection();
                    connection.setConnectTimeout(50000);
                    connection.setReadTimeout(50000);
                    connection.connect();

                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder html = new StringBuilder();
                    for (String line; (line = reader.readLine()) != null; ) {
                        html.append(line);
                    }
                    in.close();

                    String fullHtml = html.toString();

                    onSuccess(proceedWord, fullHtml);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return builder.toString();
        }
    }
}
