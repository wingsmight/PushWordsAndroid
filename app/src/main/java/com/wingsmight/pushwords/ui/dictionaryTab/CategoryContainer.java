package com.wingsmight.pushwords.ui.dictionaryTab;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairStore;

import java.io.InputStream;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

public class CategoryContainer extends LinearLayoutCompat {
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

    private void initView() {
        CategoryButton[] buttons = parseCategoryButtons();
        for (CategoryButton button : buttons) {
            addView(button);
        }
    }
    private CategoryButton[] parseCategoryButtons() {
        try {
            Context context = getContext();

            WordPairStore wordPairStore = WordPairStore.getInstance(context);

            WorkbookSettings ws = new WorkbookSettings();
            ws.setGCDisabled(true);

            // TODO: TEST
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("LearningCategories.xls");
            // TODO: ^ TEST

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

                Cell[] originals = sheet.getColumn(0);
                Cell[] translations = sheet.getColumn(1);
                int wordCount = Math.min(originals.length, translations.length);
                ArrayList<WordPair> words = new ArrayList<>(wordCount);
                for (int rowIndex = 0; rowIndex < wordCount; rowIndex++) {
                    WordPair newWordPair = new WordPair(originals[rowIndex].getContents(),
                            translations[rowIndex].getContents());

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
}
