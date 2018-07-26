package piotrmroczkowski.mycrypto.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import piotrmroczkowski.mycrypto.R;


public class DetailFragmentNotChosen extends Fragment {

    private static final String ARG_SYMBOL = "ARG SYMBOL";


    public DetailFragmentNotChosen() {
    }

    public static DetailFragmentNotChosen newFragment() {

        DetailFragmentNotChosen fragment = new DetailFragmentNotChosen();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_detail_not_chosen, container, false);

        return view;
    }
}
