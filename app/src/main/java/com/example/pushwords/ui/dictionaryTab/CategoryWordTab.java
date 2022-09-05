package com.example.pushwords.ui.dictionaryTab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.data.WordPairStore;
import com.example.pushwords.data.dictionaryTab.CategoryWordPairsAdapter;

import java.util.ArrayList;

public class CategoryWordTab extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryWordPairsAdapter wordPairsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_words_tab);

        WordPairStore wordPairStore = WordPairStore.getInstance(this);
        ArrayList<WordPair> categoryWordPairs = getIntent()
                .getParcelableArrayListExtra(CategoryButton.WORD_PAIRS_EXTRA);

        ArrayList<WordPair> words = new ArrayList<>(categoryWordPairs.size());
        for (WordPair wordPair : categoryWordPairs) {
            words.add(wordPairStore.get(wordPair));
        }

        recyclerView = findViewById(R.id.wordPairs);
        wordPairsAdapter = new CategoryWordPairsAdapter(words);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wordPairsAdapter);
    }
    @Override
    protected void onPause() {
        super.onPause();

        WordPairStore.getInstance(this).save();
    }
}