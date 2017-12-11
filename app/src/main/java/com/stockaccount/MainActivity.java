package com.stockaccount;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.*;
import com.stockaccount.fragment.*;
import com.stockaccount.utils.*;
import java.lang.reflect.*;
import java.util.*;
import org.greenrobot.eventbus.*;

import android.support.v7.widget.Toolbar;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import android.support.v4.util.*;
import java.util.regex.*;
import android.util.*;

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
    private String rudeInfo="";


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
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
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
        View v = inflater.inflate(R.layout.layout_alert_stock, (ViewGroup) findViewById(R.id.stock_view));
		final EditText etStockCode=(EditText)v.findViewById(R.id.et_stock_code);
		final EditText etStockNum=(EditText)v.findViewById(R.id.et_stock_num);
		final EditText etStockCost=(EditText)v.findViewById(R.id.et_stock_cost);
		final EditText etStockTax=(EditText)v.findViewById(R.id.et_stock_tax);
		final RadioGroup etStockExchange=(RadioGroup)v.findViewById(R.id.rg_exchange);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("买卖股票");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					final EventUtil eventUtil = new EventUtil();
					String numS=etStockNum.getText().toString();
					String costS=etStockCost.getText().toString();
					String taxS=etStockTax.getText().toString();
					String code=etStockCode.getText().toString();

					int num=0;
					double cost=0.0,tax=0.0;
					if (numS != null && numS.length() > 0)
						num = Integer.parseInt(numS);
					if (costS != null && costS.length() > 0)
						cost = Double.parseDouble(costS);
					if (taxS != null && taxS.length() > 0)
						tax = Double.parseDouble(taxS);
					etStockExchange.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
							@Override
							public void onCheckedChanged(RadioGroup p1, int p2)
							{
								int radioButtonId=p1.getCheckedRadioButtonId();
								RadioButton rb=(RadioButton)findViewById(radioButtonId);
								String exchange="";
								exchange = rb.getText().toString();
								if (exchange != null && exchange.length() > 0)
									eventUtil.setExchange(exchange);
							}
						});
					if (StockExist(code) && num > 0 && cost > 0 && tax > 0 && tax < 10)
					{//num属性里已限制为整数,cost属性里已限制为数字,tax单位是万分之一
						queryStocks(code);
						eventUtil.setName(getStockName(code));
						eventUtil.setCode(code);
						eventUtil.setNum(num);
						eventUtil.setCost(cost);
						eventUtil.setTodayRate(getTodayRate(code, cost, num, tax));
						eventUtil.setAccumulateRate(getAccumulateRate(code, cost, num, tax));
						EventBus.getDefault().post(eventUtil);

					}
					else
					{
						Toast.makeText(MainActivity.this, "输入有误！", Toast.LENGTH_SHORT).show();
					}

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
	public double getTodayRate(String code, double cost, int num, double tax)
	{
		double price=getStockPrice(code);
		return price / cost - 1;
	}

	private double getStockPrice(String code)
	{
		queryStocks(code);

		String price="0";
		if (rudeInfo != null && getStockData(rudeInfo) != null)
		{
			//Log.v("AAA","P="+getStockData(rudeInfo));
			price = getStockData(rudeInfo).get("NowPrice");
		}
		return Double.parseDouble(price);
	}
	private HashMap<String,String> getStockData(String rudeInfo)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		StockInfo myStockInfo = new StockInfo();
		String myStockInfoAtrr[] = myStockInfo.getStockInfo();
		if (rudeInfo != null && rudeInfo.length() > 0)
		{
			int endId = rudeInfo.lastIndexOf("\"");
			if (endId <= 12)
			{
				Toast.makeText(this, "股票信息不存在！", Toast.LENGTH_SHORT)
					.show();

				for (int i = 0; i < myStockInfoAtrr.length; i++)
				{
					map.put(myStockInfoAtrr[i], "");
				}
			}
			else
			{
				String tempString = rudeInfo.substring(12, endId);
				Pattern p;
				p = Pattern.compile("([^~]+)\\~");// 鍦ㄨ繖閲岋紝缂栬瘧 鎴愪竴涓鍒欍��
				Matcher m;
				m = p.matcher(tempString);// 鑾峰緱鍖归厤

				for (int i = 0;i < myStockInfoAtrr.length && m.find(); i++)
				{
					map.put(myStockInfoAtrr[i], m.group(1));
					Log.v("AAA", "myStockInfoAtrr[" + i + "]=" + myStockInfoAtrr[i] + "--" + m.group(1));
				}
			}
			return map;
		}
		return null;
	}
	public void queryStocks(String code)
	{
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String STOCK_EXCGANGE = code.substring(0, 1);
		String stockUri="";
		if (STOCK_EXCGANGE.equals("0"))
			stockUri = "http://qt.gtimg.cn/q=sz" + code;
		if (STOCK_EXCGANGE.equals("6"))
			stockUri = "http://qt.gtimg.cn/q=sh" + code;
		//String url ="http://hq.sinajs.cn/list=" + marketandcode;
        //http://hq.sinajs.cn/list=sh600000,sh600536

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stockUri,
			new Response.Listener<String>() {
				@Override
				public void onResponse(String response)
				{

					rudeInfo = response;
				}
			},
			new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error)
				{
				}
			});

        queue.add(stringRequest);
    }
	public double getAccumulateRate(String code, double cost, int num, double tax)
	{
		//if (isSameDay(new Date(), new Date()))
		//return getTodayRate(code, cost, num, tax);
		//else
		{
			double price=getStockPrice(code);
			return price / cost - 1;
		}
	}
    public static boolean isSameDay(Date date1, Date date2)
	{
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTime(date1);

		Calendar calDateB = Calendar.getInstance();
		calDateB.setTime(date2);

		return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
			&& calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
			&& calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
			.get(Calendar.DAY_OF_MONTH);
	}
	private String getStockName(String code)
	{
		String name="";
		if (rudeInfo == null)
		{
			Log.v("AAA", "oooooop=");
			//name = getStockData(rudeInfo).get("Name");
		}
		if (rudeInfo != null && getStockData(rudeInfo) == null)
		{
			Log.v("AAA", "ooLlllllp=");
			//name = getStockData(rudeInfo).get("Name");
		}
		if (rudeInfo != null && getStockData(rudeInfo) != null)
		{
			name = getStockData(rudeInfo).get("Name");
		}

		return name;
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
