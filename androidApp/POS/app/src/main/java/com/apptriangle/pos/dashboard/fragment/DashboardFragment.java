package com.apptriangle.pos.dashboard.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;


import com.apptriangle.pos.R;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DashboardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View contentView;
    String[] listItems = {"item 1", "item 2 ", "list", "android" };
    Button salesBtn, stockBtn, invoiceBtn;


    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle();
        salesBtn = (Button)contentView.findViewById(R.id.button1);
        stockBtn = (Button)contentView.findViewById(R.id.button3);
        invoiceBtn = (Button)contentView.findViewById(R.id.button4);
        salesBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onSalesClick();
            }
        });

        stockBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onStockClick();
            }
        });

        invoiceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onInvoiceClick();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onSalesClick() {
        if (mListener != null) {
            mListener.onSalesClickListener();
        }
    }

    public void onStockClick() {
        if (mListener != null) {
            mListener.onStockClickListener();
        }
    }

    public void onInvoiceClick() {
        if (mListener != null) {
            mListener.onInvoiceClickListener();
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    void setTitle()
    {
        ((Activity) getActivity()).setTitle(getResources().getString(R.string.app_name));
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSalesClickListener();
        void onStockClickListener();
        void onInvoiceClickListener();
    }
}
