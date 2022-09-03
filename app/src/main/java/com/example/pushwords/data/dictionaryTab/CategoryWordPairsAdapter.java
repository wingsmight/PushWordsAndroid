package com.example.pushwords.data.dictionaryTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;

import java.util.ArrayList;

public class CategoryWordPairsAdapter extends RecyclerView.Adapter<CategoryWordPairViewHolder> {
    private ArrayList<WordPair> wordPairs;


    public CategoryWordPairsAdapter(ArrayList<WordPair> wordPairs) {
        this.wordPairs = wordPairs;
    }


    @NonNull
    @Override
    public CategoryWordPairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_word_pair_row, parent, false);

        return new CategoryWordPairViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryWordPairViewHolder holder, int position) {
        holder.bind(wordPairs.get(position));
    }
    @Override
    public int getItemCount() {
        return wordPairs.size();
    }
}
