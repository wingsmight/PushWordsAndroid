package com.wingsmight.pushwords.data;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.ui.WordCardTabView;

public class WordPairViewHolder extends RecyclerView.ViewHolder {
    public static final String WORD_PAIR_EXTRA = "WORD_EXTRA";
    public static final String TARGET_LANGUAGE_EXTRA = "TARGET_LANGUAGE_EXTRA";
    private static final Language DEFAULT_TARGET_LANGUAGE = Language.Russian;


    private final TextView originalTextView;
    private final TextView translationTextView;

    private WordPair wordPair;


    public WordPairViewHolder(@NonNull View itemView) {
        super(itemView);

        originalTextView = itemView.findViewById(R.id.original);
        translationTextView = itemView.findViewById(R.id.translation);

        itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), WordCardTabView.class);
            intent.putExtra(WORD_PAIR_EXTRA, wordPair);
            intent.putExtra(TARGET_LANGUAGE_EXTRA, DEFAULT_TARGET_LANGUAGE);

            view.getContext().startActivity(intent);
        });
    }


    public void bind(WordPair wordPair) {
        this.wordPair = wordPair;

        originalTextView.setText(wordPair.getOriginal());
        translationTextView.setText(wordPair.getTranslation());
    }
}