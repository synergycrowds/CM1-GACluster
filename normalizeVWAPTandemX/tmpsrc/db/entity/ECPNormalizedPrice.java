/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.dataNormalizer.db.entity;

import java.sql.Timestamp;

/**
 * The normalized price for a currency pair.
 * @author Mircea 
 */
public class ECPNormalizedPrice {

    int exchange_id;
    int currency_pair_id;
    Timestamp timestamp;
    double price; // open
    double normalizedPrice;

    public ECPNormalizedPrice(int exchange_id, int currency_pair_id, Timestamp timestamp, double price) {
        this.exchange_id = exchange_id;
        this.currency_pair_id = currency_pair_id;
        this.price = price;
        this.timestamp = timestamp;
    }

    public int getExchange_id() {
        return exchange_id;
    }

    public int getCurrency_pair_id() {
        return currency_pair_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getNormalizedPrice() {
        return normalizedPrice;
    }

    public void setNormalizedPrice(double normalizedPrice) {
        this.normalizedPrice = normalizedPrice;
    }

    @Override
    public String toString() {
        return //super.toString() + " exchange_id: " + exchange_id + " currency_pair_id: " + currency_pair_id + " timestamp: " + timestamp + 
                "; price:" + price + "; normalized price: " + normalizedPrice; 
    }

}
