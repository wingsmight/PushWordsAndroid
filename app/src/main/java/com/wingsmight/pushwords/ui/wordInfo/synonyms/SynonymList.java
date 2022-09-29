package com.wingsmight.pushwords.ui.wordInfo.synonyms;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.Language;

import java.util.ArrayList;

public class SynonymList extends LinearLayoutCompat {
    private static final int MAX_PAIR_COUNT = 2;


    private View[] pairsView = new View[MAX_PAIR_COUNT];
    private TextView[] numberTexts = new TextView[MAX_PAIR_COUNT];
    private SynonymPair[] pairs = new SynonymPair[MAX_PAIR_COUNT];


    public SynonymList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }
    public SynonymList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public SynonymList(Context context) {
        super(context);
        initView();
    }


    public void set(ArrayList<ArrayList<String>> originalSynonyms, Language targetLanguage) {
        for (int i = 0; i < MAX_PAIR_COUNT; i++) {
            if (i < originalSynonyms.size()) {
                pairs[i].set(originalSynonyms.get(i), targetLanguage);
                numberTexts[i].setText(String.valueOf(i + 1));
            } else {
                pairs[i].clear();
                numberTexts[i].setText("");
            }
        }
    }
    public void clear() {
        for (int i = 0; i < MAX_PAIR_COUNT; i++) {
            pairs[i].clear();
            numberTexts[i].setText("");
        }
    }
    public void setTextColor(int color) {
        for (TextView numberText : numberTexts) {
            numberText.setTextColor(color);
        }

        for (SynonymPair pair : pairs) {
            pair.setColor(color);
        }
    }

    private void initView() {
        inflate(getContext(), R.layout.synonym_list, this);

        pairsView[0] = findViewById(R.id.row_0);
        pairsView[1] = findViewById(R.id.row_1);

        for (int i = 0; i < MAX_PAIR_COUNT; i++) {
            numberTexts[i] = pairsView[i].findViewById(R.id.number_text);
            pairs[i] = pairsView[i].findViewById(R.id.pair);
        }
    }
}
