package com.wingsmight.pushwords.data.learnedTab.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.WordPair;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;

public class RepeatCardStackAdapter extends RecyclerView.Adapter<RepeatCardViewHolder> {
    private final ArrayList<WordPair> wordPairs;
    private final CardStackView cardStackView;
    private final CardStackLayoutManager cardStackLayoutManager;
    private final int overallCount;


    public RepeatCardStackAdapter(ArrayList<WordPair> wordPairs,
                                  CardStackView cardStackView,
                                  CardStackLayoutManager cardStackLayoutManager,
                                  int overallCount) {
        this.wordPairs = wordPairs;
        this.cardStackView = cardStackView;
        this.cardStackLayoutManager = cardStackLayoutManager;
        this.overallCount = overallCount;
    }


    @NonNull
    @Override
    public RepeatCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repeat_card, parent, false);

        return new RepeatCardViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RepeatCardViewHolder holder, int position) {
        holder.bind(wordPairs.get(position),
                cardStackView,
                cardStackLayoutManager,
                position,
                overallCount);
    }
    @Override
    public int getItemCount() {
        return wordPairs.size();
    }
}
