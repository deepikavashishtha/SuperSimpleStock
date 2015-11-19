# SuperSimpleStock
SuperSimpleStock can be used to calculate stock and trade related statistics.
## Class Diagram

![Alt text](./ClassDiagram.jpg?raw=true "ClassDiagram")


## API Reference
##com.jp.supersimple.stock.api.StockService 
This is Service which can be used to calculate stock and trade related data. This service has methods to calculate DividendYield, PERatio, VolumeWeightedStockPrice and AllShareIndex.
  - calculateDividendYield(stock, price) 
    --Calculates Dividend-Yield.
  - calculatePERatio(stock, price)
    --Calculates PE-Ratio.
  - recordTrade(stock, timestamp, quantity,	indicator, price)
    --Records Trade.
  - calculateVolumeWeightedStockPrice(stock,timeRange)
    --Calculates Volume Weighted Stock Price.
  - calculateGBCEAllShareIndex()
    --Calculates GBCE All Share Index.

##com.jp.supersimple.stock.db.StockCacheService
StockCacheService maintains cache information for stock and trade. Stock is saved in a Map, where stockSymbol is considered Uique for each stock. Trades are stored in a Map of Stock and List of Trades, So if there is multiple occureance of trade for any Stock , the trade will be added in a list. It is singleton class.
  - getInstance() 
    --To get Instance of Stock.
  - addStock(stock)
    --Add a stock in cache.
  - getStock(stockSymbol)
    --Get a stock from Cache.
  - isStockRegistered(stockSymbol)
    --Find out if Stock is already registered.
  - getTrades(stock) 
    --Get Trade list for Stock.
  - addTrade(stock,trade)
    --Add a trade for a Stock.
  - cleanTrade(stock)
    --Clean Trade.

## Tests

There are two test classes: StockServiceTest and StockCacheServiceTest.

## Running Tests

mvn clean install

## Generating JavaDoc

 mvn clean site
