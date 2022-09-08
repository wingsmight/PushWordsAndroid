package com.wingsmight.pushwords.ui.dictionaryTab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Language;

public class LanguageSwitch extends FrameLayout {
    private ImageView switchButton;
    private TextView originalLanguageText;
    private TextView targetLanguageText;

    private Language currentOriginalLanguage = Language.English;
    private Language currentTargetLanguage = Language.Russian;

    private OnSwitchListener onSwitchListener;


    public LanguageSwitch(@NonNull Context context) {
        super(context);

        initView();
    }
    public LanguageSwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public LanguageSwitch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }
    public LanguageSwitch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView();
    }


    public Language getCurrentOriginalLanguage() {
        return currentOriginalLanguage;
    }
    public Language getCurrentTargetLanguage() {
        return currentTargetLanguage;
    }
    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }
    private void initView() {
        inflate(getContext(), R.layout.language_switch, this);

        switchButton = findViewById(R.id.switchButton);
        originalLanguageText = findViewById(R.id.originalLanguage);
        targetLanguageText = findViewById(R.id.targetLanguage);

        switchButton.setOnClickListener(button -> {
            currentOriginalLanguage = currentOriginalLanguage.getOpposite();
            currentTargetLanguage = currentTargetLanguage.getOpposite();

            originalLanguageText.setText(currentOriginalLanguage.getDescription());
            targetLanguageText.setText(currentTargetLanguage.getDescription());

            if (currentOriginalLanguage != Language.English) {
                button.animate().rotation(0).rotation(180).start();
            } else {
                button.animate().rotation(180).rotation(0).start();
            }

            if (onSwitchListener != null) {
                onSwitchListener.onSwitch(currentOriginalLanguage, currentTargetLanguage);
            }
        });
    }


    public interface OnSwitchListener {
        void onSwitch(Language originalLanguage, Language targetLanguage);
    }
}
