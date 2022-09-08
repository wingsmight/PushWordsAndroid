package com.example.pushwords.ui.learningTab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pushwords.R;
import com.example.pushwords.data.WordPair;
import com.example.pushwords.data.WordPairStore;
import com.example.pushwords.data.learnedTab.LearnedWordPairsAdapter;
import com.example.pushwords.data.learningTab.LearningWordPairsAdapter;
import com.example.pushwords.ui.learnedTab.StartTestButton;
import com.example.pushwords.ui.learnedTab.TestView;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class LearningFragment extends Fragment {
    private View emptyListView;
    private RecyclerView recyclerView;
    private LearningWordPairsAdapter wordPairsAdapter;
    private WordPairStore wordPairStore;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.learning_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        wordPairStore = WordPairStore.getInstance(getContext());

        recyclerView = view.findViewById(R.id.wordPairs);
        emptyListView = view.findViewById(R.id.emptyListView);

        wordPairStore.setOnStateChanged(wordPair -> onResume());
        wordPairStore.setOnPushedChanged(wordPair -> onResume());
    }
    @Override
    public void onResume() {
        super.onResume();

        ArrayList<WordPair> learnedWordPairs = wordPairStore.getLearningOnly();
        Collections.sort(learnedWordPairs, (lhs, rhs) ->
                lhs.getChangingDate().compareTo(rhs.getChangingDate()));
        Collections.sort(learnedWordPairs, (lhs, rhs) ->
                lhs.isPushed() && !rhs.isPushed()
                        ? -1
                        : 0);

        wordPairsAdapter = new LearningWordPairsAdapter(learnedWordPairs);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(wordPairsAdapter);

        emptyListView.setVisibility(learnedWordPairs.isEmpty() ? View.VISIBLE : View.GONE);
    }
}