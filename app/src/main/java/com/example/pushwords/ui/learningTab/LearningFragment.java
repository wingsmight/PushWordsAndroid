package com.example.pushwords.ui.learningTab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.data.WordPairStore;
import com.example.pushwords.data.learningTab.LearningWordPairsAdapter;

import java.util.ArrayList;

public class LearningFragment extends Fragment {
    private RecyclerView recyclerView;
    private LearningWordPairsAdapter wordPairsAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.learning_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TEST
        WordPair testWordPair = new WordPair("Book", "Книга");
        testWordPair.setState(WordPair.State.Learning);
        WordPairStore.getInstance(view.getContext()).add(testWordPair);
        // TEST

        ArrayList<WordPair> wordPairs = WordPairStore.getInstance(getContext()).getLearningOnly();

        recyclerView = view.findViewById(R.id.wordPairs);
        wordPairsAdapter = new LearningWordPairsAdapter(wordPairs);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(wordPairsAdapter);
    }
}