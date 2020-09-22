package com.luv2code.springdemo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "Global Quote")
public class StockQuote
{
	@JsonProperty("01. symbol")
	private String	symbol;
	@JsonProperty("02. open")
	private double	open;
	@JsonProperty("03. high")
	private double	high;
	@JsonProperty("04. low")
	private double	low;
	@JsonProperty("05. price")
	private double	price;
	@JsonProperty("06. volume")
	private long	volume;
	@JsonProperty("07. latest trading day")
	private Date	latestTradingDay;
	@JsonProperty("08. previous close")
	private double	prevClose;
	@JsonProperty("09. change")
	private double	change;
	@JsonProperty("10. change percent")
	private double	changePercent;

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public double getOpen()
	{
		return open;
	}

	public void setOpen(double open)
	{
		this.open = open;
	}

	public double getHigh()
	{
		return high;
	}

	public void setHigh(double high)
	{
		this.high = high;
	}

	public double getLow()
	{
		return low;
	}

	public void setLow(double low)
	{
		this.low = low;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public long getVolume()
	{
		return volume;
	}

	public void setVolume(long volume)
	{
		this.volume = volume;
	}

	public Date getLatestTradingDay()
	{
		return latestTradingDay;
	}

	public void setLatestTradingDay(Date latestTradingDay)
	{
		this.latestTradingDay = latestTradingDay;
	}

	public double getPrevClose()
	{
		return prevClose;
	}

	public void setPrevClose(double prevClose)
	{
		this.prevClose = prevClose;
	}

	public double getChange()
	{
		return change;
	}

	public void setChange(double change)
	{
		this.change = change;
	}

	public double getChangePercent()
	{
		return changePercent;
	}

	public void setChangePercent(double changePercent)
	{
		this.changePercent = changePercent;
	}

	public StockQuote()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public StockQuote(String symbol, double open, double high, double low, double price,
	          long volume, Date latestTradingDay, double prevClose, double change,
	          double changePercent)
	{
		super();
		this.symbol = symbol;
		this.open = open;
		this.high = high;
		this.low = low;
		this.price = price;
		this.volume = volume;
		this.latestTradingDay = latestTradingDay;
		this.prevClose = prevClose;
		this.change = change;
		this.changePercent = changePercent;
	}

}
