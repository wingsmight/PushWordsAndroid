package com.wingsmight.pushwords.ui.dictionaryTab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.ui.SpeakerView;

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
