package com.tm.chart.vote;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import androidx.annotation.Nullable;

public class VoteChartView extends View {

    private static final String TAG = "BarChartView";

    private Context context;
    private Paint paintBg;
    private Paint paintBar;
    private Paint textPaint;
    private Paint bitmapPaint;


    private static final int DEFAULT_SINGLE_BAR_HEIGHT = 100;
    private static final int DEFAULT_SINGLE_BAR_SPACE = 30;

    private static final int COLOR_TEXT_BLUE = Color.parseColor("#0093F4");
    private static final int COLOR_TEXT_GREY = Color.parseColor("#666666");
    private static final int COLOR_TEXT_LIGHT_GREY = Color.parseColor("#999999");
    private static final int COLOR_BG_WHITE = Color.parseColor("#ffffff");
    private static final int COLOR_BG_STOKE_BLUE = Color.parseColor("#4D0093F4");
    private static final int COLOR_BG_STOKE_GREY = Color.parseColor("#E3E3E3");
    private static final int COLOR_PERCENT_GREY = Color.parseColor("#E5E5E5");
    private static final int COLOR_PERCENT_BLUE = Color.parseColor("#1A0093F4");

    public static final int CORNER_TOP_LEFT = 1;
    public static final int CORNER_TOP_RIGHT = 1 << 1;
    public static final int CORNER_BOTTOM_LEFT = 1 << 2;
    public static final int CORNER_BOTTOM_RIGHT = 1 << 3;
    public static final int CORNER_ALL = CORNER_TOP_RIGHT | CORNER_BOTTOM_RIGHT;

    private int barHeight = DEFAULT_SINGLE_BAR_HEIGHT;
    private int barSpace = DEFAULT_SINGLE_BAR_SPACE;
    private int roundRectRadius = 10;
    private int strokeWidth = 1;
    private int spaceBitmapLeft = 10;

    private int size = 5;

    private List<Data> list;

    private int width;

    private int selecteI = -1;
    private boolean isSelected = false;

    /**
     * 动画的value
     */
    private int value = 0;
    private static final int ANIM_TIME = 300;
    private static final int AMIN_MAX_VALUE = 100;

    public VoteChartView(Context context) {
        super(context);
        init(context);
    }

    public VoteChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoteChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paintBg = new Paint();
        paintBg.setColor(COLOR_BG_WHITE);
        paintBg.setAntiAlias(true);
        paintBg.setStyle(Paint.Style.FILL);

        paintBar = new Paint();
        paintBar.setColor(COLOR_PERCENT_GREY);
        paintBar.setAntiAlias(true);
        paintBar.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                14,
                context.getResources().getDisplayMetrics()));
        textPaint.setColor(COLOR_TEXT_BLUE);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.DEFAULT);

        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);

        barHeight = DensityUtils.dp2px(context, 40);
        barSpace = DensityUtils.dp2px(context, 11);
        roundRectRadius = DensityUtils.dp2px(context, 4);
        strokeWidth = DensityUtils.dp2px(context, 0.5f);
        spaceBitmapLeft =  DensityUtils.dp2px(context, 6);

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (list != null && list.size() > 0) {
            width = getWidth();
            if (isSelected) {//开始加载动画

            } else {

            }
            for (int i = 0; i < list.size(); i++) {
                Data data = list.get(i);
                float percent = data.getPercent();
                float perWidth = percent * width;

                String text = data.getText();

                Rect rect = new Rect(0,
                        barHeight * i + barSpace * i,
                        width,
                        barHeight * (i + 1) + barSpace * i);
                RectF rectF = new RectF(rect.left, rect.top, rect.right, rect.bottom);
                RectF rectFStroke = new RectF(rect.left + strokeWidth,
                        rect.top + strokeWidth,
                        rect.right - strokeWidth,
                        rect.bottom - strokeWidth);

                Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
                float bottomLineY = rect.centerY() - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
                data.setRect(rect);
                if (selecteI < 0) {
                    paintBg.setStyle(Paint.Style.FILL);
                    paintBg.setColor(COLOR_BG_WHITE);
                    canvas.drawRoundRect(rectF, roundRectRadius,roundRectRadius, paintBg);

                    paintBg.setStyle(Paint.Style.STROKE);
                    paintBg.setStrokeWidth(strokeWidth);
                    paintBg.setColor(COLOR_BG_STOKE_BLUE);
                    canvas.drawRoundRect(rectFStroke, roundRectRadius,roundRectRadius, paintBg);

                    if (!TextUtils.isEmpty(text)) {
                        textPaint.setColor(COLOR_TEXT_BLUE);
                        textPaint.setTextAlign(Paint.Align.CENTER);
                        text = TextUtils.ellipsize(text, new TextPaint(textPaint), rect.right - rect.left - barSpace * 2, TextUtils.TruncateAt.END).toString();
                        canvas.drawText(text, (rect.left + rect.right) / 2, bottomLineY, textPaint);
                    }
                } else {
                    Rect rectPercent = new Rect(0,
                            barHeight * i + barSpace * i,
                            (int) perWidth,
                            barHeight * (i + 1) + barSpace * i);
                    //加动画
                    float valueWidth = (float) (rectPercent.right - rectPercent.left) / AMIN_MAX_VALUE * value;
                    RectF rectFPercent = new RectF(rectPercent.left, rectPercent.top, rectPercent.left + valueWidth, rectPercent.bottom);
                    //无动画
//                    RectF rectFPercent = new RectF(rectPercent.left, rectPercent.top, rectPercent.right, rectPercent.bottom);

                    paintBg.setStyle(Paint.Style.FILL);
                    paintBg.setColor(COLOR_BG_WHITE);
                    canvas.drawRoundRect(rectF, roundRectRadius,roundRectRadius, paintBg);

                    if (selecteI == i) {
                        paintBg.setColor(COLOR_BG_STOKE_BLUE);
                        paintBar.setColor(COLOR_PERCENT_BLUE);

                    } else {
                        paintBg.setColor(COLOR_BG_STOKE_GREY);
                        paintBar.setColor(COLOR_PERCENT_GREY);
                    }

                    paintBg.setStyle(Paint.Style.STROKE);
                    paintBg.setStrokeWidth(strokeWidth);
                    canvas.drawRoundRect(rectFStroke, roundRectRadius,roundRectRadius, paintBg);

                    Path path = new Path();
                    float radii[] ={roundRectRadius, roundRectRadius, 0, 0, 0, 0, roundRectRadius, roundRectRadius};
                    path.addRoundRect(rectFPercent, radii, Path.Direction.CCW);
                    canvas.drawPath(path, paintBar);

                    String textPercent = (int) (percent * 10000) + "人";

                    if (selecteI == i) {
                        textPaint.setColor(COLOR_TEXT_BLUE);
                    } else {
                        textPaint.setColor(COLOR_TEXT_GREY);
                    }
                    textPaint.setTextAlign(Paint.Align.LEFT);

                    float avail;
                    if (selecteI == i) {//不想判断图片多宽了
                        avail = rect.right - rect.left - barSpace * 4 - textPaint.measureText(textPercent);
                    } else {
                        avail = rect.right - rect.left - barSpace * 3 - textPaint.measureText(textPercent);
                    }
                    text = TextUtils.ellipsize(text, new TextPaint(textPaint), avail, TextUtils.TruncateAt.END).toString();
                    //有动画
                    float textLeftWidth = (rect.right - rect.left - textPaint.measureText(text)) / 2 - barSpace;
                    float leftValue = textLeftWidth / AMIN_MAX_VALUE * (AMIN_MAX_VALUE - value);
                    canvas.drawText(text, rect.left + barSpace + leftValue, bottomLineY, textPaint);
                    //无动画
//                    canvas.drawText(text, rect.left + barSpace, bottomLineY, textPaint);

                    if (selecteI == i) {
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_select);
                        int height = bitmap.getHeight();
                        //有动画
                        canvas.drawBitmap(bitmap,
                                rect.left + barSpace + textPaint.measureText(text) + spaceBitmapLeft + leftValue,
                                rect.top + (rect.bottom - rect.top - height) / 2, bitmapPaint);
                        //无动画
//                        canvas.drawBitmap(bitmap,
//                                rect.left + barSpace + textPaint.measureText(text) + spaceBitmapLeft,
//                                rect.top + (rect.bottom - rect.top - height) / 2, bitmapPaint);
                    }

                    if (selecteI == i) {
                        textPaint.setColor(COLOR_TEXT_BLUE);
                    } else {
                        textPaint.setColor(COLOR_TEXT_LIGHT_GREY);
                    }
                    textPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(textPercent, rect.right - barSpace, bottomLineY, textPaint);
                }

            }

            paintBar.reset();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getWidth();
//        int height = measureHeight();
//        setMeasuredDimension(width, height);
    }

    private int measureHeight() {
        int height = 0;
        if (list != null && list.size() > 0) {
            height = list.size() * barHeight + (list.size() - 1) * barSpace;
        }
        int defaultHeight = height + getPaddingTop() + getPaddingBottom();
        return defaultHeight;
    }

    private void initAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, AMIN_MAX_VALUE);
        animator.setDuration(ANIM_TIME);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (selecteI < 0) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float y = event.getY();
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        Data data = list.get(i);
                        Rect rect = data.getRect();
                        if (y < rect.bottom && y > rect.top) {
                            selecteI = i;
                            isSelected = true;
                            invalidate();
                            initAnim();
                            return true;
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setData(List<Data> list) {
        if (list == null) {
            return;
        }

        if (this.list == null || list.size() != this.list.size()) {
            this.list = list;
            ViewGroup.LayoutParams lp = this.getLayoutParams();
            int height = measureHeight();
            lp.height = height;
            this.setLayoutParams(lp);
        } else {
            this.list = list;
        }

        invalidate();
    }

    public void reset() {
        selecteI = -1;
        isSelected = false;
        invalidate();
    }

    public static class Data {
        private float percent;
        private String text;
        private Rect rect;

        public float getPercent() {
            return percent;
        }

        public void setPercent(float percent) {
            this.percent = percent;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Rect getRect() {
            return rect;
        }

        public void setRect(Rect rect) {
            this.rect = rect;
        }
    }

}
