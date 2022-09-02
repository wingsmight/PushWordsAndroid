package com.example.pushwords.ui.dictionaryTab;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.io.InputStream;

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
        Context context = getContext();

        CategoryButton.Label[] labels = parseButtonLabels();
        for (CategoryButton.Label label : labels) {
            addView(new CategoryButton(context, label));
        }
    }
    private CategoryButton.Label[] parseButtonLabels() {
        CategoryButton view = new CategoryButton(getContext());

        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setGCDisabled(true);

            // TODO: TEST
            AssetManager assetManager = getResources().getAssets();
            InputStream inputStream = assetManager.open("LearningCategories.xls");
            // TODO: ^ TEST

            Workbook workbook = Workbook.getWorkbook(inputStream, ws);
            int count = workbook.getNumberOfSheets();
            CategoryButton.Label[] viewLabels = new CategoryButton.Label[count];
            for (int sheetIndex = 0; sheetIndex < count; sheetIndex++) {
                Sheet sheet = workbook.getSheet(sheetIndex);

                CategoryButton.Label viewLabel = new CategoryButton.Label(
                        sheet.getCell(3, 1).getContents(),
                        sheet.getCell(4, 1).getContents(),
                        sheet.getCell(5, 1).getContents(),
                        sheet.getCell(6, 1).getContents());

                viewLabels[sheetIndex] = viewLabel;
            }

            return viewLabels;
        } catch (Exception e) {
            Exception ei = e;

            return new CategoryButton.Label[0];
        }
    }
}
