package piotrmroczkowski.mycrypto.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;

import piotrmroczkowski.mycrypto.app.MyApplication;

public class MyCoinRepo {

    private CryptoDatabaseHelper dbHelper;

    public MyCoinRepo() {
        dbHelper = CryptoDatabaseHelper.instance(MyApplication.getAppContext());
    }


    public void insertToMyCoinByName(String name, String symbol, String price) throws NumberFormatException {
        try {
            if (price.equals("")) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(MyApplication.getAppContext(), "Buy price is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues coinValues = new ContentValues();
        coinValues.put("NAME", name);
        coinValues.put("SYMBOL", symbol);
        coinValues.put("BUY_PRICE", price);
        writableDatabase.insert("MY_CRYPTO", null, coinValues);

        long count = DatabaseUtils.queryNumEntries(writableDatabase, "MY_CRYPTO");
        String liczba = Long.toString(count);
        Log.d("My_CRYPTO", liczba);
        writableDatabase.close();
    }

    public void updateAmountInMyCoinBySymbol(int amount, String symbol) throws NumberFormatException {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues coinValues = new ContentValues();
        coinValues.put("AMOUNT", amount);
        writableDatabase.update("MY_CRYPTO", coinValues, "SYMBOL = '" + symbol + "'", null);

    }

    public void updateMyCoin(Map<String, String> map) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues coinValues = new ContentValues();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String price = entry.getValue();
            String symbol = entry.getKey();
            coinValues.put("LAST_PRICE", price);
            writableDatabase.update("MY_CRYPTO", coinValues, "SYMBOL = '" + symbol + "'", null);
        }

        writableDatabase.close();
    }

    public void deleteFromMyCoinByName(String symbol) {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues coinValues = new ContentValues();
        coinValues.put("SYMBOL", symbol);
        writableDatabase.delete("MY_CRYPTO", "SYMBOL = '" + symbol + "'", null);

        long count = DatabaseUtils.queryNumEntries(writableDatabase, "MY_CRYPTO");
        String liczba = Long.toString(count);
        Log.d("My_CRYPTO", liczba);
        writableDatabase.close();
    }

    public Cursor getCryptoCurrencyList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =
                "SELECT _id, "
                        + "NAME, "
                        + "SYMBOL, "
                        + "BUY_PRICE, "
                        + "LAST_PRICE, "
                        + "AMOUNT "
                        + "FROM MY_CRYPTO";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        db.close();
        return cursor;
    }

    public Cursor getCryptoCurrencybySymbol(String symbol) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =
                "SELECT _id, "
                        + "NAME, "
                        + "SYMBOL, "
                        + "BUY_PRICE, "
                        + "LAST_PRICE, "
                        + "AMOUNT "
                        + "FROM MY_CRYPTO"
                        + " WHERE SYMBOL LIKE '" + symbol + "%'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        db.close();
        return cursor;
    }

    public Cursor getAllMoney() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =
                "SELECT SUM(BUY_PRICE * AMOUNT) AS 'BUY_MONEY_SUM', SUM(LAST_PRICE * AMOUNT) AS 'LAST_MONEY_SUM'"
                        + " FROM MY_CRYPTO"
                        + " WHERE AMOUNT IS NOT NULL";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        db.close();
        return cursor;
    }

}
