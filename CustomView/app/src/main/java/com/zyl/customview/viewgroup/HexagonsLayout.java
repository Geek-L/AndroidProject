package com.zyl.customview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zyl.customview.view.HexagonView;

/**
 * Created by zhangyuanlu on 2018/1/19.
 */

public class HexagonsLayout extends ViewGroup implements HexagonView.onClickHVListener{
    private int mChildWidth,mChildHeight;
    private onClickItemListener listener;
    public void setOnClickItemListener(onClickItemListener listener){
        this.listener=listener;
    }
    public interface onClickItemListener{
        void onClickItem(int x,int y);
    }
    public HexagonsLayout(Context context) {
        this(context,null);
    }

    public HexagonsLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HexagonsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public HexagonsLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int measureSize(int measureSpec,boolean isWidth){
        int measureSize;
        int mode= View.MeasureSpec.getMode(measureSpec);
        int size=View.MeasureSpec.getSize(measureSpec);
        if(mode==MeasureSpec.EXACTLY)
            measureSize=size;
        else {
            measureSize = 0;
            int count=getChildCount();
            HexagonView view;
            view=(HexagonView)getChildAt(0);
            int maxX=view.getMX(),maxY=view.getMY();
            if(count==1){
                //当只有一个子View的时候强制设定为子View的大小
                measureSize=(isWidth?mChildWidth:mChildHeight);
            }else if(count>0) {
                //遍历获得最大X、Y坐标
                for (int i = 1; i < count; i++) {
                    view=(HexagonView)getChildAt(i);
                    int mX=view.getMX(),mY=view.getMY();
                    if(mX>maxX)
                        maxX=mX;
                    if(mY>=maxY) {
                        maxY = mY;
                    }
                }
                boolean temp=false; //获取最大Y坐标并不能表示高度为Y个子View
                //判断最大Y坐标的一行是否有奇数的X坐标（突出一半的六边形）
                for (int i = 1; i < count; i++) {
                    view = (HexagonView) getChildAt(i);
                    int mX = view.getMX(), mY = view.getMY();
                    if(mY==maxY&&mX%2==1)
                        temp=true;
                }
                int line = mChildWidth / 2;
                //宽度的计算分X坐标为奇数和偶数两种情况
                if(isWidth){
                    if(maxX%2==0)
                        measureSize = (maxX / 2 + 1) * mChildWidth + line * maxX / 2;
                    else
                        measureSize=(maxX+1)/2*mChildWidth+(maxX-1)/2*line+(mChildWidth-line/2);
                }else{//高度的计算同样分两种情况（是否突出一半的六边形）
                    if(temp)
                        measureSize=mChildHeight*(maxY+1)-maxY*2*(mChildHeight/2-(int)(line/2*Math.sqrt(3)))
                                +(int)(line/2*Math.sqrt(3));
                    else
                        measureSize=mChildHeight*(maxY+1)-maxY*2*(mChildHeight/2-(int)(line/2*Math.sqrt(3)));
                }
            }
        }
        return measureSize;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        mChildWidth=getChildAt(0).getMeasuredWidth();
        mChildHeight=getChildAt(0).getMeasuredHeight();
        setMeasuredDimension(measureSize(widthMeasureSpec,true),
                measureSize(heightMeasureSpec,false));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count=getChildCount();
        HexagonView view;
        for(int i=0;i<count;i++){
            view=(HexagonView)getChildAt(i);
            int mX=view.getMX(),mY=view.getMY();
            int l,t,r,b;
            int line = mChildWidth / 2;
            //整体分成X坐标是否为偶数两种情况，然后在Y坐标为0的基础上进行向下的平移
            if (mX % 2 == 0) {
                l = (mX / 2 + 1) * mChildWidth + line * mX / 2 - mChildWidth;
                if(mY==0) {
                    t = (mY / 2) * mChildHeight;
                }else{
                    t = mY*(int)(Math.sqrt(3)*line);
                }
            }else{
                l=(mX+1)/2*mChildWidth+(mX-1)/2*line-line/2;
                if(mY==0){
                    t=(int)(1/2f*(mY*mChildHeight+Math.sqrt(3)*line));
                }else{
                    t =(int)((mY+1/2f)*(Math.sqrt(3)*line));
                }
            }
            r=l+mChildWidth;
            b=t+mChildHeight;
            getChildAt(i).layout(l, t, r, b);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int count=getChildCount();
        HexagonView hexagonView;
        for(int i=0;i<count;i++){
            hexagonView=(HexagonView) getChildAt(i);
            hexagonView.setOnClickHVListener(this);
        }
    }

    @Override
    public void onClick(int x, int y) {
        listener.onClickItem(x,y);
    }
}
