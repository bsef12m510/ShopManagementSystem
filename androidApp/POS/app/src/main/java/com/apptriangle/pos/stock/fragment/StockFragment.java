package com.apptriangle.pos.stock.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.apptriangle.pos.R;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.restInterface.SalesService;
import com.apptriangle.pos.stock.adaptor.StockAdaptor;
import com.apptriangle.pos.stock.response.StockResponse;
import com.apptriangle.pos.stock.service.StockService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    String apiKey;
    ProgressDialog pd;
    List<StockResponse> inventoryProducts;
    private EditText searchText;
    RadioGroup radioGroup;
    Integer radioOpt = null;
    Button btnSearch;

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
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");
        pd.setCanceledOnTouchOutside(false);
        lytEmpty = (LinearLayout) contentView.findViewById(R.id.lytEmpty);
        btnSearch =(Button)contentView.findViewById(R.id.btnSearch);
        searchText = (EditText)contentView.findViewById(R.id.searchText);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.my_recycler_view);

        radioGroup =(RadioGroup)contentView.findViewById(R.id.radioGroup1);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.radio0:
                        radioOpt = 0;
                        break;
                    case R.id.radio1:
                        radioOpt = 1;
                        break;
                    case R.id.radio2:
                        radioOpt = 2;
                        break;
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchText.getText().toString().trim().equals("") && radioOpt == 0 )
                    searchByProduct();
                else if(!searchText.getText().toString().trim().equals("") && radioOpt == 1 )
                    searchByBrand();
                else if(!searchText.getText().toString().trim().equals("") && radioOpt == 2 )
                    searchByModel();
            }
        });

        setTitle();
        getApiKey();
        getAllInventoryProducts();



    }

    private void getApiKey()
    {
        SharedPreferences shared = getActivity().getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        apiKey = shared.getString("api_key", "");

    }

    public void getAllInventoryProducts(){
        StockService service =
                ApiClient.getClient().create(StockService.class);


        Call<List<StockResponse>> call = service.getAllInventoryProducts(apiKey);
        pd.show();
        call.enqueue(new Callback<List<StockResponse>>() {
            @Override
            public void onResponse(Call<List<StockResponse>> call, Response<List<StockResponse>> response) {
                pd.hide();
                if (response != null) {
                    inventoryProducts = response.body();
                    if (inventoryProducts.size() > 0)
                        hideEmptyView();
                    else
                        showEmptyView();
                    adapter = new StockAdaptor(getActivity(), inventoryProducts);

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<StockResponse>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    public void searchByProduct(){
        StockService service =
                ApiClient.getClient().create(StockService.class);


        Call<List<StockResponse>> call = service.searchByProduct("hrauf",searchText.getText().toString().trim() );
        pd.show();
        call.enqueue(new Callback<List<StockResponse>>() {
            @Override
            public void onResponse(Call<List<StockResponse>> call, Response<List<StockResponse>> response) {
                pd.hide();
                if (response != null) {
                    inventoryProducts = response.body();
                    if (inventoryProducts.size() > 0)
                        hideEmptyView();
                    else
                        showEmptyView();
                    adapter = new StockAdaptor(getActivity(), inventoryProducts);

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<StockResponse>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    public void searchByBrand(){
        StockService service =
                ApiClient.getClient().create(StockService.class);


        Call<List<StockResponse>> call = service.searchByBrand(apiKey,searchText.getText().toString().trim() );
        pd.show();
        call.enqueue(new Callback<List<StockResponse>>() {
            @Override
            public void onResponse(Call<List<StockResponse>> call, Response<List<StockResponse>> response) {
                pd.hide();
                if (response != null) {
                    inventoryProducts = response.body();
                    if (inventoryProducts.size() > 0)
                        hideEmptyView();
                    else
                        showEmptyView();
                    adapter = new StockAdaptor(getActivity(), inventoryProducts);

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<StockResponse>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    public void searchByModel(){
        StockService service =
                ApiClient.getClient().create(StockService.class);


        Call<List<StockResponse>> call = service.searchByModel(apiKey,searchText.getText().toString().trim() );
        pd.show();
        call.enqueue(new Callback<List<StockResponse>>() {
            @Override
            public void onResponse(Call<List<StockResponse>> call, Response<List<StockResponse>> response) {
                pd.hide();
                if (response != null) {
                    inventoryProducts = response.body();
                    if (inventoryProducts.size() > 0)
                        hideEmptyView();
                    else
                        showEmptyView();
                    adapter = new StockAdaptor(getActivity(), inventoryProducts);

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<StockResponse>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onSalesClick() {
        if (mListener != null) {
            mListener.onStockFragmentListener();
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
        ((Activity) getActivity()).setTitle("STOCK");
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
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
        void onStockFragmentListener();
    }
}
