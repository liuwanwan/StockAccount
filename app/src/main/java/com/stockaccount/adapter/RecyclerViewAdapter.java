package com.stockaccount.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stockaccount.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<String> mTitle = new ArrayList<>();
    ArrayList<ArrayMap> stockList;
    private TextView stockName, stockCode, stockPrice;
/*
    public RecyclerViewAdapter(Context context, ArrayList<String> title) {
        mContext = context;
        mTitle = title;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_stock_code);
        }
    }

    public void remove(int position) {
        mTitle.remove(position);
        notifyItemRemoved(position);
    }

    public void add(String text, int position) {
        mTitle.add(position, text);
        notifyItemInserted(position);
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.layout_stock_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.mTextView.setText(mTitle.get(position));
    }

    @Override
    public int getItemCount() {
        return mTitle == null ? 0 : mTitle.size();
    } */

    public RecyclerViewAdapter(Context context, ArrayList<ArrayMap> list){
        mContext=context;
        stockList=list;
        mLayoutInflater=LayoutInflater.from(context);
    }


    public void remove(int position) {
        stockList.remove(position);
        notifyItemRemoved(position);
    }

    public void add(ArrayMap arrayMap, int position) {
        stockList.add(arrayMap);
        notifyItemInserted(position);
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.layout_stock_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {
        //holder.mTextView.setText(mTitle.get(position));
        int a=stockList.get(position).indexOfKey("a");

        ArrayMap m=stockList.get(position);
        holder.mTextView.setText(m.get("a")+"+a");

        holder.textView.setText(m.get("b")+"+");
    }

    @Override
    public int getItemCount() {
        return stockList==null ? 0 : stockList.size();
    }
    public  static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView,textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView=(TextView)itemView.findViewById(R.id.tv_stock_name);
            textView=(TextView)itemView.findViewById(R.id.tv_stock_code);
        }
    }
}