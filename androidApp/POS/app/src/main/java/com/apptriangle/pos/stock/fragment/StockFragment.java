package com.apptriangle.pos.stock.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.apptriangle.pos.R;
import com.apptriangle.pos.stock.adaptor.StockAdaptor;
import com.apptriangle.pos.stock.response.StockResponse;

import java.util.ArrayList;

/**
 * Created by zawan on 3/30/18.
 */

public class StockFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View contentView;
    String[] listItems = {"item 1", "item 2 ", "list", "android"};
    Button salesBtn;
    private RecyclerView mRecyclerView;
    LinearLayout searchContainer, lytEmpty;
    StockAdaptor adapter;


    public StockFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_stock, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lytEmpty = (LinearLayout) contentView.findViewById(R.id.lytEmpty);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.my_recycler_view);
        ArrayList<StockResponse> stockResponseArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            StockResponse tmp = new StockResponse();
            stockResponseArrayList.add(tmp);
        }
        if (stockResponseArrayList.size() > 0)
            hideEmptyView();
        adapter = new StockAdaptor(getActivity(), stockResponseArrayList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        setTitle();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onSalesClick() {
        if (mListener != null) {
            mListener.onSalesClickListener();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    void setTitle() {
        ((Activity) getActivity()).setTitle(getResources().getString(R.string.app_name));
    }


    public void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        lytEmpty.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        lytEmpty.setVisibility(View.GONE);
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
    }
}
