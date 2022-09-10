package com.wingsmight.pushwords.ui.dictionaryTab;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;

import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.handlers.network.TranslationApi;
import com.wingsmight.pushwords.ui.WordControlPanel;
import com.wingsmight.pushwords.ui.wordInfo.WordInfoView;

public class DictionaryTab extends Fragment {
    private WordInfoView wordInfo;
    private WordControlPanel translatedWordControlPanel;
    private LanguageSwitch languageSwitch;

    private String inputOriginal;
    private TranslationApi translationApi = new TranslationApi();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dictionary_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // category view
        View categoryView = view.findViewById(R.id.categoryView);

        // word info view
        View wordInfoView = view.findViewById(R.id.translatedWordInfoView);

        // word info
        wordInfo = view.findViewById(R.id.wordInfo);

        // translated word panel
        translatedWordControlPanel = view.findViewById(R.id.translatedWordControlPanel);
        translatedWordControlPanel.setWordTextView(wordInfo.findViewById(R.id.word));

        // language switch
        languageSwitch = view.findViewById(R.id.languageSwitch);
        languageSwitch.setOnSwitchListener((originalLanguage, targetLanguage) ->
                wordInfo.setTargetLanguage(targetLanguage));

        // word input
        View wordInput = view.findViewById(R.id.wordInput);
        EditText wordInputText = wordInput.findViewById(R.id.wordInputText);

        Consumer<String> onWordTranslated = translation -> getActivity().
                runOnUiThread(() -> {
                    wordInfo.setWord(translation);

                    WordPair wordPair = new WordPair(inputOriginal, translation);
                    translatedWordControlPanel.setWordPair(wordPair);
                });

        wordInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputOriginal = charSequence.toString();
                wordInfo.setWord("");

                translationApi.translate(inputOriginal,
                        languageSwitch.getCurrentTargetLanguage(),
                        onWordTranslated);

                if (charSequence.toString().isEmpty()) {
                    categoryView.setVisibility(View.VISIBLE);
                    wordInfoView.setVisibility(View.GONE);
                } else {
                    categoryView.setVisibility(View.GONE);
                    wordInfoView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
}