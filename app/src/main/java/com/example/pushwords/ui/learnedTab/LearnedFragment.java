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

import java.util.ArrayList;

public class LearnedFragment extends Fragment {
    private View emptyListView;
    private RecyclerView recyclerView;
    private LearnedWordPairsAdapter wordPairsAdapter;
    private WordPairStore wordPairStore;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.learned_tab, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        wordPairStore = WordPairStore.getInstance(getContext());

        recyclerView = view.findViewById(R.id.wordPairs);

        TestView testView = view.findViewById(R.id.test);
        View nonTestView = view.findViewById(R.id.nonTestView);

        StartTestButton startTestButton = view.findViewById(R.id.startTestButton);
        startTestButton.setTest(testView);
        startTestButton.setNonTestView(nonTestView);

        emptyListView = view.findViewById(R.id.emptyListView);
    }
    @Override
    public void onResume() {
        super.onResume();

        ArrayList<WordPair> wordPairs = wordPairStore.getLearnedOnly();

        wordPairsAdapter = new LearnedWordPairsAdapter(wordPairs);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(wordPairsAdapter);

        emptyListView.setVisibility(wordPairs.isEmpty() ? View.VISIBLE : View.GONE);
    }
}