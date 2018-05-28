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
import android.widget.Toast;

import com.apptriangle.pos.InvoiceSearchFragment.adapter.InvoiceAdapter;
import com.apptriangle.pos.InvoiceSearchFragment.fragment.InvoiceSearchFragment;
import com.apptriangle.pos.InvoiceSearchFragment.service.InvoiceService;
import com.apptriangle.pos.R;
import com.apptriangle.pos.SecureActivity;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.model.CSale;
import com.apptriangle.pos.model.Invoice;
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
    String[] listItems = {"item 1", "item 2 ", "list", "android"};
    private VerifySaleAdaptor adaptor;
    public boolean fromHome = false;
    private CardView searchContainer1, searchContainer2;
    private NestedScrollView saleDescContainer;
    public SalesResponse cart;
    public Button finishBtn;
    public EditText searchText;
    public CSale saleObj;
    public Double totalAmount = 0.0, paidAmount = 0.0, dueAmount = 0.0;
    public EditText edtCustName, edtCustNo, edtTotalAmnt, edtPaidAmnt, edtDueAmnt;
    public Invoice selectedInvoice;

    TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().trim().equalsIgnoreCase("")) {
               /* if (!fromHome && cart == null)
                    paidAmount = paidAmount + Double.parseDouble(s.toString());
                else
                    paidAmount = Double.parseDouble(s.toString());*/
                if (!fromHome && cart == null)
                    dueAmount = totalAmount - (paidAmount + Double.parseDouble(s.toString()));
                else
                    dueAmount = totalAmount - paidAmount;
                if (dueAmount < 0)
                    dueAmount = 0.0;
                edtDueAmnt.setText(dueAmount.toString()+"TK");
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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

    public void initialize() {
        getApiKey();
       /* pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");*/
       /* pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);*/
        finishBtn = (Button) contentView.findViewById(R.id.finishButton);
        edtTotalAmnt = (EditText) contentView.findViewById(R.id.edtTotalAmnt);
        searchText = (EditText) contentView.findViewById(R.id.searchText);
        edtCustName = (EditText) contentView.findViewById(R.id.edtCustName);
        edtPaidAmnt = (EditText) contentView.findViewById(R.id.edtPaidAmnt);
        edtCustNo = (EditText) contentView.findViewById(R.id.edtCustNo);
        edtDueAmnt = (EditText) contentView.findViewById(R.id.edtDueAmnt);

        edtCustName.setEnabled(false);
        edtCustNo.setEnabled(false);
        if (!fromHome) {
            edtTotalAmnt.setEnabled(false);
            edtPaidAmnt.setEnabled(false);
            edtDueAmnt.setEnabled(false);
            if (cart != null) {
                edtCustNo.setText(cart.cust_phone);
                edtCustName.setText(cart.cust_name);
                edtTotalAmnt.setText(cart.total_amount.toString()+"TK");
                edtPaidAmnt.setText(cart.amount_paid.toString()+"TK");
                edtDueAmnt.setText(Double.toString(cart.total_amount - cart.amount_paid)+"TK");
            } else {
                if (selectedInvoice.amount_paid < selectedInvoice.total_amount) {
                    edtPaidAmnt.setEnabled(true);
                    edtPaidAmnt.addTextChangedListener(inputTextWatcher);
                }
                totalAmount = selectedInvoice.total_amount;
                paidAmount = selectedInvoice.amount_paid;

                edtCustNo.setText(selectedInvoice.cust_phone);
                edtCustName.setText(selectedInvoice.cust_name);
                edtTotalAmnt.setText(Double.toString(selectedInvoice.total_amount)+"TK");
                edtPaidAmnt.setText(Double.toString(selectedInvoice.amount_paid)+"TK");
                edtDueAmnt.setText(Double.toString(selectedInvoice.total_amount - selectedInvoice.amount_paid)+"TK");
            }
        }
        recyclerView = (RecyclerView) contentView.findViewById(R.id.sales_recycler_view);
        searchContainer1 = (CardView) contentView.findViewById(R.id.searchContainer1);
        searchContainer2 = (CardView) contentView.findViewById(R.id.searchContainer2);
        saleDescContainer = (NestedScrollView) contentView.findViewById(R.id.saleDescContainer);
        Button printBtn = (Button) contentView.findViewById(R.id.printBtn);
        Button smsBtn = (Button) contentView.findViewById(R.id.smsBtn);
        searchContainer1.setVisibility(View.GONE);
        searchContainer2.setVisibility(View.GONE);
        if (!fromHome) {

            printBtn.setVisibility(View.VISIBLE);
            smsBtn.setVisibility(View.VISIBLE);
        } else {
            saleDescContainer.setVisibility(View.GONE);
        }
        ArrayList<SalesResponse> stockResponseArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SalesResponse tmp = new SalesResponse();
            stockResponseArrayList.add(tmp);
        }

        if (cart != null) {
            finishBtn.setText("Back To Home");
            adaptor = new VerifySaleAdaptor(getActivity(), cart.products, true);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adaptor);
        } else {
            adaptor = new VerifySaleAdaptor(getActivity(), selectedInvoice.products, true);
            adaptor.isFromInvoiceSearchScreen = true;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adaptor);
        }

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart != null) {
                    Intent intent = new Intent(getActivity(), SecureActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if (edtPaidAmnt.isEnabled())
                        updateInvoice();
                    else {
                        Intent intent = new Intent(getActivity(), SecureActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }
        });
    }


    public void updateInvoice() {
        boolean isOk = false;
        if(!fromHome && cart == null){
            if(paidAmount + (Double.parseDouble(edtPaidAmnt.getText().toString())) <= totalAmount)
                isOk = true;
            else
                isOk = false;
        }else{
            if(paidAmount <= totalAmount)
                isOk = true;
            else
                isOk =false;
        }
        if (isOk) {
            InvoiceService service =
                    ApiClient.getClient().create(InvoiceService.class);
            Call<Object> call;
            call = service.updatePayment(apiKey, selectedInvoice.invoiceId, Double.parseDouble(edtPaidAmnt.getText().toString()));
//            pd.show();
            pd = ProgressDialog.show(getActivity(), null, "Processing");
            pd.setCanceledOnTouchOutside(false);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    pd.dismiss();
                    if (response != null && response.body() != null) {
                        if (response.body() instanceof Boolean) {
                            if ((boolean) response.body())
                                Toast.makeText(getActivity(), "Invoice Payment Updated Successfully.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), "Unable to update payment", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Intent intent = new Intent(getActivity(), SecureActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("failure", "failure");
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Unable to update payment", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), SecureActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            });
        } else
            Toast.makeText(getActivity(), "Amount exceeds total amount. Please enter valid amount.", Toast.LENGTH_SHORT).show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    void setTitle() {
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