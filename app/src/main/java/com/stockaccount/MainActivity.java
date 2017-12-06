package com.stockaccount;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.stockaccount.fragment.StockFragment;
import com.stockaccount.utils.EventUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.TimeZone;
import android.support.v7.app.*;
import android.view.*;
import android.content.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity
{

    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private int fragmentSelected = 0;
    private int courseInt = 0;
    private String chapterInt = "0";
    private RecyclerView mRecyclerView;
    private StockFragment stockFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initBottomNavigationView();

        // 设置默认的Fragment
        setDefaultFragment();
    }
    private void setDefaultFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        stockFragment = new StockFragment();
        transaction.replace(R.id.fragment_content, stockFragment);
        transaction.commit();
    }
    private void initBottomNavigationView()
	{
        FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction transaction = fm.beginTransaction();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
			new BottomNavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem item)
				{
					switch (item.getItemId())
					{
						case R.id.navigation_home:
							if (fragmentSelected != 0)
							{
								if (stockFragment == null)
								{
									stockFragment = new StockFragment();
								}
								// 使用当前Fragment的布局替代id_content的控件
								transaction.replace(R.id.fragment_content, stockFragment);
								transaction.commit();
								fragmentSelected = 0;
							}
							break;
						case R.id.navigation_dashboard:
							//viewPager.setCurrentItem(1);
							break;
						case R.id.navigation_notifications:
							//viewPager.setCurrentItem(2);
							break;
					}
					return false;
				}
			});

    }

    private void refreshMenu()
	{
        supportInvalidateOptionsMenu();
    }


    private void initToolbar()
	{
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getDate());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					switch (item.getItemId())
					{
						case R.id.menu_contents:
							addStock();
							break;
						case R.id.menu_settings:
							Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
							break;
						case R.id.menu_help:
							showHelpDialog();
							break;
						case R.id.menu_specification:
							showSecificationDialog();
							break;
					}
					return true;
				}
			});
    }
    public boolean onCreateOptionsMenu(Menu menu)
	{
        setIconsVisible(menu, true);
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    // 解决menu不显示图标问题
    private void setIconsVisible(Menu menu, boolean flag)
	{
        //判断menu是否为空
        if (menu != null)
		{
            try
			{
                //如果不为空,就反射拿到menu的setOptionalIconsVisible方法
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                //暴力访问该方法
                method.setAccessible(true);
                //调用该方法显示icon
                method.invoke(menu, flag);
            }
			catch (Exception e)
			{
                e.printStackTrace();
            }
        }
    }
    public static String getDate()
	{
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String  mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String  mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay))
		{
            mWay = "天";
        }
		else if ("2".equals(mWay))
		{
            mWay = "一";
        }
		else if ("3".equals(mWay))
		{
            mWay = "二";
        }
		else if ("4".equals(mWay))
		{
            mWay = "三";
        }
		else if ("5".equals(mWay))
		{
            mWay = "四";
        }
		else if ("6".equals(mWay))
		{
            mWay = "五";
        }
		else if ("7".equals(mWay))
		{
            mWay = "六";
        }
        return mYear + "年" + mMonth + "月" + mDay + "日" + "/星期" + mWay;
    }

    private void addStock()
	{
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_stock, (ViewGroup) findViewById(R.id.stock_view));
		final EditText etStockCode=(EditText)v.findViewById(R.id.et_stock_code);
		final EditText etStockNum=(EditText)v.findViewById(R.id.et_stock_num);
		final EditText etStockCost=(EditText)v.findViewById(R.id.et_stock_cost);
		EditText etStockTax=(EditText)v.findViewById(R.id.et_stock_tax);
		EditText etStockExchange=(EditText)v.findViewById(R.id.et_stock_exchange);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("买卖股票");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					EventUtil eventUtil = new EventUtil();
					String code=etStockCode.getText().toString();
					if (StockExist(code))
					{
						eventUtil.setName(getStockName(code));
						eventUtil.setCode(code);
					}
					int num=Integer.valueOf(etStockNum.getText().toString());
					if (num >= 0)
					{
						eventUtil.setNum(num + "");
					}
					double cost=Double.valueOf(etStockCost.getText().toString());
					if (cost > 0.0)
					{
						eventUtil.setCost(cost + "");
					}
					EventBus.getDefault().post(eventUtil);
				}

			});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface p1, int p2)
				{}
			});
        builder.setView(v);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.show();

    }
	private String getStockName(String code)
	{
		// TODO: Implement this method
		return "中原证券";
	}

	private boolean StockExist(String code)
	{
		// TODO: Implement this method
		return true;
	}
    private void showHelpDialog()
	{
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.layout_dialog_help, (ViewGroup) findViewById(R.id.dialog_help));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("使用帮助");
        builder.setPositiveButton("确定", null);
        builder.setView(dialog);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.show();
    }

    private void showSecificationDialog()
	{
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("软件说明");
        builder.setMessage("这是 android.support.v7.app.AlertDialog 中的样式");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

}
