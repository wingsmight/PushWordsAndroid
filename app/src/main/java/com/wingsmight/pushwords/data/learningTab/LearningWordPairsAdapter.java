package com.wingsmight.pushwords.data.learningTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.WordPair;

import java.util.ArrayList;

public class LearningWordPairsAdapter extends RecyclerView.Adapter<LearningWordPairViewHolder> {
    private ArrayList<WordPair> wordPairs;


    public LearningWordPairsAdapter(ArrayList<WordPair> wordPairs) {
        this.wordPairs = wordPairs;
    }


    @NonNull
    @Override
    public LearningWordPairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.learning_word_pair_row, parent, false);

        return new LearningWordPairViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LearningWordPairViewHolder holder, int position) {
        holder.bind(wordPairs.get(position));
    }
    @Override
    public int getItemCount() {
        return wordPairs.size();
    }
}
