package com.stockaccount.utils;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import java.util.*;
import android.widget.*;
import android.content.*;
import java.util.regex.*;
import android.util.*;
import android.os.*;
import org.greenrobot.eventbus.*;

public class StockInfo
{
	private String name;
	private String code;
	private String exchange;
	private int num;
	private double cost;
	private double price;
	private Context context;
	private Handler handler=new Handler(){
		public void handleMessage(Message msg)
		{   
			switch (msg.what)
			{   
				case 1:   
					getStockData((String) msg.obj);
					EventUtil eventUtil = new EventUtil();
					eventUtil.setName(getName());
					eventUtil.setCode(code);
					eventUtil.setNum(num);
					eventUtil.setCost(cost);
					eventUtil.setExchange(exchange);
					eventUtil.setTodayRate(getTodayRate());
					eventUtil.setAccumulateRate(getAccumulateRate());
					EventBus.getDefault().post(eventUtil);
					break;   
			}   
			super.handleMessage(msg);   
		}   
	};
	public StockInfo(Context context, String code, int num, double cost, String exchange)
	{
		this.context = context;
		this.code = code;
		this.num = num;
		this.cost = cost;
		this.exchange = exchange;
		queryStocks(code);
	}

	public double getTodayRate()
	{
		double price=getPrice();
		return price / cost - 1.00;
	}
	public double getAccumulateRate()
	{
		if (isSameDay(new Date(), new Date()))
			return getTodayRate();
		else
		{
			double price=getPrice();
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
	private void getStockData(String rudeInfo)
	{
		ArrayMap<String, String> map = new ArrayMap<String, String>();
		StockAttr myStockInfo = new StockAttr();
		String myStockInfoAtrr[] = myStockInfo.getStockInfo();
		if (rudeInfo != null && rudeInfo.length() > 0)
		{
			int endId = rudeInfo.lastIndexOf("\"");
			if (endId <= 12)
			{
				Toast.makeText(context, "股票信息不存在！", Toast.LENGTH_SHORT)
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
				p = Pattern.compile("([^~]+)\\~");
				Matcher m;
				m = p.matcher(tempString);
				for (int i = 0;i < myStockInfoAtrr.length && m.find(); i++)
				{
					map.put(myStockInfoAtrr[i], m.group(1));
				}
				setName(map.get("Name"));
				setPrice(Double.parseDouble(map.get("NowPrice")));
			}
		}
	}

	private void setPrice(double price)
	{
		this.price = price;
	}
	public double getPrice()
	{
		return price;
	}
	private void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public void queryStocks(String code)
	{
		final Message message=Message.obtain();
        RequestQueue queue = Volley.newRequestQueue(context);
        String STOCK_EXCGANGE = code.substring(0, 1);
		String stockUri="";
		if (STOCK_EXCGANGE.equals("0"))
			stockUri = "http://qt.gtimg.cn/q=sz" + code;
		if (STOCK_EXCGANGE.equals("6"))
			stockUri = "http://qt.gtimg.cn/q=sh" + code;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stockUri,
			new Response.Listener<String>() {
				@Override
				public void onResponse(String response)
				{
					message.what = 1;
					message.obj = response;
					handler.sendMessage(message);
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

}
