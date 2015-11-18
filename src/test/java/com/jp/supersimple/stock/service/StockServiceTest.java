package com.jp.supersimple.stock.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jp.supersimple.stock.api.StockNotFoundException;
import com.jp.supersimple.stock.api.StockService;
import com.jp.supersimple.stock.db.StockCacheService;
import com.jp.supersimple.stock.entity.Indicator;
import com.jp.supersimple.stock.entity.Stock;
import com.jp.supersimple.stock.entity.StockType;
import com.jp.supersimple.stock.entity.Trade;

public class StockServiceTest {
	private StockService stockService;
	private StockCacheService stockCacheService;
	private static final long FIFTEEN_MINUTES = 900000;
	private static final long FIVE_MINUTES = 300000;
	private static final long ONE_MINUTES = 60000;
	private static final long THIRTY_MINUTES = 1800000;

	@Before
	public void setUp() {
		stockService = new StockServiceImpl();
		stockCacheService = StockCacheService.getInstance();
		populateStocks();
	}

	@Test
	public void testGetInstance() {
		Assert.assertNotNull(
				"Not able to create instance of StockCacheService",
				stockCacheService);
		Assert.assertNotNull("Not able to create instance of StockService",
				stockService);
	}

	@Test
	public void testCalculateDividendYield_Common() {
		final Stock stock = stockCacheService.getStock("TEA");
		final Double price = 43424d;
		final double dividendYield = stockService.calculateDividendYield(stock,
				price);
		Assert.assertEquals("dividendYield is not correct", 0,
				Double.compare(0d, dividendYield));
	}

	@Test
	public void testCalculateDividendYield_Preferred() {
		final Stock stock = stockCacheService.getStock("GIN");
		final Double price = 5d;
		final double dividendYield = stockService.calculateDividendYield(stock,
				price);
		Assert.assertEquals("dividendYield is not correct", 0,
				Double.compare(40d, dividendYield));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateDividendYield_Stock_Null() {
		final Double price = 5d;
		stockService.calculateDividendYield(null, price);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateDividendYield_price_zero() {
		final Stock stock = stockCacheService.getStock("GIN");
		stockService.calculateDividendYield(stock, 0d);
	}

	@Test
	public void testCalculatePERatio() {
		final Stock stock = stockCacheService.getStock("GIN");
		final Double price = 50d;
		final double peRatio = stockService.calculatePERatio(stock, price);
		Assert.assertEquals("peRatio is not correct", 0,
				Double.compare(6.25d, peRatio));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculatePERatio_StockNull() {
		final Double price = 50d;
		stockService.calculatePERatio(null, price);
	}

	@Test
	public void testCalculatePERatio_price_zero() {
		final Stock stock = stockCacheService.getStock("GIN");
		final Double price = 0d;
		final double peRatio = stockService.calculatePERatio(stock, price);
		Assert.assertEquals("peRatio is not correct", 0,
				Double.compare(0d, peRatio));
	}

	@Test
	public void testCalculatePERatio_dividend_zero() {
		final Stock stock = stockCacheService.getStock("TEA");
		final Double price = 40d;
		final double peRatio = stockService.calculatePERatio(stock, price);
		Assert.assertEquals("peRatio is not correct", 0,
				Double.compare(-1d, peRatio));
	}

	@Test
	public void tesRecordTrade() {
		final Stock stock = stockCacheService.getStock("TEA");
		stockCacheService.cleanTrade(stock);
		stockService.recordTrade(stock, System.currentTimeMillis(), 40,
				Indicator.BUY, 7000d);
		final List<Trade> tradeList = stockCacheService.getTrades(stock);
		Assert.assertEquals("Trades did not get recorded.", 1, tradeList.size());
		final Trade trade = tradeList.get(0);
		Assert.assertEquals("Price is not correct", 0,
				Double.compare(7000d, trade.getPrice()));
		Assert.assertEquals("Quantity is not correct", 40, trade.getQuantity());
		Assert.assertEquals("Indicator is not correct", Indicator.BUY,
				trade.getIndicator());
	}

	@Test(expected = StockNotFoundException.class)
	public void tesRecordTrade_unregidteredStock() {
		final Stock stock = new Stock("ABC", StockType.COMMON, 4344d, 23d, 23d);
		stockCacheService.cleanTrade(stock);
		stockService.recordTrade(stock, System.currentTimeMillis(), 40,
				Indicator.BUY, 7000d);
	}

	@Test
	public void testCalculateVolumeWeightedStockPrice() {
		final Stock stock = stockCacheService.getStock("ALE");
		recordingTrades();
		final Double volumeWeightedStockPrice = stockService
				.calculateVolumeWeightedStockPrice(stock, FIFTEEN_MINUTES);
		Assert.assertEquals("volumeWeightedStockPrice is not correct", 0,
				Double.compare(3500.0d, volumeWeightedStockPrice));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCalculateVolumeWeightedStockPrice_stock_null() {
		recordingTrades();
		stockService.calculateVolumeWeightedStockPrice(null, FIFTEEN_MINUTES);
	}

	@Test
	public void testcalculateGBCEAllShareIndex() {
		recordingTrades();
		final Double gbceAllShareIndex = stockService
				.calculateGBCEAllShareIndex();
		Assert.assertEquals("volumeWeightedStockPrice is not correct", 0,
				Double.compare(2467.532467532468d, gbceAllShareIndex));
	}

	private void populateStocks() {
		stockCacheService.addStock(new Stock("TEA", StockType.COMMON, 0, 0f,
				100));
		stockCacheService.addStock(new Stock("POP", StockType.COMMON, 8, 0f,
				100));
		stockCacheService.addStock(new Stock("ALE", StockType.COMMON, 23, 0f,
				60));
		stockCacheService.addStock(new Stock("GIN", StockType.PREFERRED, 8, 2f,
				100));
		stockCacheService.addStock(new Stock("JOE", StockType.COMMON, 13, 0f,
				250));
	}

	private void recordingTrades() {
		for (final Stock stock : stockCacheService.getStocks().values()) {
			stockCacheService.cleanTrade(stock);
			stockService.recordTrade(stock, System.currentTimeMillis(), 40,
					Indicator.BUY, 1000d);
			stockService.recordTrade(stock, System.currentTimeMillis()
					- FIFTEEN_MINUTES, 560, Indicator.BUY, 2000d);
			stockService.recordTrade(stock, System.currentTimeMillis()
					- FIVE_MINUTES, 10, Indicator.SELL, 3000d);
			stockService.recordTrade(stock, System.currentTimeMillis()
					- THIRTY_MINUTES, 90, Indicator.SELL, 4000d);
			stockService.recordTrade(stock, System.currentTimeMillis()
					- ONE_MINUTES, 70, Indicator.BUY, 5000d);
		}
	}
}
