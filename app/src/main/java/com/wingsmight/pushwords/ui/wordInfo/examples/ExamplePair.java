package com.wingsmight.pushwords.ui.wordInfo.examples;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Language;
import com.wingsmight.pushwords.handlers.network.TranslationApi;

public class ExamplePair extends LinearLayoutCompat {
    private TextView originalText;
    private TextView translatedText;

    private TranslationApi translationApi = new TranslationApi();


    public ExamplePair(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public ExamplePair(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ExamplePair(Context context) {
        super(context);
        initView();
    }


    public void set(String originalExample, Language targetLanguage) {
        Activity activity = ((Activity) getContext());
        activity.runOnUiThread(() -> originalText.setText(originalExample));

        translationApi.translate(originalExample,
                targetLanguage.getOpposite(),
                translatedExample -> activity.runOnUiThread(() -> translatedText.setText(translatedExample.getText())),
                () -> activity.runOnUiThread(() -> translatedText.setText("")));
    }

    public void clear() {
        originalText.setText("");
        translatedText.setText("");
    }

    public void setColor(int color) {
        originalText.setTextColor(color);
        translatedText.setTextColor(color);
    }

    private void initView() {
        inflate(getContext(), R.layout.example_pair, this);

        originalText = findViewById(R.id.original);
        translatedText = findViewById(R.id.translation);
    }
}
