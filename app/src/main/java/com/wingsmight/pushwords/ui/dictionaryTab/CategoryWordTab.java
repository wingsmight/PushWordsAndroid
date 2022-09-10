package com.wingsmight.pushwords.ui.dictionaryTab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.UserStore;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairStore;
import com.wingsmight.pushwords.data.dictionaryTab.CategoryWordPairsAdapter;

import java.util.ArrayList;

public class CategoryWordTab extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryWordPairsAdapter wordPairsAdapter;

    private String actionBarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_words_tab);

        WordPairStore wordPairStore = WordPairStore.getInstance(this);
        Intent intent = getIntent();
        ArrayList<WordPair> categoryWordPairs = intent
                .getParcelableArrayListExtra(CategoryButton.WORD_PAIRS_EXTRA);
        actionBarTitle = intent.getStringExtra(CategoryButton.TAB_LABEL_EXTRA);

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
        UserStore.getInstance(this).save();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().setTitle(actionBarTitle);
    }
}