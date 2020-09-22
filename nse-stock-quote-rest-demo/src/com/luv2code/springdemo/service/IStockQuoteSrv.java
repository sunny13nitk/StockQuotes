package com.luv2code.springdemo.service;

import com.luv2code.springdemo.model.StockQuote;

public interface IStockQuoteSrv
{

	public StockQuote getStockQuote(String nseSymbol);

	public StockQuote getStockQuote();;

}
