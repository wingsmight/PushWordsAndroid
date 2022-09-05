package com.example.pushwords.data.learnedTab;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.ui.SpeakerView;
import com.example.pushwords.ui.wordInfo.WordInfoView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

public class RepeatCardViewHolder extends RecyclerView.ViewHolder {
    private final TextView counterTextView;
    private final TextView originalWordTextView;
    private final SpeakerView speakerView;
    private final WordInfoView wordInfoView;
    private final View forgotButton;
    private final View rememberButton;
    private final ToggleButton watchTranslationToggle;
    private final TextView counterText;

    private WordPair wordPair;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;


    public RepeatCardViewHolder(@NonNull View itemView) {
        super(itemView);

        counterTextView = itemView.findViewById(R.id.counterText);

        originalWordTextView = itemView.findViewById(R.id.originalWordText);

        speakerView = itemView.findViewById(R.id.speakerButton);

        wordInfoView = itemView.findViewById(R.id.wordInfo);

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

        // Test
        wordInfoView.setVisibility(View.VISIBLE);
    }


    public void bind(WordPair wordPair,
                     CardStackView cardStackView,
                     CardStackLayoutManager cardStackLayoutManager,
                     int position,
                     int overallCount) {
        this.wordPair = wordPair;
        this.cardStackView = cardStackView;
        this.cardStackLayoutManager = cardStackLayoutManager;

        originalWordTextView.setText(wordPair.getOriginal());
        speakerView.setSpokenTextView(originalWordTextView);
        wordInfoView.setWord(wordPair.getTranslation());
        counterText.setText((position + 1) + " из " + overallCount);
    }
    private void forgot() {
        SwipeAnimationSetting settings = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
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
    private void remember() {
        SwipeAnimationSetting settings = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        cardStackLayoutManager.setSwipeAnimationSetting(settings);
        cardStackView.swipe();
    }
}
