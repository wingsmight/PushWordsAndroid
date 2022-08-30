package com.example.pushwords.handlers.network.dictionaryApis.examples;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RussianExamplesApi {
    private final String baseUrl = "https://lexicography.online/explanatory/";

    private ArrayList<String> examples = new ArrayList<>();
    private boolean isLoaded = false;


    public void loadExamples(Context context, String word) {
//        examples = []
//        isLoaded = false

        if (word.isEmpty()) {
            return;
        }

        String handledRaw = word.toLowerCase().trim();

        if (handledRaw.isEmpty()) {
            return;
        }
        String url = (baseUrl + handledRaw.charAt(0) + "/" + handledRaw); //.encodeUrl

        Ion.with(context).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String fullHtml) {
                String startExampleRowPattern = "<i>";
                String endExampleRowPattern = "</i>";
                int maxExampleLength = 3;

                String regexString = Pattern.quote(startExampleRowPattern) + "(.*?)" + Pattern.quote(endExampleRowPattern);

                Pattern pattern = Pattern.compile(regexString);
                Matcher matcher = pattern.matcher(fullHtml);

                while (matcher.find() && examples.size() < maxExampleLength) {
                    String example = matcher.group(1);
                    examples.add(example);
                }

//                while (examples.size() < maxExampleLength) {
//                    int startIndex = fullHtml.indexOf(startExampleRowPattern);
//                    if (startIndex >= 0) {
//                        int endIndex = fullHtml.indexOf(endExampleRowPattern);
//
//                        String example =
//                    }


//                    if let range = fullHtml.range(of: startExampleRowPattern) {
//                        let firstIndex = range.upperBound
//                        if let end = fullHtml[firstIndex...].range(of: endExampleRowPattern) {
//                            let range = firstIndex..<end.lowerBound
//
//                            let example = String(fullHtml[range])
//
//                            if let firstExampleLetter = example.first {
//                                if firstExampleLetter.isUppercase && example.count > 12 {
//                                    examples.add(example);
//                                    exampleCount++;
//                                }
//
//                                fullHtml = fullHtml[end.upperBound...]
//                            }
//                        } else {
//                            break
//                        }
//                    } else {
//                        break
//                    }
//                }
//

                Log.i("RussianExamplesApi", examples + " for " + word);

                isLoaded = true;
            }
        });

    }
}
