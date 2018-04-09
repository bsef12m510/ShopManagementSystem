package com.apptriangle.pos.sales.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.apptriangle.pos.R;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.adaptor.VerifySaleAdaptor;
import com.apptriangle.pos.sales.response.SalesResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeeshan on 3/31/2018.
 */
public class VerifySalesFragment extends Fragment {
    private Button finishBtn;
    private OnFragmentInteractionListener mListener;
    private View contentView;
    private RecyclerView recyclerView;
    String[] listItems = {"item 1", "item 2 ", "list", "android" };
    private VerifySaleAdaptor adaptor;
    private List<Product> cart;
    public Double totalAmount, paidAmount, dueAmount;
    public EditText edtCustName, edtCustNo,edtTotalAmnt, edtPaidAmnt, edtDueAmnt;

    TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if(s != null && !s.toString().trim().equalsIgnoreCase("")) {
                dueAmount = totalAmount - paidAmount;
                edtDueAmnt.setText(dueAmount.toString());
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public VerifySalesFragment() {
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
        contentView = inflater.inflate(R.layout.fragment_verify_sales, container, false);
        return contentView;
    }

    public void initialize(){
        finishBtn = (Button)contentView.findViewById(R.id.finishButton);
        edtTotalAmnt = (EditText) contentView.findViewById(R.id.edtTotalAmnt);
        edtPaidAmnt = (EditText) contentView.findViewById(R.id.edtPaidAmnt);
        edtDueAmnt = (EditText) contentView.findViewById(R.id.edtDueAmnt);
        edtPaidAmnt.addTextChangedListener(inputTextWatcher);
        finishBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateCart();
                onFinishPressed();
            }
        });
        recyclerView = (RecyclerView) contentView.findViewById(R.id.sales_recycler_view);
        ArrayList<SalesResponse> stockResponseArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SalesResponse tmp = new SalesResponse();
            stockResponseArrayList.add(tmp);
        }

//        adaptor = new VerifySaleAdaptor(getActivity(), stockResponseArrayList, false);
        adaptor = new VerifySaleAdaptor(getActivity(), cart, false);
        adaptor.parentFrag = VerifySalesFragment.this;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptor);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onFinishPressed() {
        if (mListener != null) {
            mListener.onFinishClicked();
        }
    }

    public void updateCart(){

        for (int i = 0; i < cart.size(); i++) {
            if(!cart.get(i).isChecked())
                cart.remove(i);
        }

        // create json for sales request here and send request
    }

    void setTitle()
    {
        ((Activity) getActivity()).setTitle("VERIFY SALE");
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
    public void onResume() {
        super.onResume();
        setTitle();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setCart(List<Product> cart) {
        this.cart = cart;
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
        void onFinishClicked();
    }
}