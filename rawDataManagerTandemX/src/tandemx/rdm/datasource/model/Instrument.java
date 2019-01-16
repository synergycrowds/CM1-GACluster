package tandemx.rdm.datasource.model;

public class Instrument {
    private String exchangeCode;
    private String baseAsset;
    private String quoteAsset;
    private Long tradeStartTimestamp;
    private Long tradeEndTime;

    public Instrument(String exchangeCode, String baseAsset, String quoteAsset, Long tradeStartTimestamp, Long tradeEndTime) {
        this.exchangeCode = exchangeCode;
        this.baseAsset = baseAsset;
        this.quoteAsset = quoteAsset;
        this.tradeStartTimestamp = tradeStartTimestamp;
        this.tradeEndTime = tradeEndTime;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public String getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(String baseAsset) {
        this.baseAsset = baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    public void setQuoteAsset(String quoteAsset) {
        this.quoteAsset = quoteAsset;
    }

    public Long getTradeStartTimestamp() {
        return tradeStartTimestamp;
    }

    public void setTradeStartTimestamp(Long tradeStartTimestamp) {
        this.tradeStartTimestamp = tradeStartTimestamp;
    }

    public Long getTradeEndTime() {
        return tradeEndTime;
    }

    public void setTradeEndTime(Long tradeEndTime) {
        this.tradeEndTime = tradeEndTime;
    }
}
