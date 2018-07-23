package piotrmroczkowski.mycrypto;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ViewManager {

    public static Cursor cursor;
    public static Cursor cursor2;
    public static SearchView searchView;
    public static TextView allMoney;
    public static TextView allPercent;
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
        cursor2 = myCoinRepo.getAllMoney();
        ListAdapter listAdapter = new MyCoinCursorAdapter(MyApplication.getAppContext(), cursor);
        myCoinListView.setAdapter(listAdapter);
        //String allMoneyValue = String.valueOf(cursor2.getDouble(cursor2.getColumnIndex("MONEY_SUM")));
        BigDecimal allLastMoneyValue = new BigDecimal(cursor2.getDouble(cursor2.getColumnIndex("LAST_MONEY_SUM")));
        BigDecimal allBuyMoneyValue = new BigDecimal(cursor2.getDouble(cursor2.getColumnIndex("BUY_MONEY_SUM")));
        DecimalFormat df2 = new DecimalFormat("$#.##");
        String allLastMoneyToShow = df2.format(allLastMoneyValue);
        String allPercentToShow = calculatePercent(allBuyMoneyValue.toString(), allLastMoneyValue.toString(), allPercent);
        allMoney.setText(allLastMoneyToShow);
        allPercent.setText(allPercentToShow);
    }

    private static void getMyCoinPrices() {
        cursor = myCoinRepo.getCryptoCurrencyList();
        apiConnection.getCoinPrices(cursor);
    }

    public static Cursor initMyCryptoDetailView(String symbol) {

        cursor = myCoinRepo.getCryptoCurrencybySymbol(symbol);
        return cursor;
    }

    public static String calculatePercent(String buyPrice, String lastPrice, TextView percentTextView) {
        Double percentValue = 0.0;
        try {
            buyPrice.replace(",", ".");
            percentValue = (Double.parseDouble(lastPrice) / Double.parseDouble(buyPrice)) - 1;
        } catch (Exception e) {
            Log.d("CALCULATE PERCENT", "Buy Price is empty");
        }

        percentValue = percentValue * 100;
        DecimalFormat df2 = new DecimalFormat("#.##");

        String result = df2.format(percentValue);
        if (Double.parseDouble(result) >= 0) {
            result = "+" + result + "%";
            percentTextView.setTextColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.Green800));
        } else {
            result = result + "%";
            percentTextView.setTextColor(Color.RED);
        }

        return result;
    }
}
