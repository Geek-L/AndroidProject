package com.zyl.customview.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyl.customview.R;
import com.zyl.customview.view.QQMusicEqualizerView;

public class QQMusicEqualizerViewTest extends AppCompatActivity implements QQMusicEqualizerView.updateDecibelListener{
    private QQMusicEqualizerView mView;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqmusic_equalizer_view_test);

        initView();
    }
    public void initView(){
        WindowManager wm=getWindowManager();
        DisplayMetrics dm=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        float step=width/11f;
        float padding=step/2;
        mView=findViewById(R.id.testview);
        int[] array=new int[]{3,1,2,7,-6,-4,-9,11,-10,5};
        mView.setDecibelArray(array);
        mView.setUpdateDecibelListener(this);

        LinearLayout linearLayout=findViewById(R.id.linearlayout1);
        linearLayout.setPadding((int)padding,0,(int)padding,0);
        tv1=findViewById(R.id.textView1);
        tv2=findViewById(R.id.textView2);
        tv3=findViewById(R.id.textView3);
        tv4=findViewById(R.id.textView4);
        tv5=findViewById(R.id.textView5);
        tv6=findViewById(R.id.textView6);
        tv7=findViewById(R.id.textView7);
        tv8=findViewById(R.id.textView8);
        tv9=findViewById(R.id.textView9);
        tv10=findViewById(R.id.textView10);

        setDecibel(array);
    }
    public void setDecibel(int[] array){
        tv1.setText(array[0]+"db");
        tv2.setText(array[1]+"db");
        tv3.setText(array[2]+"db");
        tv4.setText(array[3]+"db");
        tv5.setText(array[4]+"db");
        tv6.setText(array[5]+"db");
        tv7.setText(array[6]+"db");
        tv8.setText(array[7]+"db");
        tv9.setText(array[8]+"db");
        tv10.setText(array[9]+"db");
    }
    @Override
    public void updateDecibel(int[] decibels) {
        setDecibel(decibels);
    }
}
