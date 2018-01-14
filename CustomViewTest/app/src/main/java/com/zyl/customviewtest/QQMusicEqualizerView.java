package com.zyl.customviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



/**
 * Created by zhangyuanlu on 2018/1/10.
 */

public class QQMusicEqualizerView extends View {

    private Context mContext;
    private Paint mPaint;
    private Paint nodePaint;
    private Paint connectPaint;
    private int mWidth,mHeight;
    private PointF[] pointsArray;
    private final int STATE_NONE=0;
    private final int STATE_TOUCH_DOWN=1;
    private final int STATE_TOUCH_MOVE=2;
    private final int STATE_TOUCH_UP=3;
    private int STATE_NOW=STATE_NONE;

    private int[] decibelArray;
    private float mRadius;
    private float step;
    private updateDecibelListener listener;
    interface updateDecibelListener{
        void updateDecibel(int[] decibels);
    }
    public QQMusicEqualizerView(Context context) {
        this(context,null);
    }

    public QQMusicEqualizerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQMusicEqualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        init();
    }

    public QQMusicEqualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setUpdateDecibelListener(updateDecibelListener listener){
        this.listener=listener;
    }
    public int[] getDecibelArray() {
        return decibelArray;
    }

    public void setDecibelArray(int[] decibelArray) {
        this.decibelArray = decibelArray;
        invalidate();
    }

    public void init(){
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        nodePaint=new Paint();
        nodePaint.setAntiAlias(true);
        nodePaint.setColor(ContextCompat.getColor(mContext,R.color.equalizer_node_over_line));
        nodePaint.setStrokeWidth(6);
        nodePaint.setStyle(Paint.Style.STROKE);
        connectPaint=new Paint();
        connectPaint.setAntiAlias(true);
        connectPaint.setStrokeWidth(50);
        connectPaint.setStyle(Paint.Style.FILL);
        connectPaint.setColor(ContextCompat.getColor(mContext,R.color.equalizer_connect_node_line));

        pointsArray=new PointF[12];
        decibelArray=new int[10];
    }
    private int measureView(int measureSpec,int defaultSize){
        int measureSize;
        int mode=View.MeasureSpec.getMode(measureSpec);
        int size=View.MeasureSpec.getSize(measureSpec);
        if(mode== MeasureSpec.EXACTLY){
            measureSize=size;
        }else{
            measureSize=defaultSize;
            if(mode==MeasureSpec.AT_MOST){
                measureSize=Math.min(measureSize,defaultSize);
            }
        }
        return measureSize;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureView(widthMeasureSpec,400),
                measureView(heightMeasureSpec,200));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth=getWidth();
        mHeight=getHeight();
        step=mHeight/26;    //-12到12共26份
        canvas.drawColor(ContextCompat.getColor(mContext,R.color.equalizer_background));

        int stepSize=mWidth/11;
        pointsArray[0]=new PointF(-50,step*13);
        pointsArray[11] = new PointF(mWidth+50, step*13);
        if((STATE_NOW == STATE_NONE)) {
            for (int i = 1; i <= 10; i++) {
                float cx = stepSize * i, cy = step*(decibelArray[i-1]+13);
                pointsArray[i] = new PointF(cx, cy);
            }
            refreshView(canvas,stepSize);
        }else {
            refreshView(canvas,stepSize);
        }
    }
    private void refreshView(Canvas canvas,int stepSize){
        float[] points=new float[]{pointsArray[0].x,pointsArray[0].y,pointsArray[1].x,pointsArray[1].y,
                pointsArray[1].x,pointsArray[1].y,pointsArray[2].x,pointsArray[2].y,
                pointsArray[2].x,pointsArray[2].y,pointsArray[3].x,pointsArray[3].y,
                pointsArray[3].x,pointsArray[3].y,pointsArray[4].x,pointsArray[4].y,
                pointsArray[4].x,pointsArray[4].y,pointsArray[5].x,pointsArray[5].y,
                pointsArray[5].x,pointsArray[5].y,pointsArray[6].x,pointsArray[6].y,
                pointsArray[6].x,pointsArray[6].y,pointsArray[7].x,pointsArray[7].y,
                pointsArray[7].x,pointsArray[7].y,pointsArray[8].x,pointsArray[8].y,
                pointsArray[8].x,pointsArray[8].y,pointsArray[9].x,pointsArray[9].y,
                pointsArray[9].x,pointsArray[9].y,pointsArray[10].x,pointsArray[10].y,
                pointsArray[10].x,pointsArray[10].y,pointsArray[11].x,pointsArray[11].y};
        canvas.drawLines(points,connectPaint);
        for(int i=1;i<=10;i++){
            float cx = stepSize * i, cy = pointsArray[i].y;
            if(i==index&&STATE_NOW!=STATE_TOUCH_UP) {
                mRadius=50;
            }else{
                mRadius=40;
            }
            canvas.drawCircle(cx, cy, mRadius, nodePaint);
            canvas.drawCircle(cx, cy, mRadius - 6 , connectPaint);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.equalizer_node_over_line));
            mPaint.setStrokeWidth(6);
            canvas.drawLine(cx, cy + mRadius + 3, stepSize * i, mHeight, mPaint);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.equalizer_node_last_line));
            canvas.drawLine(cx, cy - mRadius - 3, stepSize * i, 0, mPaint);
        }
    }

    private int mLastY=0;
    private int index=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        int x=(int)event.getX(),y=(int)event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:{
                index=findTheIndex(x,y);
                if(index!=0){
                    STATE_NOW=STATE_TOUCH_DOWN;
                    invalidate();

                }
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                float deltaY=y-mLastY;

                if(index!=0){
                    STATE_NOW=STATE_TOUCH_MOVE;
                    pointsArray[index].y+=deltaY;
                    if(y<=40)
                        pointsArray[index].y=40;
                    if(y>=mHeight-40)
                        pointsArray[index].y=mHeight-40;
                    decibelArray[index-1]=getTheDecibel(pointsArray[index].y);
                    invalidate();
                    listener.updateDecibel(decibelArray);
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                if(index!=0){
                    STATE_NOW=STATE_TOUCH_UP;
                    if(decibelArray[index-1]!=0&&decibelArray[index-1]!=-12&&
                            decibelArray[index-1]!=12) {
                        float lastY = step * (decibelArray[index-1] + 13);
                        pointsArray[index].y = lastY;
                    }else if(decibelArray[index-1]==0)
                        pointsArray[index].y=step*13;
                    invalidate();
                }
                break;
            }
            default:
                break;
        }
        mLastY=y;
        return true;
    }

    /**
     * 查出当前正在操作的是哪个结点
     * @param x
     * @param y
     * @return
     */
    private int findTheIndex(float x,float y){
        int result=0;
        for(int i=1;i<pointsArray.length;i++){
            if(pointsArray[i].x-mRadius*1.5<x&&pointsArray[i].x+mRadius*1.5>x&&
                    pointsArray[i].y-mRadius*1.5<y&&pointsArray[i].y+mRadius*1.5>y) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * 将坐标转换为-12到12之间的数字
     * @param y
     * @return
     */
    private int getTheDecibel(float y){
        if(y==getHeight()-40)
            return -12;
        else if(y==40f)
            return 12;
        else
            return 13-Math.round(y/step);
    }
}
