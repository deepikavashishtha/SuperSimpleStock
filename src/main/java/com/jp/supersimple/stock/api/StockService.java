package com.jp.supersimple.stock.api;

import com.jp.supersimple.stock.entity.Indicator;
import com.jp.supersimple.stock.entity.Stock;

/**
 * This is a service which records Trade and calculate statistics like
 * Dividend-Yield, PE-Ratio, Volume-Weighted-StockPrice and GBCE-All Share Index
 * 
 * @author DVashishta
 * 
 */
public interface StockService {
	/**
	 * Calculates Dividend-Yield
	 * 
	 * @param stock
	 * @param price
	 * @return
	 */
	public double calculateDividendYield(final Stock stock, final Double price);

	/**
	 * Calculates PE-Ratio
	 * 
	 * @param stock
	 * @param price
	 * @return
	 */
	public double calculatePERatio(Stock stock, Double price);

	/**
	 * Records Trade
	 * 
	 * @param stock
	 * @param timestamp
	 * @param quantity
	 * @param indicator
	 * @param price
	 * @return
	 */
	public void recordTrade(Stock stock, Long timestamp, int quantity,
			Indicator indicator, Double price);

	/**
	 * Calculates Volume Weighted Stock Price
	 * 
	 * @param stock
	 * @param trades
	 * @return
	 */
	public Double calculateVolumeWeightedStockPrice(Stock stock, Long timeRange);

	/**
	 * Calculates GBCE All Share Index
	 * 
	 * @param stockList
	 * @param trades
	 * @return
	 */
	public Double calculateGBCEAllShareIndex();
}
