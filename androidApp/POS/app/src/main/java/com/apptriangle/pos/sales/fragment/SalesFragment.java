package com.apptriangle.pos.sales.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apptriangle.pos.R;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.dashboard.fragment.DashboardFragment;
import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.response.GetProductsResponse;
import com.apptriangle.pos.sales.restInterface.SalesService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zawan on 3/30/18.
 */

public class SalesFragment extends Fragment {
    private String apiKey;
    private Spinner productsDropdown, brandsDropdown;
    private OnFragmentInteractionListener mListener;
    private View contentView;
    private GetProductsResponse selectedProduct;
    private Brand selectedBrand;
    private Button checkoutBtn, btnMore;
    private ProgressDialog pd;
    private EditText edtQty, edtPrice, edtDiscount, edtTotalPrice;
    private List<Product> cart;
    Double price = 0.0, totalPrice = 0.0, lastTotalPrice = 0.0;
    TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if(s != null && !s.toString().trim().equalsIgnoreCase("")) {
                price = Integer.parseInt(s.toString()) * selectedProduct.getProduct().getUnitPrice();
                edtPrice.setText(price.toString());
                totalPrice = lastTotalPrice + price;
                edtTotalPrice.setText(totalPrice.toString());
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public SalesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize(contentView);
        setTitle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_sales, container, false);
        return contentView;
    }

    public void initialize(View view){

        cart = new ArrayList<>();
        getApiKey();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");
        pd.setCanceledOnTouchOutside(false);

        productsDropdown = (Spinner) view.findViewById(R.id.productsDropdown);
        brandsDropdown = (Spinner) view.findViewById(R.id.brandsDropdown);
        checkoutBtn = (Button)view.findViewById(R.id.checkoutBtn);
        btnMore =(Button)view.findViewById(R.id.btnMore);
        edtQty = (EditText)view.findViewById(R.id.edtQty);
        edtQty.addTextChangedListener(inputTextWatcher);
        edtPrice = (EditText)view.findViewById(R.id.edtPrice);
        edtDiscount = (EditText)view.findViewById(R.id.edtDiscount);
        edtTotalPrice = (EditText)view.findViewById(R.id.edtTotalPrice);

        checkoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onCheckoutButtonPressed();
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addProductToCart();
            }
        });
        getAllProducts();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onCheckoutButtonPressed() {
        if (mListener != null) {
            mListener.onCheckoutListener();
        }
    }

    public void addProductToCart(){
        lastTotalPrice = totalPrice;
        Product product = selectedProduct.getProduct();
        product.setOtherThanCurrentInventoryQty(Integer.parseInt(edtQty.getText().toString()));
        productsDropdown.setSelection(0);
        brandsDropdown.setSelection(0);
        edtQty.setText("");
        edtPrice.setText("");
        edtDiscount.setText("");
        edtTotalPrice.setText(totalPrice.toString());

        cart.add(product);
        Toast.makeText(getActivity(),"Product added to card...",Toast.LENGTH_SHORT).show();

    }

    private void getApiKey()
    {
        SharedPreferences shared = getActivity().getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        apiKey = shared.getString("api_key", "");

    }

    public void getAllProducts(){
        SalesService service =
                ApiClient.getClient().create(SalesService.class);

        Call<List<GetProductsResponse>> call = service.getAllProducts(apiKey);
        pd.show();
        call.enqueue(new Callback<List<GetProductsResponse>>() {
            @Override
            public void onResponse(Call<List<GetProductsResponse>> call, Response<List<GetProductsResponse>> response) {
                pd.hide();
                if (response != null) {
                    ArrayList<GetProductsResponse> productsList = (ArrayList<GetProductsResponse>) response.body();
                    GetProductsResponse tmp = new GetProductsResponse();
                    tmp.setProduct(new Product());
                    tmp.getProduct().setProductName("Select Product");
                    productsList.add(0,tmp);
                    setDropdown(productsList, productsDropdown);

                }
            }

            @Override
            public void onFailure(Call<List<GetProductsResponse>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    public void getBrands(){
        SalesService service =
                ApiClient.getClient().create(SalesService.class);

        Call<List<Brand>> call = service.getBrands(apiKey, selectedProduct.getProduct().getProductType().getTypeId());
        pd.show();
        call.enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                pd.hide();
                if (response != null) {
                    ArrayList<Brand> brandsList = (ArrayList<Brand>) response.body();
                    Brand tmp = new Brand();
                    tmp.setBrandName("Select Brand");

                    brandsList.add(0,tmp);
                    setBrandDropdown(brandsList, brandsDropdown);

                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    void setTitle()
    {
        ((Activity) getActivity()).setTitle("SALE");
    }

    public void setDropdown(final List<GetProductsResponse> list, Spinner dropdown) {
        final ArrayAdapter<GetProductsResponse> spinnerArrayAdapter = new ArrayAdapter<GetProductsResponse>(
                getActivity(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.collapsed_spinner, parent, false);
//                View view = super.getDropDownView(position, convertView, parent);

//                TextView label = (TextView) view.findViewById(R.id.label);
                TextView value = (TextView) view.findViewById(R.id.value);

//                label.setText(text);
//                label.setMaxLines(1);
                value.setText(list.get(position).getProduct().getProductName());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        dropdown.setAdapter(spinnerArrayAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedProduct = (GetProductsResponse) parent.getItemAtPosition(position);
                if(position != 0)
                    getBrands();
                // If user change the default selection
                // First item is disable and it is used for hint

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dropdown.setAdapter(spinnerArrayAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dropdown.setLayoutMode(android.R.layout.select_dialog_item);
        }


            /*if (currentForeignId != null) {
                // list.indexOf()
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getValue().equalsIgnoreCase(currentForeignId) || list.get(i).getKey().equalsIgnoreCase(currentForeignId)) { //getkey()
                        dropdown.setSelection(i);
                        break;
                    }
                }
            }*/

    }

    public void setBrandDropdown(final List<Brand> list, Spinner dropdown) {
        final ArrayAdapter<Brand> spinnerArrayAdapter = new ArrayAdapter<Brand>(
                getActivity(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.collapsed_spinner, parent, false);
//                View view = super.getDropDownView(position, convertView, parent);

//                TextView label = (TextView) view.findViewById(R.id.label);
                TextView value = (TextView) view.findViewById(R.id.value);

//                label.setText(text);
//                label.setMaxLines(1);
                value.setText(list.get(position).getBrandName());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        dropdown.setAdapter(spinnerArrayAdapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedBrand = (Brand) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dropdown.setAdapter(spinnerArrayAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dropdown.setLayoutMode(android.R.layout.select_dialog_item);
        }


            /*if (currentForeignId != null) {
                // list.indexOf()
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getValue().equalsIgnoreCase(currentForeignId) || list.get(i).getKey().equalsIgnoreCase(currentForeignId)) { //getkey()
                        dropdown.setSelection(i);
                        break;
                    }
                }
            }*/
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
        void onCheckoutListener();
    }
}