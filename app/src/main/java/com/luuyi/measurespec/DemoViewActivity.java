package com.luuyi.measurespec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * view属性展示页面
 *
 * @author liyi
 * @date 2019/08/13 11:33
 */
public class DemoViewActivity extends AppCompatActivity {

    /**
     * view 类型为 View
     */
    private static final int VIEW_TYPE_NORMAL = 0;
    /**
     * view 类型为 TextView
     */
    private static final int VIEW_TYPE_TEXT = 1;
    /**
     * view 类型为 EditText
     */
    private static final int VIEW_TYPE_EDIT = 2;
    /**
     * 被观察的view
     */
    private View mView;

    /**
     * 自定义 ViewGroup
     */
    private TestViewGroup layoutTest;

    /**
     * 展示被观察view的宽度
     */
    private TextView tvWidth;
    /**
     * 展示被观察view的高度
     */
    private TextView tvHeight;

    /**
     * 选择被观察view的宽度测量属性
     */
    private RadioGroup rgWidthSpecMode;
    /**
     * 选择被观察view的高度测量属性
     */
    private RadioGroup rgHeightSpecMode;
    /**
     * 输入被观察view的宽度测量值
     */
    private EditText etWidthSpecSize;
    /**
     * 输入被观察view的高度测量值
     */
    private EditText etHeightSpecSize;

    /**
     * 被观察view为 TextView 或 EditText 时的填充内容
     */
    private EditText etContent;
    /**
     * 选择被观察view的类型
     */
    private RadioGroup rgViewType;

    /**
     * 被观察view的宽度测量属性
     */
    private int widthSpecMode = View.MeasureSpec.UNSPECIFIED;
    /**
     * 被观察view的高度测量属性
     */
    private int heightSpecMode = View.MeasureSpec.UNSPECIFIED;
    /**
     * 被观察view的宽度测量值
     */
    private int widthSpecSize = 300;
    /**
     * 被观察view的高度测量值
     */
    private int heightSpecSize = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_view);

        initViews();
        initDemoView(VIEW_TYPE_NORMAL);
    }

    /**
     * 初始化布局
     */
    private void initViews() {
        layoutTest = findViewById(R.id.layout_demo_view_group);

        tvWidth = findViewById(R.id.tv_demo_view_width);
        tvHeight = findViewById(R.id.tv_demo_view_height);

        rgWidthSpecMode = findViewById(R.id.rg_demo_view_measure_width_spec_mode);
        rgHeightSpecMode = findViewById(R.id.rg_demo_view_measure_height_spec_mode);

        etWidthSpecSize = findViewById(R.id.et_demo_view_measure_width_spec_size);
        etWidthSpecSize.setText(Integer.toString(widthSpecSize));
        etHeightSpecSize = findViewById(R.id.et_demo_view_measure_height_spec_size);
        etHeightSpecSize.setText(Integer.toString(heightSpecSize));

        etContent = findViewById(R.id.et_demo_view_content);
        rgViewType = findViewById(R.id.rg_demo_view_type);

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
                widthSpecSize = s.toString().isEmpty() ? 0 : Integer.parseInt(s.toString());
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
                heightSpecSize = s.toString().isEmpty() ? 0 : Integer.parseInt(s.toString());
                calculateSize();
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mView instanceof TextView) {
                    ((TextView) mView).setText(s.toString());
                    calculateSize();
                }
            }
        });
        rgViewType.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            switch (checkedId) {
                case R.id.rbtn_demo_view_type_normal:
                    initDemoView(VIEW_TYPE_NORMAL);
                    break;
                case R.id.rbtn_demo_view_type_text:
                    initDemoView(VIEW_TYPE_TEXT);
                    break;
                case R.id.rbtn_demo_view_type_edit:
                    initDemoView(VIEW_TYPE_EDIT);
                    break;
            }
        });
    }

    /**
     * 切换demo view类型
     *
     * @param type
     */
    private void initDemoView(int type) {
        switch (type) {
            case VIEW_TYPE_NORMAL:
                mView = new View(this);
                mView.setBackgroundResource(R.mipmap.moments);
                break;
            case VIEW_TYPE_TEXT:
                mView = new TextView(this);
                ((TextView) mView).setText(etContent.getText().toString());
                ((TextView) mView).setGravity(View.TEXT_ALIGNMENT_CENTER);
                mView.setBackgroundColor(0xFFCCCCCC);
                break;
            case VIEW_TYPE_EDIT:
                mView = new EditText(this);
                mView.setBackgroundColor(0xFFCCCCCC);
                ((EditText) mView).setText(etContent.getText().toString());
                ((EditText) mView).setGravity(View.TEXT_ALIGNMENT_CENTER);
                break;
        }
        // 重新添加view
        layoutTest.removeAllViews();
        layoutTest.addView(mView);
        // 计算信息并展示
        calculateSize();
    }

    /**
     * 计算信息并展示
     */
    private void calculateSize() {
        int w = View.MeasureSpec.makeMeasureSpec(widthSpecSize, widthSpecMode);
        int h = View.MeasureSpec.makeMeasureSpec(heightSpecSize, heightSpecMode);
        mView.measure(w, h);
        // 展示计算结果
        tvWidth.setText(String.format("View width: %1$d", mView.getMeasuredWidth()));
        tvHeight.setText(String.format("View height: %1$d", mView.getMeasuredHeight()));
        // 重新绘制布局
        layoutTest.setChildMeasureSpec(w, h);
        layoutTest.requestLayout();
    }
}
