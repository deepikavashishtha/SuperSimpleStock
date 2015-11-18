package com.jp.supersimple.stock.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.jp.supersimple.stock.entity.Stock;
import com.jp.supersimple.stock.entity.Trade;

/**
 * StockCacheService: This class is responsible for maintaining in-memory
 * records for Stocks and Trades.This is a singlton class. Here Stocks are saved
 * in a list. Where Trades are saved in a Map of stock and List of Trades; Which
 * means if there are multiple trades per stock then list will be created to
 * save them.
 * 
 * @author DVashishta
 * 
 */
public class StockCacheService {

	private static final Map<String, Stock> STOCKS = new HashMap<>();
	private static final Map<Stock, List<Trade>> TRADES = new HashMap<Stock, List<Trade>>();
	/** The logger. */
	private static final Logger LOGGER = Logger
			.getLogger(StockCacheService.class.getName());
	private static StockCacheService instance = null;

	private StockCacheService() {
		super();
	}

	/**
	 * Returns instance of StockCacheService.
	 * 
	 * @return
	 */
	public static StockCacheService getInstance() {
		if (instance == null) {
			instance = new StockCacheService();
		}
		return instance;
	}

	/**
	 * Adding stock
	 * 
	 * @param stock
	 */
	public void addStock(final Stock stock) {
		// Just overriding value in a map , if calling again, Assumption is that
		// each stock will have unique key symbol
		STOCKS.put(stock.getStockSymbol(), stock);
	}

	/**
	 * returns stock.
	 * 
	 * @return
	 */
	public Stock getStock(final String stockSymbol) {
		return STOCKS.get(stockSymbol);
	}

	/**
	 * Gives List of stocks.
	 * 
	 * @return
	 */
	public Map<String, Stock> getStocks() {
		return STOCKS;
	}

	public boolean isStockRegistered(final Stock stock) {
		return (STOCKS.get(stock.getStockSymbol()) != null);
	}

	/**
	 * Gives Trades.
	 * 
	 * @return
	 */
	public List<Trade> getTrades(final Stock stock) {
		return TRADES.get(stock);
	}

	/**
	 * Add a trade.
	 * 
	 * @param stock
	 * @param trade
	 * @param tradeList
	 */
	public void addTrade(final Stock stock, final Trade trade) {
		List<Trade> tradeList = getTrades(stock);
		if (tradeList == null) {
			tradeList = new ArrayList<>();
		}
		tradeList.add(trade);
		TRADES.put(stock, tradeList);
	}

	public void cleanTrade(final Stock stock) {
		TRADES.put(stock, null);
	}
}
