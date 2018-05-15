package com.apptriangle.pos.sales.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.apptriangle.pos.R;
import com.apptriangle.pos.SecureActivity;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.model.CSale;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.adaptor.VerifySaleAdaptor;
import com.apptriangle.pos.sales.response.SalesResponse;
import com.apptriangle.pos.sales.restInterface.SalesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zeeshan on 4/1/2018.
 */
public class InvoiceFragment extends Fragment {
    private String apiKey;
    private ProgressDialog pd;
    private OnFragmentInteractionListener mListener;
    private View contentView;
    private RecyclerView recyclerView;
    String[] listItems = {"item 1", "item 2 ", "list", "android" };
    private VerifySaleAdaptor adaptor;
    public boolean fromHome = false;
    private CardView searchContainer1, searchContainer2;
    private NestedScrollView saleDescContainer;
    public SalesResponse cart;
    public Button finishBtn;
    public EditText searchText;
    public CSale saleObj;
    public Double totalAmount, paidAmount, dueAmount;
    public EditText edtCustName, edtCustNo,edtTotalAmnt, edtPaidAmnt, edtDueAmnt;


    TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if(s != null && !s.toString().trim().equalsIgnoreCase("")) {
                paidAmount = Double.parseDouble(s.toString());
                dueAmount = totalAmount - paidAmount;
                if(dueAmount < 0)
                    dueAmount = 0.0;
                edtDueAmnt.setText(dueAmount.toString());
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

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

    private void getApiKey() {
        SharedPreferences shared = getActivity().getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        apiKey = shared.getString("api_key", "");

    }

    public void initialize(){
        getApiKey();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");
        pd.setCanceledOnTouchOutside(false);
        finishBtn = (Button)contentView.findViewById(R.id.finishButton);
        edtTotalAmnt = (EditText) contentView.findViewById(R.id.edtTotalAmnt);
        searchText = (EditText) contentView.findViewById(R.id.searchText);
        edtCustName = (EditText) contentView.findViewById(R.id.edtCustName);
        edtPaidAmnt = (EditText) contentView.findViewById(R.id.edtPaidAmnt);
        edtCustNo = (EditText) contentView.findViewById(R.id.edtCustNo);
        edtDueAmnt = (EditText) contentView.findViewById(R.id.edtDueAmnt);
        if(!fromHome) {
            edtTotalAmnt.setEnabled(false);
            edtPaidAmnt.setEnabled(false);
            edtDueAmnt.setEnabled(false);
            edtCustName.setEnabled(false);
            edtCustNo.setEnabled(false);
            edtCustNo.setText(cart.cust_phone);
            edtCustName.setText(cart.cust_name);
            edtTotalAmnt.setText(cart.total_amount.toString());
            edtPaidAmnt.setText(cart.amount_paid.toString());
            edtDueAmnt.setText(Double.toString(cart.total_amount - cart.amount_paid));
        }
        recyclerView = (RecyclerView) contentView.findViewById(R.id.sales_recycler_view);
        searchContainer1 =(CardView) contentView.findViewById(R.id.searchContainer1);
        searchContainer2 =(CardView) contentView.findViewById(R.id.searchContainer2);
        saleDescContainer =(NestedScrollView) contentView.findViewById(R.id.saleDescContainer);
        Button printBtn = (Button)contentView.findViewById(R.id.printBtn);
        Button smsBtn = (Button)contentView.findViewById(R.id.smsBtn);
        if(!fromHome) {
            searchContainer1.setVisibility(View.GONE);
            searchContainer2.setVisibility(View.GONE);
            printBtn.setVisibility(View.VISIBLE);
            smsBtn.setVisibility(View.VISIBLE);
        }else{
            saleDescContainer.setVisibility(View.GONE);
        }
        ArrayList<SalesResponse> stockResponseArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SalesResponse tmp = new SalesResponse();
            stockResponseArrayList.add(tmp);
        }

        if(cart != null) {
            adaptor = new VerifySaleAdaptor(getActivity(), cart.products, true);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adaptor);
        }

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SecureActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


   /* public void getInvoiceData(){
        SalesService service =
                ApiClient.getClient().create(SalesService.class);
        Call<CSale> call;
        call = service.getInvoice(apiKey, searchText.getText().toString());
        pd.show();
        call.enqueue(new Callback<CSale>() {
            @Override
            public void onResponse(Call<CSale> call, Response<CSale> response) {
                pd.hide();
                if (response != null && response.body() != null) {
                    saleObj = (CSale) response.body();
                    edtTotalAmnt.setEnabled(false);

                    edtDueAmnt.setEnabled(false);
                    edtCustName.setEnabled(false);
                    edtCustNo.setEnabled(false);
                    edtCustNo.setText(cart.cust_phone);
                    edtCustName.setText(cart.cust_name);
                    edtTotalAmnt.setText(cart.total_amount.toString());
                    edtPaidAmnt.setText(cart.amount_paid.toString());
                    edtDueAmnt.setText(Double.toString(cart.total_amount - cart.amount_paid));
                    if(saleObj.amount_paid == saleObj.total_amt){
                        edtPaidAmnt.setEnabled(false);
                    }else if(saleObj.amount_paid < saleObj.total_amt){
                        edtPaidAmnt.setEnabled(true);
                    }

                }
            }

            @Override
            public void onFailure(Call<CSale> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }
*/
    public void updateInvoice(){

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