package com.zyl.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zyl.customview.util.DisplayUtil;

/**
 * Created by zhangyuanlu on 2018/1/19.
 */

public class FlexibleFontView extends View {
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private Context mContext;
    public FlexibleFontView(Context context) {
        this(context,null);
    }

    public FlexibleFontView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlexibleFontView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public FlexibleFontView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext=context;
        init();
    }
    public void init(){
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setTextSize(DisplayUtil.sp2px(mContext,42));
    }
    private int measureView(int measureSpec,int defaultSize){
        int measureSize;
        int size=MeasureSpec.getSize(measureSpec);
        int mode=MeasureSpec.getMode(measureSpec);
        if(mode==MeasureSpec.EXACTLY)
            measureSize=size;
        else if(mode==MeasureSpec.AT_MOST)
            measureSize=Math.min(size,defaultSize);
        else
            measureSize=defaultSize;
        return measureSize;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureView(widthMeasureSpec,200),
                measureView(heightMeasureSpec,300));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String testString="测试文字";
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.rotate(30);
        int cx=mWidth/2,cy=mHeight/2;
        canvas.drawText(testString,cx+200,cy,mPaint);
        canvas.save();
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=getWidth();
        mHeight=getHeight();
    }
}
