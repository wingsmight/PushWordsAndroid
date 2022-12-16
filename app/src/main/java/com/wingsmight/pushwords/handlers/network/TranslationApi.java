package com.wingsmight.pushwords.handlers.network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.core.util.Consumer;

import com.wingsmight.pushwords.data.Language;
import com.wingsmight.pushwords.data.Word;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class TranslationApi {
    private static final String url = "https://translate.api.cloud.yandex.net/translate/v2/translate";
    private static final String folderId = "b1g0hvncpdnasa6b7adg";
    private static final String apiKey = "AQVN1SkizcUmkyFnCjfxU3sKGUlCRQ14K50-eojB";

    private TranslationTask task = new TranslationTask("", Language.English);

    private Consumer<Word> onCompleted;
    private Runnable onFailure;


    public void translate(String text, Language targetLanguage, Consumer<Word> onCompleted, Runnable onFailure) {
        if (text.isEmpty()) {
            task.cancel(true);

            onFailure();

            return;
        }

        task.cancel(true);
        task = new TranslationTask(text, targetLanguage);
        task.execute();

        this.onCompleted = onCompleted;
        this.onFailure = onFailure;
    }

    private void onSuccess(String response, Language language) {
        String translationText = parse(response);
        Word translation = new Word(translationText, language);
        onCompleted.accept(translation);
    }

    private void onFailure() {
        if (onFailure != null) {
            onFailure.run();
        }
    }

    private String parse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            String translation = jsonObject
                    .getJSONArray("translations")
                    .getJSONObject(0)
                    .getString("text");

            return translation;
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        return "";
    }

    @SuppressLint("StaticFieldLeak")
    public class TranslationTask extends AsyncTask<Void, Void, Boolean> {
        private final String originalText;
        private final Language targetLanguage;


        public TranslationTask(String originalText, Language targetLanguage) {
            this.originalText = originalText;
            this.targetLanguage = targetLanguage;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isSucceed = false;

            try {
                String response = performPostCall(url, new HashMap<String, String>() {
                    private static final long serialVersionUID = 1L;

                    {
                        put("Accept", "application/json");
                        put("Content-Type", "application/json");
                    }
                });

                if (!response.equals("")) {
                    onSuccess(response, targetLanguage);

                    isSucceed = true;
                }
            } catch (Exception e) {
                Exception i = e;
            }

            if (!isSucceed) {
                onFailure();
            }

            return isSucceed;
        }

        public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {
            URL requestUrl;
            String response = "";
            try {
                requestUrl = new URL(requestURL);

                HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                connection.setRequestMethod("POST");

                connection.setDoInput(true);
                connection.setDoOutput(true);

                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Api-Key " + apiKey);

                JSONObject root = new JSONObject();
                root.put("targetLanguageCode", targetLanguage.getShort()); // language.short
                root.put("texts", originalText); // text
                root.put("folderId", folderId); // folderId

                String str = root.toString();
                byte[] outputBytes = str.getBytes(StandardCharsets.UTF_8);
                OutputStream os = connection.getOutputStream();
                os.write(outputBytes);
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }
}

