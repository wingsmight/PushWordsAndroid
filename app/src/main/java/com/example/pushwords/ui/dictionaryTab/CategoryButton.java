package com.example.pushwords.ui.dictionaryTab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pushwords.R;
import com.example.pushwords.extensions.StringExt;

public class CategoryButton extends LinearLayout {
    private ImageView iconView;
    private TextView titleTextView;
    private TextView headlineTextView;
    private View backgroundView;


    public CategoryButton(Context context, Label label) {
        super(context);

        initView(label);
    }
    public CategoryButton(Context context) {
        super(context);

        initView(null);
    }
    public CategoryButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView(null);
    }
    public CategoryButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(null);
    }

    private void initView(Label label) {
        inflate(getContext(), R.layout.category_button, this);

        iconView = findViewById(R.id.iconImage);
        titleTextView = findViewById(R.id.titleText);
        headlineTextView = findViewById(R.id.headlineText);
        backgroundView = findViewById(R.id.background);

        setOnClickListener(view -> getContext()
            .startActivity(new Intent(getContext(), CategoryWordTab.class)));

        if (label == null) {
            return;
        }

        iconView.setImageDrawable(null);
        StringExt.getDrawableFromUrl(label.getIconPath(),
                drawable -> ((Activity)getContext()).runOnUiThread(()
                        -> iconView.setImageDrawable(drawable)));

        titleTextView.setText(label.getTitleText());
        headlineTextView.setText(label.getHeadlineText());
        backgroundView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(label.getBackgroundColorHex())));
    }


    public static class Label {
        private String iconPath;
        private String titleText;
        private String headlineText;
        private String backgroundColorHex;


        public Label(String iconPath, String titleText, String headlineText, String backgroundColorHex) {
            this.iconPath = iconPath;
            this.titleText = titleText;
            this.headlineText = headlineText;
            this.backgroundColorHex = backgroundColorHex;
        }


        public String getIconPath() {
            return iconPath;
        }
        public String getTitleText() {
            return titleText;
        }
        public String getHeadlineText() {
            return headlineText;
        }
        public String getBackgroundColorHex() {
            return backgroundColorHex;
        }
    }
}
