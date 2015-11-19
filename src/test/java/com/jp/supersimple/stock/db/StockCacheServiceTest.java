package com.jp.supersimple.stock.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jp.supersimple.stock.entity.Stock;
import com.jp.supersimple.stock.entity.StockType;

public class StockCacheServiceTest {
	private StockCacheService stockCacheService = null;

	@Before
	public void setUp() {
		stockCacheService = StockCacheService.getInstance();
	}

	@Test
	public void testGetInstance() {
		Assert.assertNotNull(
				"Not able to create instance of StockCacheService",
				stockCacheService);
	}

	@Test
	public void testGetStocks() {
		Assert.assertNotNull(
				"Not able to create instance of StockCacheService",
				stockCacheService);
		populateStocks();
		Assert.assertEquals("List of stocks is not getting initialized", false,
				stockCacheService.getStocks().isEmpty());
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
}
