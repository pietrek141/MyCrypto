package piotrmroczkowski.mycrypto.fragments;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import piotrmroczkowski.mycrypto.R;
import piotrmroczkowski.mycrypto.api.ApiConnection;
import piotrmroczkowski.mycrypto.app.MyApplication;
import piotrmroczkowski.mycrypto.repository.CryptoDatabaseHelper;
import piotrmroczkowski.mycrypto.view.ViewManager;


public class MasterFragment extends Fragment {


    Cursor cursor;
    ViewGroup container;


    public interface ItemClickListener {
        void itemClicked(String symbol);
    }

    public interface DeleteClickListener {
        void deleteClicked();
    }

    public static ItemClickListener mListener;

    public static DeleteClickListener dListener;


    public MasterFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ItemClickListener) context;
        dListener = (DeleteClickListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (ItemClickListener) activity;
        dListener = (DeleteClickListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        dListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewManager.initMyCryptoView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_master, container, false);


        CryptoDatabaseHelper.instance(getContext());
        ViewManager.everyCoinListView = view.findViewById(R.id.listViewEveryCoin);
        ViewManager.myCoinListView = view.findViewById(R.id.listViewMyCoin);
        ViewManager.searchView = view.findViewById(R.id.searchView);
        ViewManager.allMoney = view.findViewById(R.id.textview_master_all_money);
        ViewManager.allPercent = view.findViewById(R.id.textview_master_all_percent);
        ViewManager.everyCoinListView.setVisibility(View.GONE);
        ViewManager.myCoinListView.setVisibility(View.VISIBLE);
        ApiConnection connection = new ApiConnection();
        connection.connectToAPI(ApiConnection.URL_COIN_LIST);


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
