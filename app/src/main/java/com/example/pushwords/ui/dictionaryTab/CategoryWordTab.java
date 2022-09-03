package com.example.pushwords.ui.dictionaryTab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.data.dictionaryTab.CategoryWordPairsAdapter;

import java.util.ArrayList;

public class CategoryWordTab extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryWordPairsAdapter wordPairsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_words_tab);

        ArrayList<WordPair> words = getIntent().getParcelableArrayListExtra(CategoryButton.WORD_PAIRS_EXTRA);

        recyclerView = findViewById(R.id.wordPairs);
        wordPairsAdapter = new CategoryWordPairsAdapter(words);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wordPairsAdapter);
    }
}