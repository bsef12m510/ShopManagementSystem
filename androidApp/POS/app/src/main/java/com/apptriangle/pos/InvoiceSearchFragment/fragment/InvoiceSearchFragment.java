package com.apptriangle.pos.InvoiceSearchFragment.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RadioGroup;

import com.apptriangle.pos.InvoiceSearchFragment.adapter.InvoiceAdapter;
import com.apptriangle.pos.InvoiceSearchFragment.service.InvoiceService;
import com.apptriangle.pos.R;
import com.apptriangle.pos.SecureActivity;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.model.CSale;
import com.apptriangle.pos.model.Invoice;
import com.apptriangle.pos.sales.adaptor.VerifySaleAdaptor;
import com.apptriangle.pos.sales.fragment.InvoiceFragment;
import com.apptriangle.pos.sales.response.SalesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceSearchFragment extends Fragment {
    private String apiKey;
    private ProgressDialog pd;
    private InvoiceSearchFragment.OnFragmentInteractionListener mListener;
    private View contentView;
    private RecyclerView recyclerView;
    String[] listItems = {"item 1", "item 2 ", "list", "android"};
    private InvoiceAdapter adaptor;
    public boolean fromHome = false;
    private CardView searchContainer1, searchContainer2;
    private NestedScrollView saleDescContainer;
    public SalesResponse cart;
    public Button finishBtn;
    public EditText searchText;
    public Invoice invObj;
    public List<Invoice> invoiceList = new ArrayList<>();
    public Double totalAmount, paidAmount, dueAmount;
    public EditText edtCustName, edtCustNo, edtTotalAmnt, edtPaidAmnt, edtDueAmnt;
    public Invoice selectedInvoice;

    TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().trim().equalsIgnoreCase("")) {
                paidAmount = Double.parseDouble(s.toString());
                dueAmount = totalAmount - paidAmount;
                if (dueAmount < 0)
                    dueAmount = 0.0;
                edtDueAmnt.setText(dueAmount.toString());
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
    private RadioGroup radioGroup;
    private Button btSearc;

    public InvoiceSearchFragment() {
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
        contentView = inflater.inflate(R.layout.fragment_invoice_search, container, false);
        return contentView;
    }

    private void getApiKey() {
        SharedPreferences shared = getActivity().getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        apiKey = shared.getString("api_key", "");

    }

    public void initialize() {
        getApiKey();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");
        pd.setCanceledOnTouchOutside(false);
        btSearc = (Button) contentView.findViewById(R.id.btSearch);
        edtTotalAmnt = (EditText) contentView.findViewById(R.id.edtTotalAmnt);
        searchText = (EditText) contentView.findViewById(R.id.searchText);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.invoice_recycler_view);
        searchContainer1 = (CardView) contentView.findViewById(R.id.searchContainer1);
        searchContainer2 = (CardView) contentView.findViewById(R.id.searchContainer2);



        radioGroup = (RadioGroup) contentView.findViewById(R.id.radioGroup1);

        btSearc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(radioGroup.getCheckedRadioButtonId() == R.id.radio0)
                    getInvoiceData();
                else if(radioGroup.getCheckedRadioButtonId() == R.id.radio1){
                    getInvoiceDataByCell();
                }
            }
        });



    }


    public void getInvoiceData() {
        InvoiceService service =
                ApiClient.getClient().create(InvoiceService.class);
        Call<Invoice> call;
        call = service.getInvoiceById(apiKey, searchText.getText().toString());
        pd.show();
        call.enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                pd.hide();
                if (response != null && response.body() != null) {
                    invoiceList.clear();
                    invObj = (Invoice) response.body();
                    invoiceList.add(invObj);
                    if (invoiceList != null) {
                        adaptor = new InvoiceAdapter(getActivity(), invoiceList, true);
                        adaptor.parentFrag = InvoiceSearchFragment.this;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(adaptor);
                    }
                }
            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    public void getInvoiceDataByCell() {
        InvoiceService service =
                ApiClient.getClient().create(InvoiceService.class);
        Call<List<Invoice>> call;
        call = service.getInvoiceByCell(apiKey, searchText.getText().toString());
        pd.show();
        call.enqueue(new Callback<List<Invoice>>() {
            @Override
            public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
                pd.hide();
                if (response != null && response.body() != null) {
                    invoiceList.clear();
                    invoiceList = (List<Invoice>) response.body();
//                    invoiceList.add(invObj);
                    if (invoiceList != null) {
                        adaptor = new InvoiceAdapter(getActivity(), invoiceList, true);
                        adaptor.parentFrag = InvoiceSearchFragment.this;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(adaptor);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Invoice>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    public void updateInvoice() {

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onInvoiceSearchFragmentInteraction(selectedInvoice);
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
            mListener = (InvoiceSearchFragment.OnFragmentInteractionListener) activity;
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
        void onInvoiceSearchFragmentInteraction( Invoice selectedInvoice);
    }
}