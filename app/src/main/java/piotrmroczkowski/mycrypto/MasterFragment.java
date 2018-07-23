package piotrmroczkowski.mycrypto;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasterFragment extends Fragment {


    Cursor cursor;
    Cursor cursor2;
    ViewGroup container;


    public interface ItemClickListener {
        public void itemClicked(String symbol);
    }

    public static ItemClickListener mListener;


    public MasterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ItemClickListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (ItemClickListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewManager.initMyCryptoView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_master, container, false);


        CryptoDatabaseHelper.instance(getContext());
        ViewManager.everyCoinListView = view.findViewById(R.id.listViewEveryCoin);
        ViewManager.myCoinListView = view.findViewById(R.id.listViewMyCoin);
        ViewManager.searchView = view.findViewById(R.id.searchView);
        ViewManager.allMoney = view.findViewById(R.id.textview_master_all_money);
        ViewManager.allPercent = view.findViewById(R.id.textview_master_all_percent);
        final LinearLayout myCoinRow = view.findViewById(R.id.myCoinRowLayout);
        ViewManager.everyCoinListView.setVisibility(View.GONE);
        ViewManager.myCoinListView.setVisibility(View.VISIBLE);
        ApiConnection connection = new ApiConnection();
        connection.letsDoSomeNetworking(ApiConnection.URL_COIN_LIST);
        ViewManager.initMyCryptoView();


        SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        //is it necessary?
        try {
            ViewManager.searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        ViewManager.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ViewManager.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ViewManager.everyCoinListView.setVisibility(View.GONE);
                ViewManager.myCoinListView.setVisibility(View.VISIBLE);
                Log.d("On search view close", "closed");
                return false;
            }
        });


        return view;

    }

}
