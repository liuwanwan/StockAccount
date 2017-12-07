package com.stockaccount.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stockaccount.R;
import com.stockaccount.adapter.RecyclerViewAdapter;
import com.stockaccount.utils.EventUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.stockaccount.utils.*;
import android.support.v4.widget.*;

public class StockFragment extends Fragment
{
	private SwipeRefreshLayout mSwiperefresh;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<ArrayMap> stockList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {if (!EventBus.getDefault().isRegistered(this))
			EventBus.getDefault().register(this);  //注册
        View view= inflater.inflate(R.layout.layout_fragment_stock, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        //为RecyclerView添加默认动画效果，测试不写也可以
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
		mRecyclerView.setAdapter(mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), stockList));
		mSwiperefresh = (SwipeRefreshLayout)view.findViewById(R.id.main_swipe_refresh_layout);  
		mSwiperefresh.setColorSchemeResources(R.color.colorPrimary);  
		mSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {  
				@Override  
				public void onRefresh()
				{  
					refreshVideoData();  
				}

			});  
        return view;
    }
	private void refreshVideoData()
	{
		// TODO: Implement this method
	}  
    // 接收函数一
    @Subscribe
    public void onEvent(EventUtil event)
	{
		//添加模拟数据到第一项
		String name = event.getName();
		String code = event.getCode();
		String todayRate=event.getTodayRate();
		String accumulateRate=event.getAccumulateRate();
		ArrayMap<String, Object> newMap;
		newMap = new ArrayMap<String, Object>();

		newMap.put("name", name);
		newMap.put("code", code);
        newMap.put("todayRate", todayRate);
		newMap.put("accumulateRate", accumulateRate);
		stockList.add(0, newMap);

		//RecyclerView列表进行UI数据更新
		mRecyclerViewAdapter.notifyItemInserted(0);
		//如果在第一项添加模拟数据需要调用 scrollToPosition（0）把列表移动到顶端（可选）
		mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onDestroy()
	{
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);//取消注册
    }
}
