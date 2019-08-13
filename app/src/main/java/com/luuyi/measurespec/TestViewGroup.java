package com.luuyi.measurespec;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author liyi
 * @date 2019/8/13 10:01
 */
public class TestViewGroup extends ViewGroup {

    private int widthMeasureSpec;
    private int heightMeasureSpec;

    public TestViewGroup(Context context) {
        super(context);
    }

    public TestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setChildMeasureSpec(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //  The layout has actually already been performed and the positions
        //  cached.  Apply the cached values to the children.
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(widthMeasureSpec, heightMeasureSpec);
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                child.layout((r - l - width) / 2, (b - t - height) / 2, (r - l + width) / 2, (b - t + height) / 2);
            }
        }
    }
}
