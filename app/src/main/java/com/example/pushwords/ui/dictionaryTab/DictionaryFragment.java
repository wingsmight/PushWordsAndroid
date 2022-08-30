package com.example.pushwords.ui.dictionaryTab;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;

import com.example.pushwords.R;
import com.example.pushwords.data.Language;
import com.example.pushwords.handlers.network.TranslationApi;
import com.example.pushwords.handlers.network.dictionaryApis.examples.RussianExamplesApi;
import com.example.pushwords.ui.WordInfoView;

public class DictionaryFragment extends Fragment {
    private Language currentOriginalLanguage = Language.English;
    private Language currentTargetLanguage = Language.Russian;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dictionary_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // category view
        View categoryView = view.findViewById(R.id.category_view);

        // word info view
        View wordInfoView = view.findViewById(R.id.word_info_view);

        // word info
        WordInfoView wordInfo = view.findViewById(R.id.word_info);

        // test
        RussianExamplesApi russianExamplesApi = new RussianExamplesApi();
        russianExamplesApi.loadExamples(getContext(),"бегать");

        // language switch
        View languageSwitch = view.findViewById(R.id.language_switch);
        ImageView languageSwitchButton = languageSwitch.findViewById(R.id.switch_language_button);
        TextView originalLanguageText = languageSwitch.findViewById(R.id.original_language);
        TextView targetLanguageText = languageSwitch.findViewById(R.id.target_language);

        languageSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOriginalLanguage = currentOriginalLanguage.getOpposite();
                currentTargetLanguage = currentTargetLanguage.getOpposite();

                originalLanguageText.setText(currentOriginalLanguage.getDescription());
                targetLanguageText.setText(currentTargetLanguage.getDescription());

                if (currentOriginalLanguage != Language.English) {
                    view.animate().rotation(0).rotation(180).start();
                } else {
                    view.animate().rotation(180).rotation(0).start();
                }
            }
        });

        // word input
        View wordInput = view.findViewById(R.id.wordInput);
        EditText wordInputText = wordInput.findViewById(R.id.wordInputText);

        Consumer<String> onWordTranslated = wordInfo::setWord;

        wordInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                new TranslationApi().translate(charSequence.toString(), currentTargetLanguage, onWordTranslated);

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