package com.wingsmight.pushwords.ui.learnedTab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wingsmight.pushwords.R;

public class StartTestButton extends FrameLayout {
    private Button button;
    private View nonTestView;

    private TestView testView;


    public StartTestButton(@NonNull Context context) {
        super(context);

        initView();
    }
    public StartTestButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initView();
    }
    public StartTestButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }


    public void setTest(TestView testView) {
        this.testView = testView;
    }
    public void setNonTestView(View nonTestView) {
        this.nonTestView = nonTestView;
    }
    private void initView() {
        inflate(getContext(), R.layout.start_test_button, this);

        button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            nonTestView.setVisibility(View.GONE);

            testView.startTest();
            testView.setVisibility(View.VISIBLE);
        });
    }
}
