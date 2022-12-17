package com.wingsmight.pushwords.handlers.network.dictionaryApis.examples;

import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnglishExamplesApi implements IExamplesApi {
    private final String baseUrl = "https://www.wordreference.com/definition/";


    private Context context;
    private String currentWord;


    public EnglishExamplesApi(Context context) {
        this.context = context;
    }


    public void loadExamples(String word, Consumer<ArrayList<String>> onCompleted) {
        currentWord = word;

        if (word.isEmpty()) {
            return;
        }

        String handledRaw = word.toLowerCase().trim();
        handledRaw = handledRaw.replaceAll(" ", "%20");

        if (handledRaw.isEmpty()) {
            return;
        }

        ArrayList<String> examples = new ArrayList<>();
        String url = baseUrl + handledRaw;

        Ion.with(context).load(url).asString().setCallback((exception, fullHtml) -> {
            if (exception != null || fullHtml == null)
                return;

            String startExampleRowPattern = "rh_ex\'>";
            String endExampleRowPattern = "</span><";
            int maxExampleLength = 3;

            String regexString = Pattern.quote(startExampleRowPattern) + "(.*?)" + Pattern.quote(endExampleRowPattern);

            Pattern pattern = Pattern.compile(regexString);
            Matcher matcher = pattern.matcher(fullHtml);

            while (matcher.find() && examples.size() < maxExampleLength) {
                String example = matcher.group(1);

                if (!example.contains("<span class")
                    && !example.contains("]</span></span>")) {
                    examples.add(example);
                }
            }

            Log.i("EnglishExamplesApi", examples + " for " + word);

            if (currentWord.equals(word)) {
                onCompleted.accept(examples);
            }
        });
    }
}
