package piotrmroczkowski.mycrypto.api;

import android.database.Cursor;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import piotrmroczkowski.mycrypto.cryptocurrency.Cryptocurrency;
import piotrmroczkowski.mycrypto.repository.EveryCoinRepo;
import piotrmroczkowski.mycrypto.repository.MyCoinRepo;
import piotrmroczkowski.mycrypto.view.ViewManager;

import static piotrmroczkowski.mycrypto.cryptocurrency.Cryptocurrency.everyCoinList;

public class ApiConnection {

    private EveryCoinRepo everyCoinRepo = new EveryCoinRepo();
    private MyCoinRepo myCoinRepo = new MyCoinRepo();
    public static final String URL_COIN_LIST = "https://www.cryptocompare.com/api/data/coinlist/";
    private final String URL_COIN_PRICE = "https://min-api.cryptocompare.com/data/pricemulti?tsyms=PLN,USD&fsyms=";

    public void connectToAPI(String URL_COIN_LIST) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL_COIN_LIST, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Cryptocurrency.parseJson(response);
                everyCoinRepo.insert();

                Log.d("API Connection", "Updated");
                Log.d("EvrCnLst Size Updated", String.valueOf(everyCoinList.size()));
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("API Connection", "Failure");

            }
        });
    }

    public void getCoinPrices(Cursor cursor) {

        final List<String> myCoinSymbolList = new ArrayList<>();

        String currentURL = "";

        if (cursor == null) {
            return;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }

        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            String symbol = cursor.getString(cursor.getColumnIndex("SYMBOL"));
            Log.d("Symbols to API", symbol);
            if (cursor.isFirst()) {
                currentURL = URL_COIN_PRICE + symbol + ",";
                myCoinSymbolList.add(symbol);
            } else {
                currentURL = currentURL + symbol + ",";
                myCoinSymbolList.add(symbol);
            }
            Log.d("URL", currentURL);
            Log.d("myCoinSymbolList", myCoinSymbolList.toString());
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(currentURL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Map<String, String> coinAndPrices;
                coinAndPrices = Cryptocurrency.pricesFromJson(myCoinSymbolList, response);
                myCoinRepo.updateMyCoin(coinAndPrices);
                ViewManager.initMyCryptoView();

                Log.d("API Prices Connection", "Updated");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("API Prices Connection", "Failure");

            }
        });
    }
}
