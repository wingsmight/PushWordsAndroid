package com.example.pushwords.ui.learnedTab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.data.WordPairStore;
import com.example.pushwords.data.learnedTab.RepeatCardStackAdapter;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;
import java.util.Collections;

public class TestView extends FrameLayout {
    // TODO: bind with prefs
    private static final int LAST_LEARNED_WORD_COUNT = 20;
    private static final int LEARNED_WORD_COUNT = 15;


    private View closeButton;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;

    private ArrayList<WordPair> testWordPairs; // all words
    private int currentWordIndex = 0;

    private ArrayList<WordPair> lastLearnedWordPairs; // 20 top learned except of forgotten words
    private ArrayList<WordPair> learnedWordPairs; // 15 learned after top except of forgotten words
    private ArrayList<WordPair> forgottenWordPairs; // all forgotten words
    private ArrayList<WordPair> appWordPairs; // all words in app
    private ArrayList<WordPair> passedWordPairs = new ArrayList<>(); // passed in current session words


    public TestView(@NonNull Context context) {
        super(context);

        initView();
    }
    public TestView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public TestView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }


    public void start() {
        WordPairStore wordPairStore = WordPairStore.getInstance(getContext());

        appWordPairs = wordPairStore.getAll();

        lastLearnedWordPairs = wordPairStore.getLearnedOnly();
        int lastLearnedWordCount = Math.min(lastLearnedWordPairs.size(),
                LAST_LEARNED_WORD_COUNT);
        Collections.sort(lastLearnedWordPairs, (lhs, rhs) ->
                lhs.getChangingDate().getTime() > rhs.getChangingDate().getTime()
                        ? -1
                        : 0);
        lastLearnedWordPairs = new ArrayList<>(lastLearnedWordPairs
                .subList(0, lastLearnedWordCount));

        learnedWordPairs = wordPairStore.getLearnedOnly();
        int remainedLearnedWordCount = Math.min(LEARNED_WORD_COUNT,
                learnedWordPairs.size() - lastLearnedWordPairs.size());
        Collections.sort(learnedWordPairs, (lhs, rhs) ->
                lhs.getChangingDate().getTime() > rhs.getChangingDate().getTime()
                        ? -1
                        : 0);
        if (remainedLearnedWordCount >= lastLearnedWordCount - 1) {
            learnedWordPairs = new ArrayList<>(learnedWordPairs
                    .subList(lastLearnedWordCount - 1,
                            remainedLearnedWordCount));
        } else {
            learnedWordPairs = new ArrayList<>();
        }

        forgottenWordPairs = wordPairStore.getForgottenOnly();

        testWordPairs = new ArrayList<>();
        testWordPairs.addAll(lastLearnedWordPairs);
        testWordPairs.addAll(learnedWordPairs);
        testWordPairs.addAll(forgottenWordPairs);

        Collections.shuffle(testWordPairs);

        cardStackView.setAdapter(new RepeatCardStackAdapter(testWordPairs,
                cardStackView,
                cardStackLayoutManager,
                testWordPairs.size()));
    }
    private void initView() {
        inflate(getContext(), R.layout.test_view, this);

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener((view) -> finishTest());

        View finishButton = findViewById(R.id.finishTestView)
                .findViewById(R.id.continueButton);
        finishButton.setOnClickListener(view -> finishTest());

        cardStackView = findViewById(R.id.cardStack);
        cardStackLayoutManager = new CardStackLayoutManager(getContext());
        cardStackLayoutManager.setCanScrollVertical(false);
        cardStackView.setLayoutManager(cardStackLayoutManager);
    }
    private void finishTest() {
        View parent = ((View) getParent());

        View nonTestView = parent.findViewById(R.id.nonTestView);
        View testView = parent.findViewById(R.id.test);

        nonTestView.setVisibility(View.VISIBLE);
        testView.setVisibility(View.GONE);
    }
}
