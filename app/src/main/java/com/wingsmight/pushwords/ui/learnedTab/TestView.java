package com.wingsmight.pushwords.ui.learnedTab;

import static android.content.Context.MODE_PRIVATE;

import static com.wingsmight.pushwords.ui.learnedTab.TestSettingsTab.DEFAULT_LAST_LEARNED_WORD_COUNT;
import static com.wingsmight.pushwords.ui.learnedTab.TestSettingsTab.DEFAULT_LEARNED_WORD_COUNT;
import static com.wingsmight.pushwords.ui.learnedTab.TestSettingsTab.LAST_LEARNED_WORD_COUNT_PREF_NAME;
import static com.wingsmight.pushwords.ui.learnedTab.TestSettingsTab.LEARNED_WORD_COUNT_PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairStore;
import com.wingsmight.pushwords.data.learnedTab.RepeatCardStackAdapter;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;
import java.util.Collections;

public class TestView extends FrameLayout {
    private View closeButton;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;

    private ArrayList<WordPair> testWordPairs; // all words
    private int currentWordIndex = 0;
    private SharedPreferences preferences;

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
        if (lastLearnedWordPairs.isEmpty()) {
            return;
        }

        int lastLearnedWordCount = preferences.getInt(LAST_LEARNED_WORD_COUNT_PREF_NAME,
                DEFAULT_LAST_LEARNED_WORD_COUNT);
        lastLearnedWordCount = Math.min(lastLearnedWordPairs.size(),
                lastLearnedWordCount);
        Collections.sort(lastLearnedWordPairs, (lhs, rhs) ->
                rhs.getChangingDate().compareTo(lhs.getChangingDate()));
        lastLearnedWordPairs = new ArrayList<>(lastLearnedWordPairs
                .subList(0, lastLearnedWordCount));

        learnedWordPairs = wordPairStore.getLearnedOnly();
        int learnedWordCount = preferences.getInt(LEARNED_WORD_COUNT_PREF_NAME,
                DEFAULT_LEARNED_WORD_COUNT);
        int remainedLearnedWordCount = Math.min(learnedWordCount,
                learnedWordPairs.size() - lastLearnedWordPairs.size());
        Collections.sort(learnedWordPairs, (lhs, rhs) ->
                rhs.getChangingDate().compareTo(lhs.getChangingDate()));
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

        preferences = getContext().getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
    }
    private void finishTest() {
        View parent = ((View) getParent());

        View nonTestView = parent.findViewById(R.id.nonTestView);
        View testView = parent.findViewById(R.id.test);

        nonTestView.setVisibility(View.VISIBLE);
        testView.setVisibility(View.GONE);
    }
}
