package com.wingsmight.pushwords.data.learnedTab.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Language;
import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.Word;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.handlers.PhoneticTranscription;
import com.wingsmight.pushwords.handlers.RandomBoolean;
import com.wingsmight.pushwords.ui.SpeakerView;
import com.wingsmight.pushwords.ui.learnedTab.TestSettingsTab;
import com.wingsmight.pushwords.ui.wordInfo.WordInfoView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

public class RepeatCardViewHolder extends RecyclerView.ViewHolder {
    private final TextView counterTextView;
    private final TextView originalWordTextView;
    private final TextView originalWordTextTranscriptionView;
    private final SpeakerView speakerView;
    private final WordInfoView wordInfoView;
    private final View forgotButton;
    private final View rememberButton;
    private final ToggleButton watchTranslationToggle;
    private final TextView counterText;

    private WordPair wordPair;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;
    private final SharedPreferences preferences;
    private boolean isOriginalWordShowing;
    private PhoneticTranscription phoneticTranscription;

    public RepeatCardViewHolder(@NonNull View itemView) {
        super(itemView);

        int elementColor = itemView.getResources().getColor(R.color.black);
        ColorStateList elementColorStateList = ColorStateList.valueOf(elementColor);

        counterTextView = itemView.findViewById(R.id.counterText);

        originalWordTextView = itemView.findViewById(R.id.originalWordText);
        originalWordTextTranscriptionView = itemView.findViewById(R.id.originalWordTextTranscription);

        speakerView = itemView.findViewById(R.id.speakerButton);

        wordInfoView = itemView.findViewById(R.id.wordInfo);
        wordInfoView.setElementColor(elementColor);

        forgotButton = itemView.findViewById(R.id.forgotButton);
        forgotButton.setOnClickListener((view) -> forgot());

        rememberButton = itemView.findViewById(R.id.rememberButton);
        rememberButton.setOnClickListener((view) -> remember());

        watchTranslationToggle = itemView.findViewById(R.id.watchTranslationToggle);
        watchTranslationToggle.setOnClickListener(view -> {
            if (watchTranslationToggle.isChecked()) {
                hideTranslation();
            } else {
                showTranslation();
            }
        });

        counterText = itemView.findViewById(R.id.counterText);

        preferences = itemView.getContext()
                .getSharedPreferences(Preference.SHARED, Context.MODE_PRIVATE);

        ImageViewCompat.setImageTintList(speakerView.getImage(),
                elementColorStateList);

        phoneticTranscription = PhoneticTranscription.getInstance(itemView.getContext());
    }


    public void bind(WordPair wordPair,
                     CardStackView cardStackView,
                     CardStackLayoutManager cardStackLayoutManager,
                     int position,
                     int overallCount) {
        this.wordPair = wordPair;
        this.cardStackView = cardStackView;
        this.cardStackLayoutManager = cardStackLayoutManager;

        isOriginalWordShowing = RandomBoolean.get();

        Word originalWord;
        Word translationWord;
        if (getIsOriginalWordShowing()) {
            originalWord = wordPair.getOriginal();
            translationWord = wordPair.getTranslation();
        } else {
            originalWord = wordPair.getTranslation();
            translationWord = wordPair.getOriginal();
        }

        originalWordTextView.setText(originalWord.getText());
        speakerView.setSpokenTextView(originalWordTextView);
        wordInfoView.setWord(translationWord, wordPair.getOriginal(), !isOriginalWordShowing);
        counterText.setText((position + 1) + " из " + overallCount);

        if (isOriginalWordShowing) {
            originalWordTextTranscriptionView.setText(phoneticTranscription.get(originalWord.getText()));
            originalWordTextTranscriptionView.setVisibility(View.VISIBLE);
        } else {
            originalWordTextTranscriptionView.setVisibility(View.GONE);
        }
    }

    private void forgot() {
        swipeOut(Direction.Left);
    }

    private void remember() {
        swipeOut(Direction.Right);
    }

    private void swipeOut(Direction direction) {
        SwipeAnimationSetting settings = new SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        cardStackLayoutManager.setSwipeAnimationSetting(settings);
        cardStackView.swipe();
    }

    private void hideTranslation() {
        wordInfoView.setVisibility(View.INVISIBLE);
    }

    private void showTranslation() {
        wordInfoView.setVisibility(View.VISIBLE);
    }


    private boolean getIsOriginalWordShowing() {
        boolean isReversedTranslation = preferences
                .getBoolean(TestSettingsTab.REVERSE_TRANSLATION_PREF_NAME,
                        TestSettingsTab.DEFAULT_REVERSE_TRANSLATION);

        if (isReversedTranslation) {
            return isOriginalWordShowing;
        } else {
            return true;
        }
    }
}
