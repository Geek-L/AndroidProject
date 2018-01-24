package com.zyl.customview.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zyl.customview.R;
import com.zyl.customview.view.HexagonView;
import com.zyl.customview.viewgroup.HexagonsLayout;

public class HexagonViewTest extends AppCompatActivity implements HexagonsLayout.onClickItemListener{
    private HexagonsLayout hexagonsLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexagon_view_test);

        hexagonsLayout=findViewById(R.id.hexagons_layout);
        hexagonsLayout.setOnClickItemListener(this);
    }

    @Override
    public void onClickItem(int x, int y) {
        Toast.makeText(this,"x="+x+",y="+y,Toast.LENGTH_SHORT).show();
    }
}
