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
import com.wingsmight.pushwords.data.learnedTab.test.RepeatCardStackListener;
import com.wingsmight.pushwords.data.stores.WordPairStore;
import com.wingsmight.pushwords.data.learnedTab.test.RepeatCardStackAdapter;
import com.wingsmight.pushwords.extensions.WordPairListExt;
import com.wingsmight.pushwords.handlers.RandomInt;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TestView extends FrameLayout {
    private View closeButton;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackLayoutManager;
    private View finishTestView;
    private View nonTestView;
    private View testView;

    private ArrayList<WordPair> testWordPairs = new ArrayList<>();; // all words
    private int currentWordIndex = 0;
    private SharedPreferences preferences;
    private RepeatCardStackAdapter repeatCardStackAdapter;

    private ArrayList<WordPair> lastLearnedWordPairs; // 20 top learned except of forgotten words
    private ArrayList<WordPair> learnedWordPairs; // 15 learned after top except of forgotten words
    private ArrayList<WordPair> forgottenWordPairs; // all forgotten words


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


    public void startTest() {
        View parent = ((View) getParent());

        nonTestView = parent.findViewById(R.id.nonTestView);
        testView = parent.findViewById(R.id.test);

        WordPairStore wordPairStore = WordPairStore.getInstance(getContext());

        lastLearnedWordPairs = wordPairStore.getLearnedOnly();
        if (lastLearnedWordPairs.isEmpty()) {
            return;
        }

        forgottenWordPairs = wordPairStore.getForgottenOnly();

        int lastLearnedWordCount = preferences.getInt(LAST_LEARNED_WORD_COUNT_PREF_NAME,
                DEFAULT_LAST_LEARNED_WORD_COUNT);
        lastLearnedWordCount = Math.min(lastLearnedWordPairs.size(),
                lastLearnedWordCount);
        WordPairListExt.sortByChangingDate(lastLearnedWordPairs);
        lastLearnedWordPairs = new ArrayList<>(lastLearnedWordPairs
                .subList(0, lastLearnedWordCount));
        lastLearnedWordPairs.removeAll(forgottenWordPairs);

        learnedWordPairs = wordPairStore.getLearnedOnly();
        learnedWordPairs.removeAll(lastLearnedWordPairs);
        learnedWordPairs.removeAll(forgottenWordPairs);

        WordPairListExt.sortByChangingDate(learnedWordPairs);

        int learnedWordCount = preferences.getInt(LEARNED_WORD_COUNT_PREF_NAME,
                DEFAULT_LEARNED_WORD_COUNT);
        int remainedLearnedWordCount = Math.min(learnedWordCount,
                learnedWordPairs.size());
        if (remainedLearnedWordCount > 0) {
            learnedWordPairs.subList(0, remainedLearnedWordCount);
        }

        testWordPairs.clear();
        testWordPairs.addAll(lastLearnedWordPairs);
        testWordPairs.addAll(forgottenWordPairs);

        Collections.shuffle(testWordPairs);

        int[] insertIndices = new int[learnedWordPairs.size()];
        for (int i = 0; i < learnedWordPairs.size(); i++) {
            int randomIndex;
            do {
                randomIndex = RandomInt.get(0, insertIndices.length);
            } while (Arrays.asList(insertIndices).contains(randomIndex));

            insertIndices[i] = randomIndex;
        }
        Arrays.sort(insertIndices);

        for (int i = 0; i < insertIndices.length; i++) {
            WordPair word = learnedWordPairs.get(i);
            int insertIndex = insertIndices[i];
            testWordPairs.add(insertIndex + i, word);
        }

        cardStackLayoutManager = new CardStackLayoutManager(getContext(),
                new RepeatCardStackListener(getContext(), testWordPairs, cardStackView));
        cardStackLayoutManager.setCanScrollVertical(false);
        cardStackView.setLayoutManager(cardStackLayoutManager);
        repeatCardStackAdapter = new RepeatCardStackAdapter(testWordPairs,
                cardStackView,
                cardStackLayoutManager,
                testWordPairs.size());
        cardStackView.setAdapter(repeatCardStackAdapter);
        repeatCardStackAdapter.notifyDataSetChanged();

//        nonTestView.setVisibility(View.GONE);
//        testView.setVisibility(View.VISIBLE);
//        cardStackView.setVisibility(View.VISIBLE);
    }
    public void finishTest() {
//        nonTestView.setVisibility(View.VISIBLE);
//        testView.setVisibility(View.GONE);
//        cardStackView.setVisibility(View.GONE);
    }

    private void initView() {
        inflate(getContext(), R.layout.test_view, this);

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener((view) -> finishTest());

        finishTestView = findViewById(R.id.finishTestView);
        View finishButton = finishTestView
                .findViewById(R.id.continueButton);
        finishButton.setOnClickListener(view -> finishTest());

        cardStackView = findViewById(R.id.cardStack);

        preferences = getContext().getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
    }
}
