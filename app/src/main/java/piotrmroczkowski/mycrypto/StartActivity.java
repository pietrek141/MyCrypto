package piotrmroczkowski.mycrypto;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    Cursor cursor;
    ListView everyCoinlistView;
    static ListView myCoinListView;
    EveryCoinRepo everyCoinRepo = new EveryCoinRepo(MyApplication.getAppContext());
    static MyCoinRepo myCoinRepo = new MyCoinRepo(MyApplication.getAppContext());
    ViewGroup container;
    String[] columns = {"NAME", "SYMBOL"};
    int[] resourceIds = {R.id.textview_list_row_coinName, R.id.textview_list_row_coinSymbol};
    ImageButton addCryptoButton;
    LayoutInflater inflater;
    SearchView searchView;
    static ApiConnection apiConnection = new ApiConnection();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        CryptoDatabaseHelper.instance(this);
        everyCoinlistView = (ListView) findViewById(R.id.listViewEveryCoin);
        everyCoinlistView.setItemsCanFocus(true);
        myCoinListView = (ListView) findViewById(R.id.listViewMyCoin);
        searchView = (SearchView) findViewById(R.id.searchView);

/*        inflater = getLayoutInflater();
        View convertView = (ConstraintLayout)inflater.inflate(R.layout.listview_everycoin_row,null );
        addCryptoButton = (ImageButton)convertView.findViewById(R.id.button_add_to_MyCrypto);
        addCryptoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ON CLICKED", "clicked");
            }
        });*/

        ApiConnection connection = new ApiConnection();
        connection.letsDoSomeNetworking(ApiConnection.URL_COIN_LIST);
        initMyCryptoView();

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d("SEARCH", "OnQueryTextSubmit");
                showEveryCoinList(query);
                if (cursor == null) {
                    Toast.makeText(MyApplication.getAppContext(), "No records  found!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MyApplication.getAppContext(), cursor.getCount() + " records found!", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.d("SEARCH", "OnQueryTextChange");
                showEveryCoinList(query);
                return false;
            }
        });
    }

    public static void updateMyCrypto() {

        Cursor cursor = myCoinRepo.getCryptoCurrencyList();
        ListAdapter listAdapter = new MyCoinCursorAdapter(MyApplication.getAppContext(),cursor);
        myCoinListView.setAdapter(listAdapter);
        getMyCoinPrices();
    }

    public void showEveryCoinList(String query) {
        cursor = everyCoinRepo.getCryptoCurrencyListByKeyword(query);
        ListAdapter listAdapter = new EveryCoinCursorAdapter(this,  cursor);
        everyCoinlistView.setAdapter(listAdapter);
    }

    public static void initMyCryptoView() {

        Cursor cursor = myCoinRepo.getCryptoCurrencyList();
        ListAdapter listAdapter = new MyCoinCursorAdapter(MyApplication.getAppContext(),cursor);
        myCoinListView.setAdapter(listAdapter);
    }

    public static void getMyCoinPrices(){
        Cursor cursor = myCoinRepo.getCryptoCurrencyList();
        apiConnection.getCoinPrices(cursor);
    }

}
