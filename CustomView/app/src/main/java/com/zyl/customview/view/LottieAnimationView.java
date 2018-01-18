package com.zyl.customview.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.zyl.customview.R;


/**
 * Created by zhangyuanlu on 2018/1/16.
 */

public class LottieAnimationView extends View {
    private Context mContext;
    private Paint mPaint;
    private AnimatorSet animatorSet;
    private int centerX;
    private float circleOffSet;
    private float lineHeight;
    private ValueAnimator circleAnim,lineAnim;
    private int mWidth,mHeight;
    private int outTime=10;
    private boolean autoFresh=true;
    private int time=0;

    public LottieAnimationView(Context context) {
        this(context,null);
    }

    public LottieAnimationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LottieAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.LottieAnimationView);
        outTime=ta.getInteger(R.styleable.LottieAnimationView_outTime,outTime)*1000;
        autoFresh=ta.getBoolean(R.styleable.LottieAnimationView_autoFresh,autoFresh);
        this.mContext=context;
        mPaint=new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }
    public void setOutTime(int seconds){
        this.outTime=seconds*1000;
    }
    public void setAutoFresh(boolean autoFresh){
        this.autoFresh=autoFresh;
    }
    public void startFresh(){
        if(!autoFresh)
            startAnim();
    }
    public void stopFresh(){
        if(lineAnim!=null&&lineAnim.isRunning())
            lineAnim.cancel();
        if(circleAnim!=null&&circleAnim.isRunning())
            circleAnim.cancel();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureView(widthMeasureSpec,200),
                measureView(heightMeasureSpec,200));
    }

    private int measureView(int measureSpec,int defaultSize){
        int measureSize;
        int mode=View.MeasureSpec.getMode(measureSpec);
        int size=View.MeasureSpec.getSize(measureSpec);
        if(mode==MeasureSpec.EXACTLY)
            measureSize=size;
        else if(mode==MeasureSpec.AT_MOST)
            measureSize=Math.min(defaultSize,size);
        else
            measureSize=defaultSize;
        return measureSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=getWidth();
        mHeight=getHeight();
        centerX=getWidth()/2;
        if(autoFresh)
            startAnim();
    }
    private void startAnim(){

        animatorSet=new AnimatorSet();
        lineAnim=ValueAnimator.ofFloat(-100f,100f);
        lineAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lineHeight=(Float)valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        lineAnim.setDuration(600);
        lineAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        circleAnim=ValueAnimator.ofFloat(-10f,10f);
        circleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                circleOffSet= (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        circleAnim.setRepeatMode(ValueAnimator.REVERSE);
        circleAnim.setRepeatCount(1);
        circleAnim.setDuration(400);
        circleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.play(lineAnim).before(circleAnim);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                time+=500;
                if(time<outTime)
                    animatorSet.start();
                else
                    time=0;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(3f/50*mWidth);
        canvas.drawColor(Color.TRANSPARENT);
        if(lineAnim!=null&&lineAnim.isRunning()) {
            if(lineHeight<0) {
                lineHeight=100-Math.abs(lineHeight);
                canvas.drawLine(centerX-2/5f*mWidth,3/10f*mHeight,
                        centerX-2/5f*mWidth,3/10f*mHeight+3/5f*mHeight*lineHeight/100f,mPaint);
                canvas.drawLine(centerX+2/5f*mWidth,3/10f*mHeight,
                        centerX+2/5f*mWidth,3/10f*mHeight+3/5f*mHeight*lineHeight/100f,mPaint);

                canvas.drawLine(centerX-1/5f*mWidth,2/5f*mHeight,
                        centerX-1/5f*mWidth,2/5f*mHeight+3/10f*mHeight*lineHeight/100f,mPaint);
                canvas.drawLine(centerX+1/5f*mWidth,2/5f*mHeight,
                        centerX+1/5f*mWidth,2/5f*mHeight+3/10f*mHeight*lineHeight/100f,mPaint);

                canvas.drawLine(centerX,mHeight/2f,centerX,mHeight/2f+3/10f*mHeight*lineHeight/100f,mPaint);

            }else{
                canvas.drawLine(centerX-2/5f*mWidth,3/10f*mHeight,
                        centerX-2/5f*mWidth,9/10f*mHeight-3/5f*mHeight*lineHeight/100f,mPaint);
                canvas.drawLine(centerX+2/5f*mWidth,3/10f*mHeight,
                        centerX+2/5f*mWidth,9/10f*mHeight-3/5f*mHeight*lineHeight/100f,mPaint);

                canvas.drawLine(centerX-1/5f*mWidth,2/5f*mHeight,
                        centerX-1/5f*mWidth,7/10f*mHeight-2/5f*mHeight*lineHeight/100f,mPaint);
                canvas.drawLine(centerX+1/5f*mWidth,2/5f*mHeight,
                        centerX+1/5f*mWidth,7/10f*mHeight-2/5f*mHeight*lineHeight/100f,mPaint);

                canvas.drawLine(centerX,mHeight/2f,centerX,4/5f*mHeight-1/2f*mHeight*lineHeight/100f,mPaint);
            }
        }
        if(circleAnim!=null&&circleAnim.isRunning()){
            canvas.drawCircle(centerX-2/5f*mWidth,1/5f*mHeight-circleOffSet,3/100f*mWidth,mPaint);
            canvas.drawCircle(centerX-1/5f*mWidth,1/5f*mHeight-circleOffSet/2f,3/100f*mWidth,mPaint);
            canvas.drawCircle(centerX,1/5f*mHeight+circleOffSet/4f,3/100f*mWidth,mPaint);
            canvas.drawCircle(centerX+1/5f*mWidth,1/5f*mHeight+circleOffSet/2f,3/100f*mWidth,mPaint);
            canvas.drawCircle(centerX+2/5f*mWidth,1/5f*mHeight+circleOffSet,3/100f*mWidth,mPaint);
        }
    }
}
