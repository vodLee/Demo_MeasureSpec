package com.luuyi.measurespec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DemoViewActivity extends AppCompatActivity {

    private View mView;

    private TextView tvWidth;
    private TextView tvHeight;

    private RadioGroup rgWidthSpecMode;
    private RadioGroup rgHeightSpecMode;
    private EditText etWidthSpecSize;
    private EditText etHeightSpecSize;

    private int widthSpecMode;
    private int heightSpecMode;
    private int widthSpecSize;
    private int heightSpecSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_view);

        initViews();
        calculateSize();
    }

    private void initViews() {
        mView = new View(this);
        mView.setBackgroundResource(R.mipmap.moments);

        tvWidth = findViewById(R.id.tv_demo_view_width);
        tvHeight = findViewById(R.id.tv_demo_view_height);

        rgWidthSpecMode = findViewById(R.id.rg_demo_view_measure_width_spec_mode);
        rgHeightSpecMode = findViewById(R.id.rg_demo_view_measure_height_spec_mode);

        etWidthSpecSize = findViewById(R.id.et_demo_view_measure_width_spec_size);
        etHeightSpecSize = findViewById(R.id.et_demo_view_measure_height_spec_size);

        rgWidthSpecMode.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            switch (checkedId) {
                case R.id.rbtn_demo_view_measure_width_spec_at_most:
                    widthSpecMode = View.MeasureSpec.AT_MOST;
                    break;
                case R.id.rbtn_demo_view_measure_width_spec_exactly:
                    widthSpecMode = View.MeasureSpec.EXACTLY;
                    break;
                case R.id.rbtn_demo_view_measure_width_spec_unspecified:
                    widthSpecMode = View.MeasureSpec.UNSPECIFIED;
                    break;
            }
            calculateSize();
        });
        rgHeightSpecMode.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            switch (checkedId) {
                case R.id.rbtn_demo_view_measure_height_spec_at_most:
                    heightSpecMode = View.MeasureSpec.AT_MOST;
                    break;
                case R.id.rbtn_demo_view_measure_height_spec_exactly:
                    heightSpecMode = View.MeasureSpec.EXACTLY;
                    break;
                case R.id.rbtn_demo_view_measure_height_spec_unspecified:
                    heightSpecMode = View.MeasureSpec.UNSPECIFIED;
                    break;
            }
            calculateSize();
        });

        etWidthSpecSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                widthSpecSize = Integer.parseInt(s.toString());
                calculateSize();
            }
        });
        etHeightSpecSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                heightSpecSize = Integer.parseInt(s.toString());
                calculateSize();
            }
        });
    }

    /**
     * 计算信息并展示
     */
    private void calculateSize() {
        int w = View.MeasureSpec.makeMeasureSpec(widthSpecSize, widthSpecMode);
        int h = View.MeasureSpec.makeMeasureSpec(heightSpecSize, heightSpecMode);
        mView.measure(w, h);
        tvWidth.setText(String.format("View width: %1$d", mView.getMeasuredWidth()));
        tvHeight.setText(String.format("View height: %1$d", mView.getMeasuredHeight()));
    }
}
