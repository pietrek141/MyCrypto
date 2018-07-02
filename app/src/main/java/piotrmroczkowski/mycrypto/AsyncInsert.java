package piotrmroczkowski.mycrypto;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import static piotrmroczkowski.mycrypto.Cryptocurrency.everyCoinList;

public class AsyncInsert extends AsyncTask {

    private CryptoDatabaseHelper dbHelper;

    public AsyncInsert(Context context){
        dbHelper = CryptoDatabaseHelper.instance(MyApplication.getAppContext());
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        /*        new AsyncInsert.execute();

        class AsyncInsert extends AsyncTask {



            @Override
            protected Object doInBackground(Object[] objects) {*/

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.rawQuery("DELETE FROM EVERY_COIN", null );
        for (int i = 0; i < everyCoinList.size(); i++) {
            String name = everyCoinList.get(i).getName();
            String symbol = everyCoinList.get(i).getSymbol();
            insertToEveryCoin(writableDatabase, name, symbol);
        }
        writableDatabase.close();
        //return null;


    // }


        return null;
    }

    public void insertToEveryCoin(SQLiteDatabase db, String name, String symbol){
        ContentValues coinValues = new ContentValues();
        coinValues.put("NAME", name);
        coinValues.put("SYMBOL", symbol);
        db.insert("EVERY_COIN", null, coinValues);
    }
}
