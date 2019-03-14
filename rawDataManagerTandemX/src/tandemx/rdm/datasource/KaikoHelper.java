package tandemx.rdm.datasource;

import tandemx.db.util.Constants;
import tandemx.model.*;
import tandemx.rdm.datasource.model.CountOhlcvVwap;
import tandemx.rdm.datasource.model.Instrument;
import tandemx.rdm.util.Pair;

import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KaikoHelper {
    private String apiKey;

    private static String BASE_URL_NO_AUTH = "https://reference-data-api.kaiko.io/v1/";
    private static String BASE_URL_WITH_AUTH = "https://eu.market-api.kaiko.io/v1/";

    public KaikoHelper(String apiKey) {
        this.apiKey = apiKey;
    }


    /**
     * Read a response from a http connection and return it as a string
     * @param connection connection from which to read
     * @return response text
     * @throws IOException if there is an error when reading from the connection
     */
    private String readResponseText(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append(System.lineSeparator());
        }
        in.close();
        return content.toString();
    }

    /**
     * Read a response JSON from a http connection and return it
     * @param connection connection from which to read
     * @return JSON
     * @throws IOException if there are problems while reading from the connection
     */
    private JsonObject readResponseJson(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        JsonReader jsonReader = Json.createReader(in);
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        in.close();
        return jsonObject;
    }

    /**
     * Get the list of all exchanges supported by Kaiko
     * @return list of exchanges
     * @throws Exception if there is a problem in obtaining the data
     */
    public List<Exchange> getExchanges() throws Exception{
        URL url = null;
        String exceptionMsgPrefix = "Error while obtaining exchanges: ";
        try {
            url = new URL(BASE_URL_NO_AUTH + "exchanges");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
//            con.setRequestProperty("Accept-Encoding", "gzip");

            JsonObject jsonObject = readResponseJson(con);
            con.disconnect();
            JsonArray exchangesJsonArray = jsonObject.getJsonArray("data");
            List<Exchange> exchanges = new ArrayList<>();
            for (int i = 0; i < exchangesJsonArray.size(); i++) {
                JsonObject exchangeJsonObj = exchangesJsonArray.getJsonObject(i);
                exchanges.add(new Exchange(exchangeJsonObj.getString("code"), exchangeJsonObj.getString("name")));
            }
            return exchanges;

        } catch (IOException e) {
            throw new Exception(exceptionMsgPrefix + e.getMessage());
        }
    }

    /**
     * Get a list of all symbols supported by Kaiko. Kaiko assets transformed into symbols.
     * @param currencyTypes mapping between a currency type name and its ID
     * @return list of symbols
     * @throws Exception if there is a problem in obtaining the data
     */
    public List<Symbol> getAssets(Map<String, Integer> currencyTypes) throws Exception{
        URL url = null;
        String exceptionMsgPrefix = "Error while obtaining assets: ";
        Map<String, String> ctKaikoToTX = new HashMap<>();
        ctKaikoToTX.put("cryptocurrency", Constants.CURRENCY_TYPE_DIGITAL);
        ctKaikoToTX.put("fiat", Constants.CURRENCY_TYPE_FIAT);
        try {
            url = new URL(BASE_URL_NO_AUTH + "assets");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
//            con.setRequestProperty("Accept-Encoding", "gzip");

            JsonObject jsonObject = readResponseJson(con);
            con.disconnect();
            JsonArray assetsJsonArray = jsonObject.getJsonArray("data");
            List<Symbol> symbols = new ArrayList<>();
            for (int i = 0; i < assetsJsonArray.size(); i++) {
                JsonObject assetJsonObj = assetsJsonArray.getJsonObject(i);
                symbols.add(new Symbol(assetJsonObj.getString("code"),
                        currencyTypes.get(ctKaikoToTX.get(assetJsonObj.getString("asset_class"))),
                        assetJsonObj.getString("name"), null));
            }
            return symbols;

        } catch (IOException e) {
            throw new Exception(exceptionMsgPrefix + e.getMessage());
        }
    }

    /**
     * Get a list of instruments supported by Kaiko
     * @return list of instruments
     * @throws Exception if there is a problem in obtaining the data
     */
    public List<Instrument> getInstruments() throws Exception {
        URL url = null;
        String exceptionMsgPrefix = "Error while obtaining instruments: ";
        try {
            url = new URL(BASE_URL_NO_AUTH + "instruments");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
//            con.setRequestProperty("Accept-Encoding", "gzip");

            JsonObject jsonObject = readResponseJson(con);
            con.disconnect();
            JsonArray instrumentsJsonArray = jsonObject.getJsonArray("data");
            List<Instrument> instruments = new ArrayList<>();
            for (int i = 0; i < instrumentsJsonArray.size(); i++) {
                JsonObject instrumentsJsonObj = instrumentsJsonArray.getJsonObject(i);

                if (!instrumentsJsonObj.getString("class").equals("spot")) {
                    // TODO: 2/7/2019 check what "future" class means (maybe other classes?)
                    continue;
                }
                Long tradeStartTimestamp = null;
                if (!instrumentsJsonObj.isNull("trade_start_timestamp")) {
                    tradeStartTimestamp = instrumentsJsonObj.getJsonNumber("trade_start_timestamp").longValueExact();
                }
                Long tradeEndTimestamp = null;
                if (!instrumentsJsonObj.isNull("trade_end_timestamp")) {
                    tradeEndTimestamp = instrumentsJsonObj.getJsonNumber("trade_end_timestamp").longValueExact();
                }
                instruments.add(new Instrument(instrumentsJsonObj.getString("exchange_code"),
                        instrumentsJsonObj.getString("base_asset"), instrumentsJsonObj.getString("quote_asset"),
                        tradeStartTimestamp, tradeEndTimestamp));
            }
            return instruments;

        } catch (IOException e) {
            throw new Exception(exceptionMsgPrefix + e.getMessage());
        }
    }

    /**
     * Get the list of CountOhclVwap from Kaiko. Such an instance encodes aggregated data about prices.
     * @param exchangeCode Kaiko code for the exchange from which the data should be provided
     * @param baseAsset Kaiko base asset name
     * @param quoteAsset Kaiko quote asset name
     * @param start beginning of the period of time for which the data is aggregated
     * @param end end of the period of time for which the data is aggregated
     * @return list of aggregated data instances
     * @throws Exception  if there is a problem in obtaining the data
     */
    public CountOhlcvVwap getCountOhlcvVwapFromExchange(String exchangeCode, String baseAsset, String quoteAsset,
                                                        LocalDateTime start, LocalDateTime end) throws Exception {
        URL url = null;
        String exceptionMsgPrefix = "Error while obtaining data for (" + exchangeCode + ", " + baseAsset + ", "
                + quoteAsset + "): ";
        try {
            String currencyPairCode = baseAsset + "-" + quoteAsset;
            String startDateStr = start.format(DateTimeFormatter.ISO_DATE_TIME);
            String endDateStr = end.format(DateTimeFormatter.ISO_DATE_TIME);
            url = new URL(BASE_URL_WITH_AUTH + "data/trades.v1/exchanges/" + exchangeCode + "/spot/"
                    + currencyPairCode + "/aggregations/count_ohlcv_vwap?start_time=" + startDateStr + "Z&end_time="
                    + endDateStr + "Z");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
//            con.setRequestProperty("Accept-Encoding", "gzip");
            con.setRequestProperty("X-Api-Key", apiKey);

            JsonObject jsonObject = readResponseJson(con);
            JsonArray jsonArray = jsonObject.getJsonArray("data");
            CountOhlcvVwap result;
            if (jsonArray.size() > 0) {
                JsonObject dataObj = jsonArray.getJsonObject(0);
                result = new CountOhlcvVwap(dataObj.getJsonNumber("timestamp").longValueExact(),
                        dataObj.getInt("count"),
                        Double.parseDouble(dataObj.getString("open")),
                        Double.parseDouble(dataObj.getString("high")),
                        Double.parseDouble(dataObj.getString("low")),
                        Double.parseDouble(dataObj.getString("close")),
                        Double.parseDouble(dataObj.getString("volume")),
                        Double.parseDouble(dataObj.getString("price")));
            } else {
                result = null;
            }
            con.disconnect();
            return result;
        } catch (IOException e) {
            throw new Exception(exceptionMsgPrefix + e.getMessage());
        }
    }

}
