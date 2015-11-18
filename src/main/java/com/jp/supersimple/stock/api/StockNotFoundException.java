package com.jp.supersimple.stock.api;

public class StockNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 546436456456541L;

	public StockNotFoundException(final String message) {
		super(message);
	}
}
