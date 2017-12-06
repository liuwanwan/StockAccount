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

/**
 * Created by Administrator on 2017/12/6 0006.
 */

public class StockFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private String[] title = {"JAVA","C","C++","C#","PYTHON","PHP"
            ,".NET","JAVASCRIPT","RUBY","PERL","VB","OC","SWIFT"
    };
    private ArrayList<String> mTitle=new ArrayList<>();
    private ArrayList<ArrayMap> stockList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {if (!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);  //注册
        View view= inflater.inflate(R.layout.layout_fragment_stock, container, false);
        ArrayMap<String, Object> newMap;

        newMap = new ArrayMap<String, Object>();
        newMap.put("a", 1);
        newMap.put("b", "b");

        stockList.add(0,newMap);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        //ArrayList.addAll();
        //Collections.addAll(mTitle,title);
        //为RecyclerView添加默认动画效果，测试不写也可以
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerViewAdapter=new RecyclerViewAdapter(getActivity(), stockList));
        // mRecyclerView.setAdapter(mRecyclerViewAdapter=new RecyclerViewAdapter(getActivity(), mTitle));
        return view;
    }
    // 接收函数一
    @Subscribe
    public void onEvent(EventUtil event) {
       // int i = event.getCourseInt();
       // if (i==0){
            //添加模拟数据到第一项
           mTitle.add(0, "www.lijizhou.com");
            int a = event.getCourseInt();
            String b = event.getChapterInt();
             ArrayMap<String, Object> newMap;

            newMap = new ArrayMap<String, Object>();

            newMap.put("a", ++a);
            newMap.put("b", b);

            stockList.add(0,newMap);

            //RecyclerView列表进行UI数据更新
            mRecyclerViewAdapter.notifyItemInserted(0);
            //如果在第一项添加模拟数据需要调用 scrollToPosition（0）把列表移动到顶端（可选）
            mRecyclerView.scrollToPosition(0);
        //}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);//取消注册
    }
}