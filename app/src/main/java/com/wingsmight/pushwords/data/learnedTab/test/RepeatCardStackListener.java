package com.wingsmight.pushwords.data.learnedTab.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.core.util.Consumer;

import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.ui.learnedTab.TestSettingsTab;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;

public class RepeatCardStackListener implements CardStackListener {
    private int currentWordPairIndex = 0;
    private ArrayList<WordPair> testWordPairs;
    private SharedPreferences preferences;
    private CardStackView cardStackView;


    public RepeatCardStackListener(Context context,
                                   ArrayList<WordPair> testWordPairs,
                                   CardStackView cardStackView) {
        this.testWordPairs = testWordPairs;
        this.cardStackView = cardStackView;

        preferences = context.getSharedPreferences(Preference.SHARED, Context.MODE_PRIVATE);
    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {  }
    @Override
    public void onCardSwiped(Direction direction) {
        if (direction == Direction.Left) {
            forgot();
        } else if (direction == Direction.Right) {
            remember();
        }
    }
    @Override
    public void onCardRewound() { }
    @Override
    public void onCardCanceled() { }
    @Override
    public void onCardAppeared(View view, int position) {
        currentWordPairIndex = position;
    }
    @Override
    public void onCardDisappeared(View view, int position) { }

    private void forgot() {
        getCurrentWordPair().forgot();

        pass();
    }
    private void remember() {
        int requiredRememberingCount = preferences
                .getInt(TestSettingsTab.FORGOT_REPEAT_COUNT_PREF_NAME,
                        TestSettingsTab.DEFAULT_FORGOT_REPEAT_COUNT);

        getCurrentWordPair().remember(requiredRememberingCount);

        pass();
    }
    private void pass() {
        if (currentWordPairIndex >= testWordPairs.size() - 1) {
            cardStackView.setVisibility(View.GONE);
        }
    }


    private WordPair getCurrentWordPair() {
        return testWordPairs.get(currentWordPairIndex);
    }
}
