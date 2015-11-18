package com.jp.supersimple.stock.entity;

public class Trade {
	private final Long timestamp;
	private final int quantity;
	private final Indicator indicator;
	private final double price;

	public Trade(final Long timestamp, final int quantity,
			final Indicator indicator, final double price) {
		super();
		this.timestamp = timestamp;
		this.quantity = quantity;
		this.indicator = indicator;
		this.price = price;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public int getQuantity() {
		return quantity;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public double getPrice() {
		return price;
	}
}
