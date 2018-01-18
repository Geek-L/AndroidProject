package com.zyl.customview.test;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zyl.customview.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.onItemClickListener{
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.my_recyclerview);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MyAdapter(initData());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        recyclerView.addItemDecoration(new MyItemDecoration());
    }
    private ArrayList<String> initData(){
        ArrayList<String> data=new ArrayList<>();
        data.add("BallenetVerifierView");
        data.add("QQMusicEqualizerView");
        data.add("LottieAnimationView");
        return data;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent=null;
        switch (position){
            case 0: {
                intent = new Intent(this, BallenetVerifierViewTest.class);
                break;
            }
            case 1:{
                intent=new Intent(this,QQMusicEqualizerViewTest.class);
                break;
            }
            case 2:{
                intent=new Intent(this,LottieAnimationViewTest.class);
                break;
            }
            default:
                break;
        }
        if(intent!=null)
            startActivity(intent);
    }
    class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, 1);
        }
    }
}
