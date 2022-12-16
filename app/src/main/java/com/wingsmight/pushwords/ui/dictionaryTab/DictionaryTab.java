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
import com.wingsmight.pushwords.data.Language;
import com.wingsmight.pushwords.data.User;
import com.wingsmight.pushwords.data.Word;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.database.CloudDatabase;
import com.wingsmight.pushwords.handlers.network.TranslationApi;
import com.wingsmight.pushwords.ui.WordControlPanel;
import com.wingsmight.pushwords.ui.wordInfo.WordInfoView;

public class DictionaryTab extends Fragment {
    private WordInfoView translatedWordInfo;
    private WordControlPanel translatedWordControlPanel;
    private LanguageSwitch languageSwitch;
    private CategoryContainer categoryView;
    private View translatedWordInfoView;

    private TranslationApi translationApi = new TranslationApi();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dictionary_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // category view
        categoryView = view.findViewById(R.id.categoryView);

        // word info view
        translatedWordInfoView = view.findViewById(R.id.translatedWordInfoView);

        // word info
        translatedWordInfo = view.findViewById(R.id.wordInfo);

        // translated word panel
        translatedWordControlPanel = view.findViewById(R.id.translatedWordControlPanel);
        translatedWordControlPanel.setWordTextView(translatedWordInfo.findViewById(R.id.word));

        // word input
        View wordInput = view.findViewById(R.id.wordInput);
        EditText wordInputText = wordInput.findViewById(R.id.wordInputText);

        // language switch
        languageSwitch = view.findViewById(R.id.languageSwitch);
        languageSwitch.setOnSwitchListener((originalLanguage, targetLanguage) ->
                translate(new Word(wordInputText.getText().toString(), originalLanguage),
                        targetLanguage));

        wordInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                translate(new Word(charSequence.toString(), languageSwitch.getCurrentOriginalLanguage()),
                        languageSwitch.getCurrentTargetLanguage());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

//        File existedFile = InternalStorage.readFile(getContext(), "LearningCategories.xls");
//        if (existedFile != null) {
//            CategoryButton[] buttons = categoryView.parseCategoryButtons(existedFile);
//
//            for (CategoryButton button : buttons) {
//                categoryView.addButton(button);
//            }
//        }

        CloudDatabase.loadLearningCategories(getContext(), file -> {
            CategoryButton[] buttons = categoryView.parseCategoryButtons(file);

            for (CategoryButton button : buttons) {
                categoryView.addButton(button);
            }
        });
    }

    private void translate(Word inputWord, Language targetLanguage) {
        if (inputWord.getText().isEmpty()) {
            categoryView.setVisibility(View.VISIBLE);
            translatedWordInfoView.setVisibility(View.GONE);
        } else {
            Consumer<Word> onWordTranslated = translation -> getActivity().
                    runOnUiThread(() -> {
                        WordPair wordPair = new WordPair(inputWord,
                                translation,
                                User.PrimaryLanguage,
                                User.LearningLanguage);

                        translatedWordControlPanel.setWordPair(wordPair);
                        translatedWordInfo.setWord(translation, wordPair.getOriginal());
                    });

            Runnable onTranslationFailure = () -> getActivity().
                    runOnUiThread(() -> translatedWordInfo.clear());

            translationApi.translate(inputWord.getText(),
                    targetLanguage,
                    onWordTranslated,
                    onTranslationFailure);

            categoryView.setVisibility(View.GONE);
            translatedWordInfoView.setVisibility(View.VISIBLE);
        }

        getActivity().
                runOnUiThread(() -> translatedWordInfo.clear());
    }
}