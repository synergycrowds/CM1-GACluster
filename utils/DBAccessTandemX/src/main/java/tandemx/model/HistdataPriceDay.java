package tandemx.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "HISTDATA_PRICE_DAY")
public class HistdataPriceDay {
    @Id
    @Column(name = "histdata_price_day_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "exchange_id")
    private Integer exchangeId;

    @Id
    @Column(name = "currency_pair_id")
    private Integer currencyPairId;

    @Column(name = "timestamp")
    private LocalDate timestamp;

    @Column(name = "open")
    private Double openPrice;

    @Column(name = "high")
    private Double highPrice;

    @Column(name = "low")
    private Double lowPrice;

    @Column(name = "close")
    private Double closePrice;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "volumefrom")
    private Double volumeFrom;

    @Column(name = "volumeto")
    private Double volumeTo;

    public HistdataPriceDay() {
    }

    public HistdataPriceDay(Integer exchangeId, Integer currencyPairId, LocalDate timestamp, Double openPrice, Double highPrice, Double lowPrice, Double closePrice, Double volume, Double volumeFrom, Double volumeTo) {
        this.exchangeId = exchangeId;
        this.currencyPairId = currencyPairId;
        this.timestamp = timestamp;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.closePrice = closePrice;
        this.volume = volume;
        this.volumeFrom = volumeFrom;
        this.volumeTo = volumeTo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Integer exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Integer getCurrencyPairId() {
        return currencyPairId;
    }

    public void setCurrencyPairId(Integer currencyPairId) {
        this.currencyPairId = currencyPairId;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getVolumeFrom() {
        return volumeFrom;
    }

    public void setVolumeFrom(Double volumeFrom) {
        this.volumeFrom = volumeFrom;
    }

    public Double getVolumeTo() {
        return volumeTo;
    }

    public void setVolumeTo(Double volumeTo) {
        this.volumeTo = volumeTo;
    }
}
