package com.luv2code.springdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luv2code.springdemo.model.StockQuote;
import com.luv2code.springdemo.service.IStockQuoteSrv;

@Controller
@RequestMapping("/quote")
public class StockQuoteController
{
	@Autowired
	private IStockQuoteSrv stockQSrv;

	@GetMapping("/quote/{nseSymbol}")
	public String getStockQuote(@PathVariable("nseSymbol") String nseSymbol, Model model)
	{

		if (stockQSrv != null)
		{
			if (nseSymbol != null)
			{
				StockQuote stQuote = stockQSrv.getStockQuote(nseSymbol);
				model.addAttribute("quote", stQuote);
			}
		}
		return "stock-quote";
	}

	@GetMapping("/getquote")
	public String getStockQuote(Model model)
	{

		if (stockQSrv != null)
		{
			StockQuote stQuote = stockQSrv.getStockQuote();
			model.addAttribute("quote", stQuote);

		}
		return "stock-quote";
	}

}
