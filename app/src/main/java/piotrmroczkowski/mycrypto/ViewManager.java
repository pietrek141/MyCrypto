package piotrmroczkowski.mycrypto;

import android.database.Cursor;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ViewManager {

    public static Cursor cursor;
    static ListView everyCoinListView;
    static ListView myCoinListView;
    public static EveryCoinRepo everyCoinRepo = new EveryCoinRepo();
    public static MyCoinRepo myCoinRepo = new MyCoinRepo();
    public static ApiConnection apiConnection = new ApiConnection();

    public static void updateMyCrypto() {

        initMyCryptoView();
        getMyCoinPrices();
    }

    public static Cursor showEveryCoinList(String query) {
        cursor = everyCoinRepo.getCryptoCurrencyListByKeyword(query);
        ListAdapter listAdapter = new EveryCoinCursorAdapter(MyApplication.getAppContext(), cursor);
        everyCoinListView.setAdapter(listAdapter);
        return cursor;
    }

    public static void initMyCryptoView() {

        cursor = myCoinRepo.getCryptoCurrencyList();
        ListAdapter listAdapter = new MyCoinCursorAdapter(MyApplication.getAppContext(), cursor);
        myCoinListView.setAdapter(listAdapter);
    }

    private static void getMyCoinPrices() {
        cursor = myCoinRepo.getCryptoCurrencyList();
        apiConnection.getCoinPrices(cursor);
    }
}
