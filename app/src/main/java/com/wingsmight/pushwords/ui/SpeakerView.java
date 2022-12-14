package com.wingsmight.pushwords.ui;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wingsmight.pushwords.R;

import java.util.Locale;
import java.util.regex.Pattern;

public class SpeakerView extends FrameLayout {
    private ImageButton button;
    private TextView spokenTextView;

    private TextToSpeech textToSpeech;


    public SpeakerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView();
    }
    public SpeakerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public SpeakerView(Context context) {
        super(context);

        initView();
    }


    private void initView() {
        inflate(getContext(), R.layout.speaker, this);

        button = findViewById(R.id.button);

        textToSpeech = new TextToSpeech(getContext(), i -> { });

        button.setOnClickListener(view -> {
            if (spokenTextView == null)
                return;

            String text = spokenTextView.getText().toString();
            String textLanguage = "en";
            if (Pattern.matches(".*\\p{InCyrillic}.*", text)) {
                textLanguage = "ru";
            }

            textToSpeech.setLanguage(Locale.forLanguageTag(textLanguage));
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        });
    }


    public void setSpokenTextView(TextView spokenTextView) {
        this.spokenTextView = spokenTextView;
    }
    public ImageView getImage() {
        return button;
    }
}
