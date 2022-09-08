package com.wingsmight.pushwords.ui.learningTab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairStore;
import com.wingsmight.pushwords.data.learningTab.LearningWordPairsAdapter;

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