package com.stockaccount.adapter;

import android.content.*;
import android.support.v4.util.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.stockaccount.*;
import java.text.*;
import java.util.*;
import android.graphics.*;

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
			double tRate=map.get("todayRate");
			int tRateColor=Color.WHITE;
			if(tRate>0){
				tRateColor=Color.RED;
			}
			if(tRate<0){
				tRateColor=Color.GREEN;
			}
			holder.stockTodayRate.setText(new DecimalFormat("#0.00").format(tRate) + "%");
			holder.stockTodayRate.setTextColor(tRateColor);
			double tARate=map.get("accumulateRate");
			int tARateColor=Color.WHITE;
			if(tARate>0){
				tARateColor=Color.RED;
			}
			if(tARate<0){
				tARateColor=Color.GREEN;
			}
			holder.stockAccumulateRate.setText(new DecimalFormat("#0.00").format(tARate) + "%");
			holder.stockAccumulateRate.setTextColor(tARateColor);
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
