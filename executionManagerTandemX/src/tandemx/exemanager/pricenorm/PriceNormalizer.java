/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tandemx.exemanager.pricenorm;

import tandemx.model.HistdataPriceDay;

import java.util.List;

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

    /**
     * Normalize series
     * @param lstPrices series
     * @param bottom initialization value of elements in series
     * @param minVolumeThreshold threshold for the volume; if the volume is not strictly above it, the normalization value is set to bottom
     */
    public static void normalize(List<HistdataPriceDay> lstPrices, double bottom, double minVolumeThreshold) {
//        abnormal = false;

        if (lstPrices.size() > 0) {
            lstPrices.get(0).setNormalizedPrice(bottom);
        }
        if (lstPrices.size() >= 2) {

            for (int i = 1; i < lstPrices.size(); i++) {
                if (lstPrices.get(i).getVolume() > minVolumeThreshold && lstPrices.get(i - 1).getVolume() >= minVolumeThreshold) {
                    double rentability = ((lstPrices.get(i).getPrice() - lstPrices.get(i - 1).getPrice()) / lstPrices.get(i - 1).getPrice());
                    double normalizedPrice = lstPrices.get(i - 1).getNormalizedPrice() * (1 + rentability);
                    lstPrices.get(i).setNormalizedPrice(normalizedPrice);
//                if(normalizedPrice > normalityThreshold){
//                    abnormal = true;
//                }
                } else {
                    lstPrices.get(i).setNormalizedPrice(bottom);
                }
            }
        }
    }
    
    public static boolean isAbnormal(){
        return abnormal;
    }
//
//    public static void setNormalityThreshold(double newNormalityThreshold) {
//        normalityThreshold = newNormalityThreshold;
//    }
}
