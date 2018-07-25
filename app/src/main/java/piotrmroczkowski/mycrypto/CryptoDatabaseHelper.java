package piotrmroczkowski.mycrypto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CryptoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "crypto";
    private static final int DB_VERSION = 1;
    private static CryptoDatabaseHelper mInstance;

    private CryptoDatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    public static CryptoDatabaseHelper instance(Context context) {
        if (mInstance == null) {
            mInstance = new CryptoDatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + "EVERY_COIN");
        db.execSQL("CREATE TABLE IF NOT EXISTS EVERY_COIN ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "SYMBOL TEXT);");

        db.execSQL("DROP TABLE IF EXISTS " + "MY_CRYPTO");
        db.execSQL("CREATE TABLE IF NOT EXISTS MY_CRYPTO ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "SYMBOL TEXT, "
                + "BUY_PRICE REAL, "
                + "LAST_PRICE REAL, "
                + "AMOUNT INTEGER, "
                + "BUY_DATE NUMERIC);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "EVERY_COIN");
        onCreate(db);
    }

}
