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
import com.apptriangle.pos.model.ProductType;
import com.apptriangle.pos.sales.response.GetProductsResponse;
import com.apptriangle.pos.sales.response.SalesResponse;
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
    SalesResponse sale;
    private Spinner productTypesDropdown, brandsDropdown, modelsDropdown;
    private OnFragmentInteractionListener mListener;
    private View contentView;
    private Product selectedProduct;
    private ProductType selectedProductType;
    private Brand selectedBrand;
    private Button checkoutBtn, btnMore;
    private ProgressDialog pd;
    private EditText edtQty, edtPrice, edtDiscount, edtTotalPrice, edtUoM;
    private List<Product> cart;
    Double price = 0.0, totalPrice = 0.0, lastTotalPrice = 0.0;
    TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if(s != null && !s.toString().trim().equalsIgnoreCase("")) {
                price = Integer.parseInt(s.toString()) * selectedProduct.getUnitPrice();
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
        sale = new SalesResponse();
        cart = new ArrayList<>();

        getApiKey();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");
        pd.setCanceledOnTouchOutside(false);

        productTypesDropdown = (Spinner) view.findViewById(R.id.productsDropdown);
        brandsDropdown = (Spinner) view.findViewById(R.id.brandsDropdown);
        modelsDropdown = (Spinner) view.findViewById(R.id.modelsDropdown);
        checkoutBtn = (Button)view.findViewById(R.id.checkoutBtn);
        btnMore =(Button)view.findViewById(R.id.btnMore);
        edtQty = (EditText)view.findViewById(R.id.edtQty);
        edtUoM = (EditText)view.findViewById(R.id.edtUoM);
        edtUoM.setEnabled(false);
        edtQty.addTextChangedListener(inputTextWatcher);
        edtPrice = (EditText)view.findViewById(R.id.edtPrice);
        edtDiscount = (EditText)view.findViewById(R.id.edtDiscount);
        edtTotalPrice = (EditText)view.findViewById(R.id.edtTotalPrice);

        checkoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(validate()) {
                    addProductToCart();
                    onCheckoutButtonPressed();
                }else{
                    Toast.makeText(getActivity(),"Provide all information..",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnMore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(validate())
                    addProductToCart();
                else
                    Toast.makeText(getActivity(),"Provide all information..",Toast.LENGTH_SHORT).show();
            }
        });
        getAllProductTypes();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onCheckoutButtonPressed() {
        if (mListener != null) {
            sale.products = new ArrayList<>();
            sale.products.addAll(cart);
            sale.total_amount = totalPrice;
            sale.apiKey = apiKey;
            mListener.onCheckoutListener(sale);
        }
    }

    public void addProductToCart(){
        lastTotalPrice = totalPrice;
        Product product = selectedProduct;
        product.setBrand(selectedBrand);
        product.setProductType(selectedProductType);
        product.setOtherThanCurrentInventoryQty(Integer.parseInt(edtQty.getText().toString()));
        product.setQty(Integer.parseInt(edtQty.getText().toString()));
        productTypesDropdown.setSelection(0);
        brandsDropdown.setSelection(0);
        modelsDropdown.setSelection(0);
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

    public void getAllProductTypes(){
        SalesService service =
                ApiClient.getClient().create(SalesService.class);

        Call<List<ProductType>> call = service.getAllProductTypes(apiKey);
        pd.show();
        call.enqueue(new Callback<List<ProductType>>() {
            @Override
            public void onResponse(Call<List<ProductType>> call, Response<List<ProductType>> response) {
                pd.hide();
                if (response != null) {
                    ArrayList<ProductType> productsList = (ArrayList<ProductType>) response.body();
                    ProductType tmp = new ProductType();

                    tmp.setTypeName("Select Product Type");
                    productsList.add(0,tmp);
                    setProductTypesDropdown(productsList, productTypesDropdown);

                }
            }

            @Override
            public void onFailure(Call<List<ProductType>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    public void getAllModelsForProductType(){
        SalesService service =
                ApiClient.getClient().create(SalesService.class);

        Call<List<Product>> call = service.getModels(apiKey,selectedProductType.getTypeId(),selectedBrand.getBrandId());
        pd.show();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                pd.hide();
                if (response != null) {
                    ArrayList<Product> productsList = (ArrayList<Product>) response.body();
                    Product tmp = new Product();

                    tmp.setProductName("Select Model");
                    productsList.add(0,tmp);
                    setModelsDropdown(productsList, modelsDropdown);

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.hide();

            }
        });
    }

    public void getBrands(){
        SalesService service =
                ApiClient.getClient().create(SalesService.class);

        Call<List<Brand>> call = service.getBrands(apiKey, selectedProductType.getTypeId());
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


    public boolean validate(){
        if(selectedProductType == null || selectedProductType.getTypeId() == null)
            return false;
        else if(selectedBrand == null || selectedBrand.getBrandId() == null)
            return false;
        else if(selectedProduct == null ||selectedProduct.getProductId() == null)
            return false;
        else if(edtQty.getText().toString().trim().equalsIgnoreCase(""))
            return false;
        else
            return true;
    }

    void setTitle()
    {
        ((Activity) getActivity()).setTitle("SALE");
    }

    public void setProductTypesDropdown(final List<ProductType> list, Spinner dropdown) {
        final ArrayAdapter<ProductType> spinnerArrayAdapter = new ArrayAdapter<ProductType>(
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
                value.setText(list.get(position).getTypeName());
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

                selectedProductType = (ProductType) parent.getItemAtPosition(position);
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

    public void setModelsDropdown(final List<Product> list, Spinner dropdown) {
        final ArrayAdapter<Product> spinnerArrayAdapter = new ArrayAdapter<Product>(
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
                value.setText(list.get(position).getProductName());
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

                selectedProduct = (Product) parent.getItemAtPosition(position);
                edtUoM.setText(selectedProduct.getUnitOfMsrmnt().getDescription());
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
                if(position != 0)
                    getAllModelsForProductType();
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
        if(productTypesDropdown != null)
            productTypesDropdown.setSelection(0);
        if(null != brandsDropdown)
             brandsDropdown.setSelection(0);
        if(null != modelsDropdown)
             modelsDropdown.setSelection(0);
        if(edtQty != null)
            edtQty.setText("");
        if(edtPrice != null) {

            edtPrice.setText("");
        }
        if(edtDiscount != null) {

            edtDiscount.setText("");
        }
        if(edtTotalPrice != null) {

            edtTotalPrice.setText("");
        }
        price = 0.0;
        totalPrice = 0.0;
        lastTotalPrice = 0.0;
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
        void onCheckoutListener(SalesResponse sale);
    }
}