package tandemx.rdm.datasource;

import tandemx.db.util.Constants;
import tandemx.model.Exchange;
import tandemx.model.Symbol;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

    private JsonObject readResponseJson(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        JsonReader jsonReader = Json.createReader(in);
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        in.close();
        return jsonObject;
    }

    public List<Exchange> getExchanges() throws Exception{
        URL url = null;
        String exceptionMsgPrefix = "Error while obtaining exchanges: ";
        try {
            url = new URL(BASE_URL_NO_AUTH + "exchanges");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
//            con.setRequestProperty("Accept-Encoding", "gzip");

            con.disconnect();
            JsonObject jsonObject = readResponseJson(con);
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

            con.disconnect();
            JsonObject jsonObject = readResponseJson(con);
            JsonArray assetsJsonArray = jsonObject.getJsonArray("data");
            List<Symbol> symbols = new ArrayList<>();
            for (int i = 0; i < assetsJsonArray.size(); i++) {
                JsonObject assetJsonObj = assetsJsonArray.getJsonObject(i);
                symbols.add(new Symbol(assetJsonObj.getString("code"),
                        currencyTypes.get(ctKaikoToTX.get(assetJsonObj.getString("asset_class"))), null));
            }
            return symbols;

        } catch (IOException e) {
            throw new Exception(exceptionMsgPrefix + e.getMessage());
        }
    }

    public void getData() {
        URL url = null;
        try {
            url = new URL(BASE_URL_WITH_AUTH + "data/trades.v1/exchanges/cccagg/spot/btc-usd/aggregations/count_ohlcv_vwap");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
//            con.setRequestProperty("Accept-Encoding", "gzip");
            con.setRequestProperty("X-Api-Key", apiKey);
            int status = con.getResponseCode();
            System.out.println(status);
            if (status == 400) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                System.out.println(content);
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            System.out.println(content);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
