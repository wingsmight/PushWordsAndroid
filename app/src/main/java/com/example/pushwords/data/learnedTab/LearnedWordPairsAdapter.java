package com.example.pushwords.data.learnedTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;

import java.util.ArrayList;

public class LearnedWordPairsAdapter extends RecyclerView.Adapter<LearnedWordPairViewHolder> {
    private ArrayList<WordPair> wordPairs;


    public LearnedWordPairsAdapter(ArrayList<WordPair> wordPairs) {
        this.wordPairs = wordPairs;
    }


    @NonNull
    @Override
    public LearnedWordPairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.learned_word_pair_row, parent, false);

        return new LearnedWordPairViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LearnedWordPairViewHolder holder, int position) {
        holder.bind(wordPairs.get(position));
    }
    @Override
    public int getItemCount() {
        return wordPairs.size();
    }
}
