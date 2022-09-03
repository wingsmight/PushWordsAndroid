package com.example.pushwords.data.learningTab;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.data.WordPairViewHolder;
import com.example.pushwords.ui.LearnedToggle;
import com.example.pushwords.ui.PushToggle;

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
