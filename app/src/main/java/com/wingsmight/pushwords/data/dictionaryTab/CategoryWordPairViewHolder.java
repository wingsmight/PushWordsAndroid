package com.wingsmight.pushwords.data.dictionaryTab;

import android.view.View;

import androidx.annotation.NonNull;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairViewHolder;
import com.wingsmight.pushwords.ui.LearnedToggle;
import com.wingsmight.pushwords.ui.LearningToggle;

public class CategoryWordPairViewHolder extends WordPairViewHolder {
    private final LearningToggle learningToggle;
    private final LearnedToggle learnedToggle;


    public CategoryWordPairViewHolder(@NonNull View itemView) {
        super(itemView);

        learningToggle = itemView.findViewById(R.id.learningToggle);
        learnedToggle = itemView.findViewById(R.id.learnedToggle);
    }


    @Override
    public void bind(WordPair wordPair) {
        super.bind(wordPair);

        learningToggle.setWordPair(wordPair);
        learnedToggle.setWordPair(wordPair);
    }
}
