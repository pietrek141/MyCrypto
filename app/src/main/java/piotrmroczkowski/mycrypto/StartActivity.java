package piotrmroczkowski.mycrypto;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;


public class StartActivity extends AppCompatActivity {

    Cursor cursor;
    ViewGroup container;
    SearchView searchView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        CryptoDatabaseHelper.instance(this);
        ViewManager.everyCoinListView = findViewById(R.id.listViewEveryCoin);
        ViewManager.myCoinListView = findViewById(R.id.listViewMyCoin);
        searchView = findViewById(R.id.searchView);
        ViewManager.everyCoinListView.setVisibility(View.GONE);
        ViewManager.myCoinListView.setVisibility(View.VISIBLE);

        ApiConnection connection = new ApiConnection();
        connection.letsDoSomeNetworking(ApiConnection.URL_COIN_LIST);
        ViewManager.initMyCryptoView();

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //is it necessary?
        try {
            searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ViewManager.everyCoinListView.setVisibility(View.VISIBLE);
                ViewManager.myCoinListView.setVisibility(View.GONE);
                Log.d("SEARCH", "OnQueryTextSubmit");
                cursor = ViewManager.showEveryCoinList(query);
                if (cursor == null) {
                    Toast.makeText(MyApplication.getAppContext(), "No records  found!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MyApplication.getAppContext(), cursor.getCount() + " records found!", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                ViewManager.everyCoinListView.setVisibility(View.VISIBLE);
                ViewManager.myCoinListView.setVisibility(View.GONE);
                Log.d("SEARCH", "OnQueryTextChange");
                ViewManager.showEveryCoinList(query);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ViewManager.everyCoinListView.setVisibility(View.GONE);
                ViewManager.myCoinListView.setVisibility(View.VISIBLE);
                Log.d("On search view close", "closed");
                return false;
            }
        });
    }
}
