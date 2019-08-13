# Android MeasureSpec详解

    MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。

* * *

模式 | 数值 | 描述
--- | --- | ---
UNSPECIFIED | 0（0x00000000） | 父控件没有给子视图任何限制，子视图可以设置为任意大小。
EXACTLY | 1073741824（0x40000000） | 表示父控件已经确切的指定了子视图的大小。
AT_MOST | -2147483648（0x80000000） | 表示子查看具体大小没有尺寸限制，但是存在上限，上限一般为父视图大小。

* 简单应用
    ```
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            int heightSize = view.getMeasuredHeight();
    ```
    
    
* 先看源码
    ```
    /**
     * MeasureSpec 可以通过 int 进行标示。
     * int 一共32位，而测量模式有三种，所以定义前两位为mode，后三十位为size
     * 这样，measureSpec 就可以通过一个 int 值进行传递。
     * 虽然这样做有什么好处我不知道，但是这样做就是好的，双手叉腰
     */
    public static class MeasureSpec {
        /** 标示位转化成int时，向左偏移 30 位 */
        private static final int MODE_SHIFT = 30;
        /** 方便下方进行位运算使用的，二进制标示为 1100 0000 ... 0000 */
        private static final int MODE_MASK  = 0x3 << MODE_SHIFT;

        /** 定义注解，用注解相比用enum会性能更好 */
        @IntDef({UNSPECIFIED, EXACTLY, AT_MOST})
        @Retention(RetentionPolicy.SOURCE)
        public @interface MeasureSpecMode {}

        /** 无限制 0000 0000 ... 0000 */
        public static final int UNSPECIFIED = 0 << MODE_SHIFT;

        /** 绝对值 0100 0000 ... 0000 */
        public static final int EXACTLY     = 1 << MODE_SHIFT;

        /** 最大值 1000 0000 ... 0000 */
        public static final int AT_MOST     = 2 << MODE_SHIFT;

        /**
         * 将 mode 和 size 转成一个 int 类型的值
         * @param size 通过注解，限定 size 的范围是 0 到 0011 1111 ... 1111
         * @param mode 通过注解，限定 mode 类型
         */
        public static int makeMeasureSpec(@IntRange(from = 0, to = (1 << MeasureSpec.MODE_SHIFT) - 1) int size,
                                          @MeasureSpecMode int mode) {
            if (sUseBrokenMakeMeasureSpec) {
                // 通过旧的方式构建 MeasureSpec，目前版本 sUseBrokenMakeMeasureSpec 为常量 false
                // 这种方式存在弊端，如果传入参数不规范，数据就会异常
                return size + mode;
            } else {
                // MODE_MASK -> 1100 0000 ... 0000
                // ~MODE_MASK -> 0011 1111 ... 1111
                // & -> 左右两边都为1，结果为1，否则为0
                // | -> 左右两边存在1，结果为1，否则为0
                // 将 size 的前两位置为0，将 mode 的后三十位置为0，然后构建 MeasureSpec
                return (size & ~MODE_MASK) | (mode & MODE_MASK);
            }
        }

        public static int makeSafeMeasureSpec(int size, int mode) {
            // 说真的，我不懂。因为 sUseZeroUnspecifiedMeasureSpec 为常量 false
            if (sUseZeroUnspecifiedMeasureSpec && mode == UNSPECIFIED) {
                return 0;
            }
            return makeMeasureSpec(size, mode);
        }

        @MeasureSpecMode
        public static int getMode(int measureSpec) {
            // 将 mode 的后三十位置为0
            return (measureSpec & MODE_MASK);
        }

        public static int getSize(int measureSpec) {
            // 将 size 的前两位置为0
            return (measureSpec & ~MODE_MASK);
        }

        /**
         * 这个方法不细讲，涉及到 Android 4.3 引入的光学边界这个布局对齐方式
         * 有兴趣可以了解一下
         */
        static int adjust(int measureSpec, int delta) {
            final int mode = getMode(measureSpec);
            int size = getSize(measureSpec);
            if (mode == UNSPECIFIED) {
                return makeMeasureSpec(size, UNSPECIFIED);
            }
            size += delta;
            if (size < 0) {
                Log.e(VIEW_LOG_TAG, "MeasureSpec.adjust: new size would be negative! (" + size +
                        ") spec: " + toString(measureSpec) + " delta: " + delta);
                size = 0;
            }
            return makeMeasureSpec(size, mode);
        }

        /**
         * MeasureSpec: {mode} {size}
         */
        public static String toString(int measureSpec) {
            int mode = getMode(measureSpec);
            int size = getSize(measureSpec);

            StringBuilder sb = new StringBuilder("MeasureSpec: ");

            if (mode == UNSPECIFIED)
                sb.append("UNSPECIFIED ");
            else if (mode == EXACTLY)
                sb.append("EXACTLY ");
            else if (mode == AT_MOST)
                sb.append("AT_MOST ");
            else
                sb.append(mode).append(" ");

            sb.append(size);
            return sb.toString();
        }
    }
    ```
    
* onMeasure() 如何调用的 MeasureSpec
    * 以view方法举例  
        ```
        // 自定义view时，对于view的重新计算，需要重写此方法
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                    getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        }
        
        public static int getDefaultSize(int size, int measureSpec) {
            int result = size;
            int specMode = MeasureSpec.getMode(measureSpec);
            int specSize = MeasureSpec.getSize(measureSpec);
    
            switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            }
            return result;
        }
            
        protected int getSuggestedMinimumWidth() {
            return (mBackground == null) ? mMinWidth : max(mMinWidth, mBackground.getMinimumWidth());
        }
        ```

* View TextView EditText 三种view在不同情况下对于View的展示
    > DemoViewActivity 演示，具体逻辑可以看下源码

* 自定义 ViewGroup 简单实现通过 onLayout 自定义布局位置
    >  TestViewGroup
    ```
        // 所有的view都展示在viewgroup中间
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
    ```