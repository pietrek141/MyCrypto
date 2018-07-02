package piotrmroczkowski.mycrypto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static piotrmroczkowski.mycrypto.Cryptocurrency.everyCoinList;

public class EveryCoinRepo {
    private CryptoDatabaseHelper dbHelper;

    public EveryCoinRepo(Context context) {
        dbHelper = CryptoDatabaseHelper.instance(MyApplication.getAppContext());
    }

    public static String readFromEveryCoin(CryptoDatabaseHelper helper) {

        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        String result = "";

        Cursor cursor = readableDatabase.query("EVERY_COIN", new String[]{"NAME", "SYMBOL"}, "_id = ?",
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


    public void insert() {


/*        new AsyncInsert.execute();

        class AsyncInsert extends AsyncTask {

            @Override
            protected Object doInBackground(Object[] objects) {*/
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.delete("EVERY_COIN", null, null);

        for (int i = 0; i < everyCoinList.size(); i++) {
            String name = everyCoinList.get(i).getName();
            String symbol = everyCoinList.get(i).getSymbol();
            insertToEveryCoin(writableDatabase, name, symbol);
        }

        long count = DatabaseUtils.queryNumEntries(writableDatabase, "EVERY_COIN");
        String liczba = Long.toString(count);
        Log.d("DB Connection", liczba);
        writableDatabase.close();
        //return null;
    }

    // }


    public void insertToEveryCoin(SQLiteDatabase db, String name, String symbol) {
        ContentValues coinValues = new ContentValues();
        coinValues.put("NAME", name);
        coinValues.put("SYMBOL", symbol);
        db.insert("EVERY_COIN", null, coinValues);
    }

    public Cursor getCryptoCurrencyListByKeyword(String search) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =
                "SELECT _id, " + "NAME, "
                        + "SYMBOL " + "FROM EVERY_COIN" + " WHERE NAME LIKE '%" + search + "%'";
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
