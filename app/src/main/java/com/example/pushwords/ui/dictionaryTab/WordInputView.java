package com.example.pushwords.ui.dictionaryTab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.pushwords.R;
import com.example.pushwords.handlers.network.TranslationApi;
import com.example.pushwords.ui.SpeakerView;

public class WordInputView extends FrameLayout {
    private EditText wordInputText;
    private ClearInputButton clearButton;
    private SpeakerView speakerView;


    public WordInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }
    public WordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public WordInputView(Context context) {
        super(context);
        initView();
    }


    private void initView() {
        inflate(getContext(), R.layout.word_input, this);

        wordInputText = findViewById(R.id.wordInputText);
        clearButton = findViewById(R.id.clearButton);
        speakerView = findViewById(R.id.speakerButton);

        clearButton.setTextToClear(wordInputText);
        speakerView.setSpokenTextView(wordInputText);
    }
}
