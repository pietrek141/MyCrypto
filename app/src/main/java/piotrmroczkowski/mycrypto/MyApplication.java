package piotrmroczkowski.mycrypto;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

 /*   public void addButtonClicked(View v){

        Log.d("ON CLICKED 2", "clicked 2");
    }*/

    public static Context getAppContext() {
        return MyApplication.context;
    }

}
