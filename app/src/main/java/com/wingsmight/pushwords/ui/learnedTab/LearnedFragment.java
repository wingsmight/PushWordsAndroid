package com.wingsmight.pushwords.ui.learnedTab;

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
import com.wingsmight.pushwords.data.learnedTab.LearnedWordPairsAdapter;

import java.util.ArrayList;

public class LearnedFragment extends Fragment {
    private View emptyListView;
    private StartTestButton startTestButton;

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

        startTestButton = view.findViewById(R.id.startTestButton);
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
        startTestButton.setVisibility(wordPairs.isEmpty() ? View.GONE : View.VISIBLE);
    }
}