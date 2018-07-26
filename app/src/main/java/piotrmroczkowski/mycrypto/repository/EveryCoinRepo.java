package piotrmroczkowski.mycrypto.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import piotrmroczkowski.mycrypto.app.MyApplication;
import piotrmroczkowski.mycrypto.cryptocurrency.Cryptocurrency;

import static piotrmroczkowski.mycrypto.cryptocurrency.Cryptocurrency.everyCoinList;

public class EveryCoinRepo {
    private CryptoDatabaseHelper dbHelper;

    public EveryCoinRepo() {
        dbHelper = CryptoDatabaseHelper.instance(MyApplication.getAppContext());
    }


    public void insert() {

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.delete("EVERY_COIN", null, null);


        insertToEveryCoin(writableDatabase, everyCoinList);
        long count = DatabaseUtils.queryNumEntries(writableDatabase, "EVERY_COIN");
        String liczba = Long.toString(count);
        Log.d("DB Connection", liczba);
        writableDatabase.close();
    }


    private void insertToEveryCoin(SQLiteDatabase db, List<Cryptocurrency> everyCoinList) {
        db.beginTransaction();
        for (int i = 0; i < everyCoinList.size(); i++) {
            String name = everyCoinList.get(i).getName();
            String symbol = everyCoinList.get(i).getSymbol();
            ContentValues coinValues = new ContentValues();
            coinValues.put("NAME", name);
            coinValues.put("SYMBOL", symbol);
            db.insert("EVERY_COIN", null, coinValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public Cursor getCryptoCurrencyListByKeyword(String search) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =
                "SELECT _id, " + "NAME, "
                        + "SYMBOL " + "FROM EVERY_COIN" + " WHERE NAME LIKE '" + search + "%'" + " ORDER BY NAME";
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
