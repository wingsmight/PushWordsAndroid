package com.wingsmight.pushwords.ui.dictionaryTab;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.wingsmight.pushwords.data.Language;
import com.wingsmight.pushwords.data.Word;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.stores.WordPairStore;
import com.wingsmight.pushwords.handlers.InternalStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class CategoryContainer extends LinearLayoutCompat {
    private ArrayList<CategoryButton> buttons = new ArrayList<>();


    public CategoryContainer(@NonNull Context context) {
        super(context);

        initView();
    }

    public CategoryContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    public CategoryContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }


    public void addButton(CategoryButton newButton) {
        CategoryButton existedButton = null;
        for (CategoryButton button : buttons) {
            if (button.getLabel().getTitleText()
                    .equals(newButton.getLabel().getTitleText())) {
                existedButton = button;
                break;
            }
        }

        if (existedButton != null) {
            removeView(existedButton);
        }

        buttons.add(newButton);
        addView(newButton);
    }

    public CategoryButton[] parseCategoryButtons(File file) {
        try {
            return parseCategoryButtons(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return new CategoryButton[0];
        }
    }

    public CategoryButton[] parseCategoryButtons(InputStream inputStream) {
        try {
            Context context = getContext();

            WordPairStore wordPairStore = WordPairStore.getInstance(context);

            WorkbookSettings ws = new WorkbookSettings();
            ws.setGCDisabled(true);

            Workbook workbook = Workbook.getWorkbook(inputStream, ws);
            int count = workbook.getNumberOfSheets();
            CategoryButton[] buttons = new CategoryButton[count];
            for (int sheetIndex = 0; sheetIndex < count; sheetIndex++) {
                Sheet sheet = workbook.getSheet(sheetIndex);

                CategoryButton.Label viewLabel = new CategoryButton.Label(
                        sheet.getCell(3, 1).getContents(),
                        sheet.getCell(4, 1).getContents(),
                        sheet.getCell(5, 1).getContents(),
                        sheet.getCell(6, 1).getContents());

                Language originalLanguage = Language
                        .valueOf(sheet.getCell(0, 0).getContents());
                Cell[] originals = sheet.getColumn(0);
                Cell[] translations = sheet.getColumn(1);
                int wordCount = Math.min(originals.length, translations.length);
                ArrayList<WordPair> words = new ArrayList<>(wordCount);
                for (int rowIndex = 1; rowIndex < wordCount; rowIndex++) {
                    WordPair newWordPair = new WordPair(new Word(originals[rowIndex].getContents(), originalLanguage),
                            new Word(translations[rowIndex].getContents(), originalLanguage.getOpposite()));

                    words.add(newWordPair);
                    wordPairStore.add(newWordPair);
                }

                buttons[sheetIndex] = new CategoryButton(context, viewLabel, words);
            }

            return buttons;
        } catch (Exception e) {
            return new CategoryButton[0];
        }
    }

    private void initView() {
        File[] categoryFiles = InternalStorage
                .getFiles(getContext(), InternalStorage.LEARNING_CATEGORIES_DIRECTORY);

        for (File file : categoryFiles) {
            CategoryButton[] buttons = parseCategoryButtons(file);
            for (CategoryButton button : buttons) {
                addButton(button);
            }
        }
    }

    public CategoryButton[] parseCategoryButtonsFromAssets() {
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("LearningCategories.xls");

            return parseCategoryButtons(inputStream);
        } catch (IOException e) {
            e.printStackTrace();

            return new CategoryButton[0];
        }
    }
}
