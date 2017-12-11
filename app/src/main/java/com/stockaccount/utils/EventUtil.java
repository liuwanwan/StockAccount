package com.stockaccount.utils;

public class EventUtil
{
    private String name,code,exchange;
	private double todayRate,accumulateRate,price,cost;
private int num,tax;
	public String getName()
	{
		return name;
	}
	public String getCode()
	{
		return code;
	}
    public void setName(String name)
	{
        this.name = name;  
    }  
	public void setCode(String code)
	{
        this.code = code;  
    }  
	public double getTodayRate()
	{
		return todayRate;
	}
	public double getAccumulateRate()
	{
		return accumulateRate;
	}
    public void setTodayRate(double todayRate)
	{
        this.todayRate = todayRate;  
    }  
	public void setAccumulateRate(double accumulateRate)
	{
        this.accumulateRate = accumulateRate;  
    }  
	public void setNum(int num)
	{
        this.num = num;  
    }  
	public double getPrice()
	{
		return price;
	}
	public void setPrice(double price)
	{
        this.price = price;  
    }  
	public double getCost()
	{
		return cost;
	}
	public void setTax(int tax)
	{
        this.tax = tax;  
    }  
	public int getTax()
	{
		return tax;
	}
	public void setCost(double cost)
	{
        this.cost= cost;  
    }  
	public int getNum()
	{
		return num;
	}
	public void setExchange(String exchange)
	{
        this.exchange = exchange;  
    }  
	public String getExchange()
	{
		return exchange;
	}
}
