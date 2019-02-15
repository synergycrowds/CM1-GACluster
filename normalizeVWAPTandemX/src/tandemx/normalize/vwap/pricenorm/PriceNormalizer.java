/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tandemx.normalize.vwap.pricenorm;

import java.util.ArrayList;
import util.dataNormalizer.db.entity.ECPNormalizedPrice;

/**
 *
 * @author Mircea
 */
public class PriceNormalizer {

    private static boolean abnormal;
    private static double normalityThreshold;
    
    static {
        /**
         * Flag to indicate the exceeding of the normality threshold. True when the data series has exceeded the threshold, false otherwise.
         */
        abnormal = false;
        /**
         * A threshold for limiting input data to data series that have lower growth rates than specified threshold value.
         * This is an outliers filter.
         * The client checks for exceeding the threshold, and if the case, can ignore the data concerned data series.
         */
        normalityThreshold = 800;
    }

    public static void normalize(ArrayList<ECPNormalizedPrice> lstPrices, int bottom) {
        abnormal = false;
        
        if (lstPrices.size() >= 2) {
            lstPrices.get(0).setNormalizedPrice(bottom);

            for (int i = 1; i < lstPrices.size(); i++) {
                double rentability = ((lstPrices.get(i).getPrice() - lstPrices.get(i - 1).getPrice()) / lstPrices.get(i - 1).getPrice());
                double normalizedPrice = lstPrices.get(i - 1).getNormalizedPrice() * (1 + rentability);
                lstPrices.get(i).setNormalizedPrice(normalizedPrice);
                if(normalizedPrice > normalityThreshold){
                    abnormal = true;
                }
            }
        }
    }
    
    public static boolean isAbnormal(){
        return abnormal;
    }
}
