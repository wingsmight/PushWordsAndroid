package com.example.pushwords.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pushwords.R;

public class WordPairsAdapter extends RecyclerView.Adapter<WordPairViewHolder> {
    private WordPair[] wordPairs;


    public WordPairsAdapter(WordPair[] wordPairs) {
        this.wordPairs = wordPairs;
    }

    @NonNull
    @Override
    public WordPairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(context).inflate(R.layout.category_word_pair_row, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_word_pair_row, parent, false);

        return new WordPairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordPairViewHolder holder, int position) {
        holder.bind(wordPairs[position]);
    }

    @Override
    public int getItemCount() {
        return wordPairs.length;
    }
}
