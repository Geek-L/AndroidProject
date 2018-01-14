package com.zyl.customviewtest;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

/**
 * Created by zhangyuanlu on 2018/1/4.
 */

public class BallenetVerifierView extends View implements View.OnTouchListener{
    private Context mContext;
    private String mText;
    private float mTextSize;
    private int mTextColor= Color.WHITE;
    private Paint mPaint;
    private Paint mProgressPaint;
    private Rect mTextRect;
    private Rect mCopyRect;
    private String mCopyText;
    private int mPeriodTime=30*1000;
    private float mProgress=0;
    private int mProgressColor=Color.GREEN;
    private ValueAnimator animator;
    private State mState=State.START;
    private enum State{
        READY,
        START,
    };

    public BallenetVerifierView(Context context) {
        this(context,null);
    }

    public BallenetVerifierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BallenetVerifierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.BallenetVerifierView);
        mText=typedArray.getString(R.styleable.BallenetVerifierView_text);
        mTextSize=typedArray.getDimension(R.styleable.BallenetVerifierView_textSize,mTextSize);
        mTextColor=typedArray.getInt(R.styleable.BallenetVerifierView_textColor,mTextColor);
        mPeriodTime=typedArray.getInt(R.styleable.BallenetVerifierView_period,mPeriodTime);

        mContext=context;
        mTextRect=new Rect();
        mCopyRect=new Rect();
        mPaint=new Paint();
        mProgressPaint=new Paint();
        mCopyText=context.getString(R.string.BallentVerifierView_copy_test);
        if(mState==State.START) {
            animator = ValueAnimator.ofFloat(0f, 360f);
            animator.setDuration(mPeriodTime);
        }else{
            animator=ValueAnimator.ofFloat(0f,3f);
            animator.setDuration(1000);
        }
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        this.setOnTouchListener(this);
    }

    public BallenetVerifierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        int minSize=DisplayUtil.dp2px(mContext,300f);
        if(widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(minSize,minSize);
        }else if(widthSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(minSize,heightSpecSize);
        }else if(heightSpecMode==MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,minSize);
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        //大圆半径
        final int centerX=getWidth()/2,centerY=getWidth()/2;
        final int radius=Math.min(centerX,centerY)-getPaddingLeft();
        //画最大的实心圆
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#434343"));
        canvas.drawCircle(centerX, centerY, radius, mPaint);
        //画外圈圆环
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(centerX, centerY, radius - 30, mPaint);
        switch (mState){
            case READY:{
                //画显示器
                RectF rect=new RectF(centerX/3*2+centerX/12,centerY/3+centerY/12,centerX/3*4-centerX/12,centerY/3*2+centerY/12);
                mPaint.setStrokeWidth(10);
                mPaint.setColor(Color.BLUE);
                canvas.drawRoundRect(rect,20,20,mPaint);
                mPaint.setStrokeWidth(80);
                canvas.drawLine(centerX,centerY/3*2+centerY/12,centerX,centerY/3*2+centerY/12*2,mPaint);
                mPaint.setStrokeWidth(10);
                canvas.drawLine(centerX-70,centerY/3*2+centerY/12*2,centerX+70,centerY/3*2+centerY/12*2,mPaint);
                //画ZZZ
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(50);
                mPaint.getTextBounds("Z Z Z", 0, "Z Z Z".length(), mTextRect);
                if(mProgress<1){
                    canvas.drawText("Z    ",centerX,centerY/3*2-mTextRect.height()/2,mPaint);
                }else if(mProgress<2){
                    canvas.drawText("Z Z  ",centerX,centerY/3*2-mTextRect.height()/2,mPaint);
                }else{
                    canvas.drawText("Z Z Z",centerX,centerY/3*2-mTextRect.height()/2,mPaint);
                }
                //开始游戏吧
                mPaint.setTextSize(60);
                mPaint.setStrokeWidth(3);
                mText=mContext.getString(R.string.BallentVerifierView_nologin_test);
                mPaint.getTextBounds(mText,0,mText.length(),mTextRect);
                canvas.drawText(mText,centerX,centerY+mTextRect.height(),mPaint);
                int tempHeight=mTextRect.height();
                mText=mContext.getString(R.string.BallentVerifierview_startgame_test);
                mPaint.setTextSize(45);
                mPaint.getTextBounds(mText,0,mText.length(),mTextRect);
                canvas.drawText(mText,centerX,centerY+tempHeight+mTextRect.height()*2,mPaint);
                break;
            }
            case START:{
                //显示数字
                mPaint.setColor(mTextColor);
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setStrokeWidth(5);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setTextSize(DisplayUtil.sp2px(mContext, mTextSize));
                mPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
                canvas.drawText(mText, centerX, centerY, mPaint);
                if(mProgress<180){
                    mProgressColor=Color.GREEN;
                }else if(mProgress>=180&&mProgress<270){
                    mProgressColor=Color.parseColor("#FFAA01");
                }else if(mProgress>=270&&mProgress<360){
                    mProgressColor=Color.RED;
                }
                //画进度圆环
                mProgressPaint.setAntiAlias(true);
                mProgressPaint.setStrokeWidth(25);
                mProgressPaint.setStyle(Paint.Style.STROKE);
                mProgressPaint.setColor(mProgressColor);
                RectF oval = new RectF(centerX - radius+30, centerY - radius+30, centerX + radius-30, centerY+ radius-30);
                canvas.drawArc(oval, -90, mProgress, false, mProgressPaint);
                //画倒计时5秒
                if(mProgress>=300){
                    mProgressPaint.setStyle(Paint.Style.FILL);
                    int minRadius=getPaddingLeft();
                    double angle=Math.toRadians(360f-mProgress);
                    float minCenterX=(float) (centerX-(Math.sin(angle)*(radius-30)));
                    float minCenterY=(float) (centerY-(Math.cos(angle)*(radius-30)));
                    canvas.drawCircle(minCenterX,minCenterY,minRadius,mProgressPaint);
                    mPaint.setTextSize(DisplayUtil.sp2px(mContext, mTextSize)-30);
                    mPaint.setStrokeWidth(5);
                    int lastSecond=5;
                    Rect tempRect=new Rect();
                    mPaint.getTextBounds(lastSecond+"",0,1,tempRect);
                    lastSecond=(int)(360-mProgress)/12+1;
                    canvas.drawText(lastSecond+"",minCenterX,minCenterY+tempRect.height()/2,mPaint);
                }
                //点击复制
                mPaint.setTextSize(DisplayUtil.sp2px(mContext,16));
                mPaint.getTextBounds(mCopyText,0,mCopyText.length(),mCopyRect);
                mPaint.setStrokeWidth(2);
                mPaint.setColor(Color.BLUE);
                canvas.drawText(mCopyText,centerX,centerY+mTextRect.height(),mPaint);
                break;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (float) animator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x=motionEvent.getX(),y=motionEvent.getY();
        Point p1=new Point(getWidth()/2-mCopyRect.width()/2,getHeight()/2);
        Point p2=new Point(getWidth()/2+mCopyRect.width()/2,getHeight()/2+2*mCopyRect.height());
        if(motionEvent.getAction()== MotionEvent.ACTION_UP) {
            if (mState== State.START&&x>p1.x&&x<p2.x&&y>p1.y&&y<p2.y) {
                ClipboardManager cm= (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(null,mText));
                Toast.makeText(mContext,mContext.getString(R.string.BallentVerifierView_copyover_test),Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
}
