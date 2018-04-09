package com.apptriangle.pos.sales.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apptriangle.pos.R;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.adaptor.VerifySaleAdaptor;
import com.apptriangle.pos.sales.response.SalesResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeeshan on 4/1/2018.
 */
public class InvoiceFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View contentView;
    private RecyclerView recyclerView;
    String[] listItems = {"item 1", "item 2 ", "list", "android" };
    private VerifySaleAdaptor adaptor;
    public boolean fromHome = false;
    private CardView searchContainer1, searchContainer2;
    private NestedScrollView saleDescContainer;
    private List<Product> cart;


    public InvoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle();
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_invoice, container, false);
        return contentView;
    }

    public void initialize(){
        recyclerView = (RecyclerView) contentView.findViewById(R.id.sales_recycler_view);
        searchContainer1 =(CardView) contentView.findViewById(R.id.searchContainer1);
        searchContainer2 =(CardView) contentView.findViewById(R.id.searchContainer2);
        saleDescContainer =(NestedScrollView) contentView.findViewById(R.id.saleDescContainer);
        Button printBtn = (Button)contentView.findViewById(R.id.printBtn);
        if(!fromHome) {
            searchContainer1.setVisibility(View.GONE);
            searchContainer2.setVisibility(View.GONE);
            printBtn.setVisibility(View.VISIBLE);
        }else{
            saleDescContainer.setVisibility(View.GONE);
        }
        ArrayList<SalesResponse> stockResponseArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SalesResponse tmp = new SalesResponse();
            stockResponseArrayList.add(tmp);
        }

        adaptor = new VerifySaleAdaptor(getActivity(), cart, true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptor);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    void setTitle()
    {
        ((Activity) getActivity()).setTitle("INVOICE");
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
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
        void onFragmentInteraction();
    }
}