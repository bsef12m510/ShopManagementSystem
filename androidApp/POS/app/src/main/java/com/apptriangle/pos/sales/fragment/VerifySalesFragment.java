package com.apptriangle.pos.sales.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import com.apptriangle.pos.R;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.adaptor.VerifySaleAdaptor;
import com.apptriangle.pos.sales.response.SalesResponse;
import com.apptriangle.pos.sales.restInterface.SalesService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zeeshan on 3/31/2018.
 */
public class VerifySalesFragment extends Fragment {
    ProgressDialog pd;
    String apiKey;
    private Button finishBtn;
    private OnFragmentInteractionListener mListener;
    private View contentView;
    private RecyclerView recyclerView;
    String[] listItems = {"item 1", "item 2 ", "list", "android" };
    private VerifySaleAdaptor adaptor;
    private List<Product> cart;
    SalesResponse sale;
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

      /*  pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");*/
//        pd.setCanceledOnTouchOutside(false);

        finishBtn = (Button)contentView.findViewById(R.id.finishButton);
        edtTotalAmnt = (EditText) contentView.findViewById(R.id.edtTotalAmnt);
        edtCustName = (EditText) contentView.findViewById(R.id.edtCustName);
        edtPaidAmnt = (EditText) contentView.findViewById(R.id.edtPaidAmnt);
        edtCustNo = (EditText) contentView.findViewById(R.id.edtCustNo);
        edtDueAmnt = (EditText) contentView.findViewById(R.id.edtDueAmnt);
        edtTotalAmnt.setEnabled(false);
        edtDueAmnt.setEnabled(false);
        edtPaidAmnt.addTextChangedListener(inputTextWatcher);
        finishBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(validate()) {
                    updateCart();
                    processSale();
                }else{
                    Toast.makeText(getActivity(),"Please provide all information..",Toast.LENGTH_SHORT).show();
                }

            }
        });
        recyclerView = (RecyclerView) contentView.findViewById(R.id.sales_recycler_view);
        ArrayList<SalesResponse> stockResponseArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SalesResponse tmp = new SalesResponse();
            stockResponseArrayList.add(tmp);
        }
        for (int i = 0; i < sale.products.size(); i++) {
            sale.products.get(i).setChecked(true);
        }

//        adaptor = new VerifySaleAdaptor(getActivity(), stockResponseArrayList, false);
        adaptor = new VerifySaleAdaptor(getActivity(), sale.products, false);
        adaptor.parentFrag = VerifySalesFragment.this;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptor);
    }

    private void getApiKey()
    {
        SharedPreferences shared = getActivity().getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        apiKey = shared.getString("api_key", "");

    }

    public void processSale(){
//        RequestBody emailParam = RequestBody.create(MediaType.parse("application/json"), sale);
        sale.total_amount = totalAmount;
        sale.amount_paid = paidAmount;
        sale.discount = "0";
        sale.cust_name = edtCustName.getText().toString().trim();
        sale.cust_phone = edtCustNo.getText().toString().trim();

        SalesService service =
                ApiClient.getClient().create(SalesService.class);


        Call<Object> call = service.processSale(sale);
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                pd.dismiss();
                if (response != null) {
                   if((Double)response.body() > 0) {
                       sale.sale_id = (Double)response.body();
                       Toast.makeText(getActivity(), "DONE " + sale.sale_id, Toast.LENGTH_SHORT).show();
                       onFinishPressed();
                   }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.dismiss();

            }
        });
    }

    public boolean validate(){
        if(edtCustName.getText().toString().trim().equals(""))
            return false;
        else if(edtCustNo.getText().toString().trim().equals(""))
            return false;
        else if(edtTotalAmnt.getText().toString().trim().equals(""))
            return false;
        else if(edtPaidAmnt.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onFinishPressed() {
        if (mListener != null) {
            mListener.onFinishClicked(sale);
        }
    }

    public void updateCart(){

        for (int i = 0; i < sale.products.size(); i++) {
            if(!sale.products.get(i).isChecked())
                sale.products.remove(i);
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

    public void setCart(SalesResponse sale) {
        this.sale = sale;
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
        void onFinishClicked(SalesResponse sale);
    }
}