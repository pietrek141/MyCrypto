package piotrmroczkowski.mycrypto;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Cryptocurrency {


    public static List<Cryptocurrency> everyCoinList = new ArrayList<Cryptocurrency>();
    public static List<Cryptocurrency> myCoinList = new ArrayList<Cryptocurrency>();


    public Cryptocurrency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;

    }


    public static List<Cryptocurrency> fromJson(JSONObject jsonObject) {

        try {

            JSONObject jO = jsonObject.getJSONObject("Data");
            Iterator<?> keys = jO.keys();
            everyCoinList.clear();

            while (keys.hasNext()) {

                Object key = keys.next();
                JSONObject value = jO.getJSONObject((String) key);
                String name = value.getString("CoinName");
                String symbol = value.getString("Symbol");

                everyCoinList.add(new Cryptocurrency(name, symbol));
                Log.d("Name", name);
                Log.d("Symbol", symbol);

            }
            Log.d("Every Coin List Size", String.valueOf(everyCoinList.size()));
            return everyCoinList;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map pricesFromJson(List<String> listOfCoins, JSONObject jsonObject){

        Map<String , String> coinAndPrices = new HashMap<>();
        try{
            for (String listItem:listOfCoins) {

                JSONObject jO = jsonObject.getJSONObject(listItem);
                String price = jO.getString("USD");
                coinAndPrices.put(listItem, price);
                Log.d(listItem, price);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coinAndPrices;
    }

    private String name;
    private String symbol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}


