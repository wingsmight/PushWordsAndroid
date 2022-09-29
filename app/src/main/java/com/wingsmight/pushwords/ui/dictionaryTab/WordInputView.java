package com.wingsmight.pushwords.ui.dictionaryTab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.core.widget.ImageViewCompat;

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

        int buttonColor = getContext().getResources().getColor(R.color.black);
        ColorStateList buttonColorStateList = ColorStateList.valueOf(buttonColor);

        ImageViewCompat.setImageTintList(clearButton.getImage(),
                buttonColorStateList);
        ImageViewCompat.setImageTintList(speakerView.getImage(),
                buttonColorStateList);
    }
}
