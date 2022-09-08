package com.wingsmight.pushwords.data.learningTab;

import android.view.View;

import androidx.annotation.NonNull;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairViewHolder;
import com.wingsmight.pushwords.ui.LearnedToggle;
import com.wingsmight.pushwords.ui.PushToggle;

public class LearningWordPairViewHolder extends WordPairViewHolder {
    private final PushToggle pushToggle;
    private final LearnedToggle learnedToggle;


    public LearningWordPairViewHolder(@NonNull View itemView) {
        super(itemView);

        pushToggle = itemView.findViewById(R.id.pushToggle);
        learnedToggle = itemView.findViewById(R.id.learnedToggle);
    }


    @Override
    public void bind(WordPair wordPair) {
        super.bind(wordPair);

        pushToggle.setWordPair(wordPair);
        learnedToggle.setWordPair(wordPair);
    }
}
