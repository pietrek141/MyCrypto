package piotrmroczkowski.mycrypto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Map;

public class MyCoinRepo {

    private CryptoDatabaseHelper dbHelper;

    public MyCoinRepo(Context context) {
        dbHelper = CryptoDatabaseHelper.instance(MyApplication.getAppContext());
    }


    public static String readFromMyCoin(CryptoDatabaseHelper helper) {

        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        String result = "";

        Cursor cursor = readableDatabase.query("MY_CRYPTO", new String[]{"NAME", "BUY_PRICE"}, "_id = ?",
                new String[]{Integer.toString(1)},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            result = cursor.getString(1);

        }
        cursor.close();
        readableDatabase.close();

        return result;

    }

    public void insertToMyCoinByName(String symbol, String price) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues coinValues = new ContentValues();
        coinValues.put("SYMBOL", symbol);
        coinValues.put("BUY_PRICE", price);
        writableDatabase.insert("MY_CRYPTO", null, coinValues);

        long count = DatabaseUtils.queryNumEntries(writableDatabase, "MY_CRYPTO");
        String liczba = Long.toString(count);
        Log.d("My_CRYPTO", liczba);
        writableDatabase.close();
    }

    public void updateMyCoin(Map<String, String> map) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues coinValues = new ContentValues();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String price = entry.getValue();
            String name = entry.getKey();
            coinValues.put("LAST_PRICE", price);
            writableDatabase.update("MY_CRYPTO", coinValues, "SYMBOL = '" + name + "'", null);
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
                "SELECT _id, " + "SYMBOL, "
                        + "BUY_PRICE, "
                        + "LAST_PRICE "
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
}
