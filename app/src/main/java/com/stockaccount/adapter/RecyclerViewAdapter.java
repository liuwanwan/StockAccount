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
import android.support.v7.widget.*;

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<ArrayMap> stockList;

    public RecyclerViewAdapter(Context context, ArrayList<ArrayMap> list)
	{
        mContext = context;
        stockList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void remove(int position)
	{
        stockList.remove(position);
        notifyItemRemoved(position);
    }

    public void add(ArrayMap arrayMap, int position)
	{
        stockList.add(arrayMap);
        notifyItemInserted(position);
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.layout_stock_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position)
	{
        ArrayMap map=stockList.get(position);
		if (map != null)
		{
			holder.stockName.setText(map.get("name") + "");
			holder.stockCode.setText(map.get("code") + "");
			holder.stockTodayRate.setText(map.get("todayRate") + "");
			holder.stockAccumulateRate.setText(map.get("accumulateRate") + "");
			holder.mCardView.setOnClickListener(new View.OnClickListener() {  
                    @Override  
                    public void onClick(View v)
					{  
                        //listener.onClicked(position,holder.mCardView);  
                    }  
                });  
			holder.mCardView.setOnLongClickListener(new View.OnLongClickListener(){  
                    @Override  
                    public boolean onLongClick(View v)
					{  
                        //listener.onClicked(position,holder.mCardView);  
                        return true;  
                    }  
                });  
		}
    }

    @Override
    public int getItemCount()
	{
        return stockList == null ? 0 : stockList.size();
    }
    public  static class MyViewHolder extends RecyclerView.ViewHolder
	{

		CardView mCardView;  
		TextView stockName, stockCode, stockTodayRate,stockAccumulateRate;
        public MyViewHolder(View itemView)
		{
            super(itemView);
			mCardView = (CardView)itemView.findViewById(R.id.card_view);
            stockName = (TextView)itemView.findViewById(R.id.tv_stock_name);
            stockCode = (TextView)itemView.findViewById(R.id.tv_stock_code);
			stockTodayRate = (TextView)itemView.findViewById(R.id.tv_stock_todayrate);
            stockAccumulateRate = (TextView)itemView.findViewById(R.id.tv_stock_accumulaterate);
        }
    }
}
