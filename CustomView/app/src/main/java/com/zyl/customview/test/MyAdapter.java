package com.zyl.customview.test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyl.customview.R;

import java.util.ArrayList;

/**
 * Created by zhangyuanlu on 2018/1/18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private ArrayList<String> mData;
    private onItemClickListener listener;

    public MyAdapter(ArrayList<String> data){
        this.mData=data;
    }
    public void updateData(ArrayList<String> data){
        this.mData=data;
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.textView.setText(mData.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null) {
                    int position = holder.getLayoutPosition();
                    listener.onItemClick(holder.itemView,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData==null?0:mData.size();
    }
    public interface onItemClickListener{
        void onItemClick(View view,int position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
        }
    }
}