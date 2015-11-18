package com.jp.supersimple.stock.service;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jp.supersimple.stock.api.StockNotFoundException;
import com.jp.supersimple.stock.api.StockService;
import com.jp.supersimple.stock.db.StockCacheService;
import com.jp.supersimple.stock.entity.Indicator;
import com.jp.supersimple.stock.entity.Stock;
import com.jp.supersimple.stock.entity.StockType;
import com.jp.supersimple.stock.entity.Trade;

public class StockServiceImpl implements StockService {
	/** The logger. */
	private static final Logger LOGGER = Logger
			.getLogger(StockServiceImpl.class.getName());
	private final StockCacheService stockCacheService;

	public StockServiceImpl() {
		super();
		stockCacheService = StockCacheService.getInstance();
	}

	@Override
	public double calculateDividendYield(final Stock stock, final Double price) {
		double dividendYield = -1;
		isIllegalArgument(stock, price);
		if (StockType.COMMON == stock.getStockType()) {
			dividendYield = (stock.getLastDividend() / price);
		} else if (StockType.PREFERRED == stock.getStockType()) {
			dividendYield = (stock.getFixedDividend() * stock.getParValue())
					/ price;
		}
		LOGGER.log(Level.INFO, "calculated DividendYield:" + dividendYield
				+ ", Stock:" + stock.getStockSymbol() + ",price:" + price);
		return dividendYield;
	}

	private void isIllegalArgument(final Stock stock, final Double price) {
		if ((stock == null) || (price <= 0)) {
			throw new IllegalArgumentException("Invalid Inputs");
		}
	}

	@Override
	public double calculatePERatio(final Stock stock, final Double price) {
		double peRatio = -1;
		isIllegalArgument(stock);
		final double lastDividend = stock.getLastDividend();
		if (lastDividend > 0) {
			peRatio = price / lastDividend;
		}
		LOGGER.log(Level.INFO, "calculated PERatio:" + peRatio + ", Stock:"
				+ stock.getStockSymbol() + ",price:" + price);
		return peRatio;
	}

	private void isIllegalArgument(final Stock stock) {
		if (stock == null) {
			throw new IllegalArgumentException("Invalid Inputs");
		}
	}

	@Override
	public Double calculateVolumeWeightedStockPrice(final Stock stock,
			final Long timeRange) {
		Double volumeWeightedStockPrice = -1d;
		final List<Trade> tradeList = stockCacheService.getTrades(stock);
		Double sumOfProductOfQuanityAndTradedPrice = 0d;
		long sumOfQuantity = 0;
		isIllegalArgument(stock);
		if (tradeList == null) {
			LOGGER.log(Level.WARNING, "No trade found for stock");
			return 0d;
		}
		for (final Trade trade : tradeList) {
			final Long timestamp = trade.getTimestamp();
			if (isTimestampWithinTimeRange(timestamp, timeRange)) {
				sumOfQuantity = sumOfQuantity + trade.getQuantity();
				final double productOfQuanityAndTradedPrice = trade
						.getQuantity() * trade.getPrice();
				sumOfProductOfQuanityAndTradedPrice = sumOfProductOfQuanityAndTradedPrice
						+ productOfQuanityAndTradedPrice;
			}
		}
		volumeWeightedStockPrice = sumOfProductOfQuanityAndTradedPrice
				/ sumOfQuantity;
		LOGGER.log(
				Level.INFO,
				"calculating VolumeWeightedStockPrice:"
						+ volumeWeightedStockPrice + ", Stock:"
						+ stock.getStockSymbol() + ",timeRange:" + timeRange);
		return volumeWeightedStockPrice;
	}

	private boolean isTimestampWithinTimeRange(final Long timestamp,
			final Long timeRange) {
		final long currentTime = System.currentTimeMillis();
		long rangeTime = 0;
		if (timeRange > 0) {
			rangeTime = currentTime - timeRange;
		}
		return (timestamp <= currentTime) && (timestamp >= rangeTime);
	}

	@Override
	public Double calculateGBCEAllShareIndex() {
		LOGGER.log(Level.INFO, "calculating GBCEAllShareIndex for Stocks");
		double product = 1.0;
		final Map<String, Stock> stocks = stockCacheService.getStocks();
		for (final Stock stock : stocks.values()) {
			final double volumeWightedStockPrice = calculateVolumeWeightedStockPrice(
					stock, -1l);
			product = product * volumeWightedStockPrice;
		}
		final double geoMean = Math.pow(product, 1.0 / stocks.size());
		LOGGER.log(Level.INFO, "calculating GBCEAllShareIndex for Stocks:"
				+ geoMean);
		return geoMean;
	}

	@Override
	public void recordTrade(final Stock stock, final Long timestamp,
			final int quantity, final Indicator indicator, final Double price) {
		if (!stockCacheService.isStockRegistered(stock)) {
			throw new StockNotFoundException("stock is not registered");
		}

		LOGGER.log(Level.INFO, "recording Trade for these entries-stock:"
				+ stock.getStockSymbol() + " timestamp:" + timestamp,
				" quantity:" + quantity + " indicator:" + indicator.name());
		final Trade trade = new Trade(timestamp, quantity, indicator, price);
		stockCacheService.addTrade(stock, trade);
		LOGGER.log(Level.INFO, "Trade recorded");
	}
}
