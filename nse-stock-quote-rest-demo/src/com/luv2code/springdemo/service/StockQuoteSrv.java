package com.luv2code.springdemo.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.luv2code.springdemo.model.StockQuote;

@Service
public class StockQuoteSrv implements IStockQuoteSrv
{

	private RestTemplate	restTemplate;

	private String			nseRestUrl1;

	private String			nseRestUrl2;

	private Logger			logger	= Logger.getLogger(getClass().getName());

	// Rest Template Bean Initialization
	@Autowired
	public StockQuoteSrv(RestTemplate theRestTemplate, @Value("${nse.url.part1}") String nseUrl1,
	          @Value("${nse.url.part2}") String nseUrl2)
	{
		restTemplate = theRestTemplate;
		nseRestUrl1 = nseUrl1;
		nseRestUrl2 = nseUrl2;

		logger.info("Loaded property: nse.rest.url=" + nseUrl1 + nseUrl2);
	}

	@Override
	public StockQuote getStockQuote(String nseSymbol)
	{
		logger.info("in getStockQuote(): Calling REST API for NSE Symbol" + nseSymbol);
		String urlFull = nseRestUrl1 + nseSymbol + nseRestUrl2;
		logger.info("Complete Url : " + urlFull);

		// make REST call
		ResponseEntity<StockQuote> responseEntity = restTemplate.exchange(urlFull, HttpMethod.GET,
		          null, new ParameterizedTypeReference<StockQuote>()
		          {
		          });

		// get the list of customers from response
		StockQuote quote = responseEntity.getBody();

		logger.info("Quote : " + quote);

		return quote;
	}

	@Override
	public StockQuote getStockQuote()
	{
		logger.info("in getStockQuote(): Calling REST API for NSE Symbol" + "NSE:MINDAIND");
		String urlFull = nseRestUrl1 + "NSE:MINDAIND" + nseRestUrl2;
		logger.info("Complete Url : " + urlFull);

		// make REST call
		ResponseEntity<StockQuote> responseEntity = restTemplate.exchange(urlFull, HttpMethod.GET,
		          null, new ParameterizedTypeReference<StockQuote>()
		          {
		          });

		// get the list of customers from response
		StockQuote quote = responseEntity.getBody();

		logger.info("Quote : " + quote);

		return quote;
	}

}
