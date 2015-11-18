package com.jp.supersimple.stock.entity;

public class Stock {
	private final String stockSymbol;
	private final StockType stockType;
	private final double lastDividend;
	private final double fixedDividend;
	private final double parValue;

	public Stock(final String stockSymbol, final StockType stockType,
			final double lastDividend, final double fixedDividend,
			final double parValue) {
		super();
		this.stockSymbol = stockSymbol;
		this.stockType = stockType;
		this.lastDividend = lastDividend;
		this.fixedDividend = fixedDividend;
		this.parValue = parValue;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public StockType getStockType() {
		return stockType;
	}

	public double getLastDividend() {
		return lastDividend;
	}

	public double getFixedDividend() {
		return fixedDividend;
	}

	public double getParValue() {
		return parValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(fixedDividend);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lastDividend);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(parValue);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		result = (prime * result)
				+ ((stockSymbol == null) ? 0 : stockSymbol.hashCode());
		result = (prime * result)
				+ ((stockType == null) ? 0 : stockType.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Stock other = (Stock) obj;
		if (Double.doubleToLongBits(fixedDividend) != Double
				.doubleToLongBits(other.fixedDividend)) {
			return false;
		}
		if (Double.doubleToLongBits(lastDividend) != Double
				.doubleToLongBits(other.lastDividend)) {
			return false;
		}
		if (Double.doubleToLongBits(parValue) != Double
				.doubleToLongBits(other.parValue)) {
			return false;
		}
		if (stockSymbol == null) {
			if (other.stockSymbol != null) {
				return false;
			}
		} else if (!stockSymbol.equals(other.stockSymbol)) {
			return false;
		}
		if (stockType != other.stockType) {
			return false;
		}
		return true;
	}

}
