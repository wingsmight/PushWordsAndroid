package com.example.pushwords.ui.learnedTab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.data.WordPairStore;
import com.example.pushwords.data.learnedTab.LearnedWordPairsAdapter;
import com.example.pushwords.data.learningTab.LearningWordPairsAdapter;
import com.example.pushwords.databinding.LearnedTabBinding;

import java.util.ArrayList;

public class LearnedFragment extends Fragment {
    private RecyclerView recyclerView;
    private LearnedWordPairsAdapter wordPairsAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.learned_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TEST
        WordPair testWordPair = new WordPair("Dog", "Собака");
        testWordPair.setState(WordPair.State.Learned);
        WordPairStore.getInstance(view.getContext()).add(testWordPair);
        // TEST

        ArrayList<WordPair> wordPairs = WordPairStore.getInstance(getContext()).getLearnedOnly();

        recyclerView = view.findViewById(R.id.wordPairs);
        wordPairsAdapter = new LearnedWordPairsAdapter(wordPairs);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(wordPairsAdapter);
    }
}