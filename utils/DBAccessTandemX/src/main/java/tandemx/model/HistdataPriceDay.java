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

    @Column(name = "currency_pair_id")
    private Integer currencyPairId;

    @Column(name = "timestamp")
    private LocalDate timestamp;

//    @Column(name = "open")
//    private Double openPrice;
//
//    @Column(name = "high")
//    private Double highPrice;
//
//    @Column(name = "low")
//    private Double lowPrice;
//
//    @Column(name = "close")
//    private Double closePrice;

    @Column(name = "price")
    private Double price;

    @Column(name = "volume")
    private Double volume;

//    @Column(name = "volumefrom")
//    private Double volumeFrom;
//
//    @Column(name = "volumeto")
//    private Double volumeTo;

    public HistdataPriceDay() {
    }

    public HistdataPriceDay(Integer exchangeId, Integer currencyPairId, LocalDate timestamp, Double price, Double volume) {
        this.exchangeId = exchangeId;
        this.currencyPairId = currencyPairId;
        this.timestamp = timestamp;
        this.price = price;
        this.volume = volume;
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

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
