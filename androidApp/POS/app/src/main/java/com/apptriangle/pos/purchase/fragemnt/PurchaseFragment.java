package com.apptriangle.pos.purchase.fragemnt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apptriangle.pos.R;
import com.apptriangle.pos.SecureActivity;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.model.ProductType;
import com.apptriangle.pos.model.UoM;
import com.apptriangle.pos.purchase.response.JProduct;
import com.apptriangle.pos.purchase.response.PurchaseResponse;
import com.apptriangle.pos.purchase.service.PurchaseService;
import com.apptriangle.pos.sales.restInterface.SalesService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zeeshan on 4/4/2018.
 */
public class PurchaseFragment extends Fragment {
    private String apiKey;
    public static String HALF_MONTH_DATE_FORMAT = "MMM dd, yyyy";
    String m_Text = "Product";
    private OnFragmentInteractionListener mListener;
    private View contentView;
    String[] listItems = {"item 1", "item 2 ", "list", "android"};
    private Button checkoutBtn, btnMore;
    private ImageButton addProductBtn, addBrandBtn, editProductBtn, editBrandBtn, addModelBtn, editModelBtn, deleteBrandBtn, deleteProductTypeBtn, deleteModelBtn, addUoMBtn, editUoMBtn;
    private Spinner productTypesDropdown, brandsDropdown, modelsDropdown, uoMDropdown;
    private String pickerDateSring;
    private String newProductString, editProductSting, newBrandString, editBrandString, newModelString, editModelString, newUoMString, editUoMString;
    private TextView txtDate;
    private LinearLayout dateContainer;

    private EditText edtSupplier, edtInvoice, edtQty, edtPrice, edtTotalPrice, edtSpecs;
    private ScrollView scrollView;
    private ProgressDialog pd;
    private JProduct selectedProduct;
    private ProductType selectedProductType;
    private Brand selectedBrand;
    private UoM selectedUoM;
    Double price = 0.0, totalPrice = 0.0, lastTotalPrice = 0.0;
    private List<JProduct> cart;
    ArrayList<ProductType> productsTypeList;
    ArrayList<JProduct> productsList;
    ArrayList<Brand> brandsList;
    ArrayList<UoM> uoMList;
    public PurchaseResponse purchase;
    public Map<ProductType, List<Brand>> brandsMap = new HashMap<>();
    public Map<Brand, List<JProduct>> modelsMap = new HashMap<>();
    public boolean isFirst = true;

    TextWatcher inputTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().trim().equalsIgnoreCase("")) {
                if (!edtPrice.getText().toString().equals("")) {
                   /* price = Integer.parseInt(s.toString()) * selectedProduct.getUnitPrice();
                    edtPrice.setText(price.toString());
                    totalPrice = lastTotalPrice + price;
                    edtTotalPrice.setText(totalPrice.toString());*/

                    price = Double.parseDouble(edtPrice.getText().toString());
                    totalPrice = lastTotalPrice + (price * Double.parseDouble(edtQty.getText().toString()));
                    edtTotalPrice.setText(Double.toString(price * Double.parseDouble(edtQty.getText().toString()))+"TK");
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    TextWatcher priceTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (s != null && !s.toString().trim().equalsIgnoreCase("")) {
                if (!edtQty.getText().toString().equals("")) {
                    price = Double.parseDouble(edtPrice.getText().toString());
                    totalPrice = lastTotalPrice + (price * Double.parseDouble(edtQty.getText().toString()));
                    edtTotalPrice.setText(Double.toString(price * Double.parseDouble(edtQty.getText().toString()))+"TK");
                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    public PurchaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize(contentView);
        setBrandsDropdownLabel();
        setModelsDropdownLabel();
        setTitle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_purchase, container, false);
        return contentView;
    }

    public void initialize(View view) {
        cart = new ArrayList<>();
        purchase = new PurchaseResponse();

        getApiKey();
        /*pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");*/
       /* pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);*/

        checkoutBtn = (Button) view.findViewById(R.id.checkoutBtn);
        btnMore = (Button) view.findViewById(R.id.btnMore);
        addProductBtn = (ImageButton) view.findViewById(R.id.addProductBtn);
        addBrandBtn = (ImageButton) view.findViewById(R.id.addBrandBtn);
        editProductBtn = (ImageButton) view.findViewById(R.id.editProductBtn);
        editBrandBtn = (ImageButton) view.findViewById(R.id.editBrandBtn);
        addModelBtn = (ImageButton) view.findViewById(R.id.addModelBtn);
        addUoMBtn = (ImageButton) view.findViewById(R.id.addUoMBtn);
        editModelBtn = (ImageButton) view.findViewById(R.id.editModelBtn);
        editUoMBtn = (ImageButton) view.findViewById(R.id.editUoMBtn);
        deleteModelBtn = (ImageButton) view.findViewById(R.id.deleteModelBtn);
        deleteBrandBtn = (ImageButton) view.findViewById(R.id.deleteBrandBtn);
        deleteProductTypeBtn = (ImageButton) view.findViewById(R.id.deleteProductBtn);
        productTypesDropdown = (Spinner) view.findViewById(R.id.productsDropdown);
        brandsDropdown = (Spinner) view.findViewById(R.id.brandsDropdown);
        modelsDropdown = (Spinner) view.findViewById(R.id.modelsDropdown);
        uoMDropdown = (Spinner) view.findViewById(R.id.uoMDropdown);
        dateContainer = (LinearLayout) view.findViewById(R.id.dateContainer);
        txtDate = (TextView) view.findViewById(R.id.txtDate);

        edtSupplier = (EditText) view.findViewById(R.id.edtSupplierDetails);
        edtInvoice = (EditText) view.findViewById(R.id.txtInvoiceNo);
        edtQty = (EditText) view.findViewById(R.id.edtQty);
        edtQty.addTextChangedListener(inputTextWatcher);
        edtPrice = (EditText) view.findViewById(R.id.edtPrice);
        edtPrice.addTextChangedListener(priceTextWatcher);
        edtTotalPrice = (EditText) view.findViewById(R.id.edtTotalPrice);
        edtSpecs = (EditText) view.findViewById(R.id.edtSpecs);

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        edtSupplier.setSingleLine(false);
        edtSupplier.setHorizontallyScrolling(false);
        edtSupplier.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        getAllUoM();

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
        editProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(2);
            }
        });
        addBrandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(3);
            }
        });
        editBrandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(4);
            }
        });
        addModelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(5);
            }
        });
        editModelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(6);
            }
        });
        addUoMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(7);
            }
        });
        editUoMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(8);
            }
        });
        deleteModelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(11);
            }
        });
        deleteBrandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(10);
            }
        });
        deleteProductTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(9);
            }
        });

        dateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(txtDate);
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    addProductToCart();
                    processPurchase();
                }

            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                    addProductToCart();
                else
                    Toast.makeText(getActivity(), "Provide all information..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void processPurchase() {
        purchase.products = new ArrayList<>();
        purchase.products.addAll(cart);
        purchase.total_amount = totalPrice;
        purchase.amount_paid = totalPrice;
        purchase.dlr_info = edtSupplier.getText().toString();
        purchase.purch_id = Integer.parseInt(edtInvoice.getText().toString());
        try {
            String dateString = txtDate.getText().toString();
            Date dt = Calendar.getInstance().getTime();
            int hours = dt.getHours();
            int minutes = dt.getMinutes();
            int seconds = dt.getSeconds();
            dateString = dateString + " " + hours + ":" + minutes + ":" + seconds;
            SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss");
            java.util.Date date = sdf1.parse(dateString);
            Timestamp sqlStartDate = new Timestamp(date.getTime());
            purchase.purch_dtime = sqlStartDate;
        } catch (Exception e) {
        }
        purchase.apiKey = apiKey;


        PurchaseService service =
                ApiClient.getClient().create(PurchaseService.class);


        Call<Object> call = service.processPurchase(purchase);
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                pd.dismiss();
                if (response != null) {
                    if (response.body() instanceof String) {
                        if ((String) response.body() != null) {

                            Toast.makeText(getActivity(), "DONE " + purchase.purch_id, Toast.LENGTH_SHORT).show();
                            productTypesDropdown.setSelection(0);
                            brandsDropdown.setSelection(0);
                            modelsDropdown.setSelection(0);
                            uoMDropdown.setSelection(0);
                            edtQty.setText("");
                            edtPrice.setText("");
                            edtTotalPrice.setText("");
                            edtSpecs.setText("");
                            txtDate.setText("Date");
                            edtInvoice.setText("");
                            edtSupplier.setText("");
                            price = 0.0;
                            lastTotalPrice = 0.0;
                            totalPrice = 0.0;
                            edtTotalPrice.setText("");
                            onCheckoutButtonPressed();
                            Intent intent = new Intent(getActivity(), SecureActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.dismiss();
                Intent intent = new Intent(getActivity(), SecureActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onCheckoutButtonPressed() {
        if (mListener != null) {
            mListener.onCheckoutListenerPurchase();
        }
    }

    void setTitle() {
        ((Activity) getActivity()).setTitle("PURCHASE");
    }


    public void addProductToCart() {
        lastTotalPrice = totalPrice;
        JProduct product = selectedProduct;
        product.setBrand(selectedBrand);
        product.setProductType(selectedProductType);
        product.setUnitOfMsrmnt(selectedUoM);
        product.setOtherThanCurrentInventoryQty(Integer.parseInt(edtQty.getText().toString()));
        product.setQty(Integer.parseInt(edtQty.getText().toString()));
        product.setUnitPrice(Double.parseDouble(edtPrice.getText().toString()));
        product.setSpecs(edtSpecs.getText().toString());
        setProductTypesDropdown(productsTypeList, productTypesDropdown, false, false);
        productTypesDropdown.setSelection(0);
        brandsDropdown.setSelection(0);
        modelsDropdown.setSelection(0);
        uoMDropdown.setSelection(0);
        edtQty.setText("");
        edtPrice.setText("");
        edtTotalPrice.setText("");
        edtSpecs.setText("");
        isFirst = false;
        cart.add(product);
        Toast.makeText(getActivity(), "Product added to card...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }

    public void showDialog(final int identifier) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (1 == identifier)
            builder.setTitle("New Product");
        else if (2 == identifier)
            builder.setTitle("Edit Product");
        else if (3 == identifier)
            builder.setTitle("New Brand");
        else if (4 == identifier)
            builder.setTitle("Edit Brand");
        else if (5 == identifier)
            builder.setTitle("New Model");
        else if (6 == identifier)
            builder.setTitle("Edit Model");
        else if (7 == identifier)
            builder.setTitle("New UoM");
        else if (8 == identifier)
            builder.setTitle("Edit UoM");
        else if (9 == identifier)
            builder.setTitle("Delete Product");
        else if (10 == identifier)
            builder.setTitle("Delete Brand");
        else if (11 == identifier)
            builder.setTitle("Delete Model");
// Set up the input
        final EditText input = new EditText(getActivity());
        input.setEnabled(true);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        if (identifier == 2)
            input.setText(selectedProductType != null ? selectedProductType.getTypeName() : "");
        else if (identifier == 4)
            input.setText(selectedBrand != null ? selectedBrand.getBrandName() : "");
        else if (identifier == 6)
            input.setText(selectedProduct != null ? selectedProduct.getProductName() : "");
        else if (identifier == 8)
            input.setText(selectedUoM != null ? selectedUoM.description : "");
        else if (identifier == 11) {
            input.setText(selectedProduct != null ? selectedProduct.getProductName() : "");
            input.setEnabled(false);
        } else if (identifier == 10) {
            input.setText(selectedBrand != null ? selectedBrand.getBrandName() : "");
            input.setEnabled(false);
        } else if (identifier == 9) {
            input.setText(selectedProductType != null ? selectedProductType.getTypeName() : "");
            input.setEnabled(false);
        }
        builder.setView(input);
        String btnLabel = (identifier == 1 || identifier == 3 || identifier == 5 || identifier == 7) ? "Add" : "Edit";
        if (identifier == 11 || identifier == 10 || identifier == 9)
            btnLabel = "Delete";

// Set up the buttons
        builder.setPositiveButton(btnLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (1 == identifier) {
                    if (validateProductType(input.getText().toString().trim())) {
                        newProductString = input.getText().toString().trim();
                        ProductType tmp = new ProductType();
                        tmp.setTypeName(newProductString);
                        if (productsTypeList == null)
                            productsTypeList = new ArrayList<>();
                        productsTypeList.add(tmp);
                        selectedProductType = tmp;

                        setProductTypesDropdown(productsTypeList, productTypesDropdown, true, false);
                    } else
                        Toast.makeText(getActivity(), "Added Already..", Toast.LENGTH_SHORT).show();
                } else if (2 == identifier) {

                    editProductSting = input.getText().toString().trim();
                    if (selectedProductType != null && selectedProductType.getTypeId() != null) {
                        for (int i = 0; i < productsTypeList.size(); i++) {
                            if (productsTypeList.get(i).getTypeId() == selectedProductType.getTypeId()) {
                                productsTypeList.get(i).setTypeName(editProductSting);
                                setProductTypesDropdown(productsTypeList, productTypesDropdown, true, true);
                                break;
                            }
                        }
                    } else if (selectedProductType != null && selectedProductType.getTypeId() == null && (selectedProductType.getTypeName() != null || selectedProductType.getTypeName() != "")) {
                        for (int i = 0; i < productsTypeList.size(); i++) {
                            if (productsTypeList.get(i).getTypeName().equalsIgnoreCase(selectedProductType.getTypeName())) {
                                productsTypeList.get(i).setTypeName(editProductSting);
                                setProductTypesDropdown(productsTypeList, productTypesDropdown, true, true);
                                break;
                            }
                        }
                    }

                } else if (3 == identifier) {
                    if (validateBrand(input.getText().toString().trim())) {
                        newBrandString = input.getText().toString();
                        Brand tmp = new Brand();
                        tmp.setBrandName(newBrandString);

                        if (brandsList == null)
                            brandsList = new ArrayList<>();
//                    if(brandsList.size() == 1 && brandsList.get(0).getBrandName().equalsIgnoreCase("Select Brand"))

                        brandsList.add(tmp);
                        if (selectedProductType.getTypeId() == null)
                            brandsMap.put(selectedProductType, brandsList);
/*                    if(!brandsMap.containsKey(selectedProductType))
                        brandsMap.put(selectedProductType,new ArrayList<Brand>());
                    brandsMap.get(selectedProductType).add(tmp);*/
                        selectedBrand = tmp;
                        if (!isFirst && selectedProductType.getTypeId() == null) {

                            setBrandDropdown(brandsMap.get(selectedProductType), brandsDropdown, true, false);
                        } else
                            setBrandDropdown(brandsList, brandsDropdown, true, false);
                    } else
                        Toast.makeText(getActivity(), "Added Already..", Toast.LENGTH_SHORT).show();
                } else if (4 == identifier) {
                    String old = "";
                    editBrandString = input.getText().toString();
                    if (selectedBrand != null && selectedBrand.getBrandId() != null) {
                        for (int i = 0; i < brandsList.size(); i++) {
                            if (brandsList.get(i).getBrandId() == selectedBrand.getBrandId()) {
                                old = brandsList.get(i).getBrandName();
                                brandsList.get(i).setBrandName(editBrandString);
//                                modelsMap.put(brandsList.get(i),modelsMap.get(old));
                                setBrandDropdown(brandsList, brandsDropdown, true, true);
                                break;
                            }
                        }
                    } else if (selectedBrand != null && selectedBrand.getBrandId() == null && (selectedBrand.getBrandName() != null || selectedBrand.getBrandName() != "")) {
                        for (int i = 0; i < brandsList.size(); i++) {
                            if (brandsList.get(i).getBrandName().equalsIgnoreCase(selectedBrand.getBrandName())) {
                                old = brandsList.get(i).getBrandName();
                                brandsList.get(i).setBrandName(editBrandString);
//                                modelsMap.put(brandsList.get(i),modelsMap.get(old));
                                setBrandDropdown(brandsList, brandsDropdown, true, true);
                                break;
                            }
                        }
                    }
                } else if (5 == identifier) {
                    if (validateModel(input.getText().toString().trim())) {
                        newModelString = input.getText().toString();
                        JProduct tmp = new JProduct();
                        tmp.setProductName(newModelString);
                        if (productsList == null)
                            productsList = new ArrayList<>();
                        productsList.add(tmp);
                        selectedProduct = tmp;
                        if (selectedBrand.getBrandId() == null)
                            modelsMap.put(selectedBrand, productsList);
                        if (!isFirst && selectedBrand.getBrandId() == null) {
                       /* if(!modelsMap.containsKey(selectedBrand))
                            modelsMap.put(selectedBrand,new ArrayList<JProduct>());
                        modelsMap.get(selectedBrand).add(tmp);*/
                            setModelsDropdown(modelsMap.get(selectedBrand), modelsDropdown, true);
                        } else
                            setModelsDropdown(productsList, modelsDropdown, true);
                    } else
                        Toast.makeText(getActivity(), "Added Already..", Toast.LENGTH_SHORT).show();
                } else if (6 == identifier) {
                    editModelString = input.getText().toString();
                    if (selectedProduct != null && selectedProduct.getProductId() != null) {
                        for (int i = 0; i < productsList.size(); i++) {
                            if (productsList.get(i).getProductId() == selectedProduct.getProductId()) {
                                productsList.get(i).setProductName(editModelString);
                                setModelsDropdown(productsList, modelsDropdown, true);
                                break;
                            }
                        }
                    } else if (selectedProduct != null && selectedProduct.getProductId() == null && (selectedProduct.getProductName() != null || selectedProduct.getProductName() != "")) {
                        for (int i = 0; i < productsList.size(); i++) {
                            if (productsList.get(i).getProductName().equalsIgnoreCase(selectedProduct.getProductName())) {
                                productsList.get(i).setProductName(editModelString);
                                setModelsDropdown(productsList, modelsDropdown, true);
                                break;
                            }
                        }
                    }
                } else if (7 == identifier) {
                    if (validateUoM(input.getText().toString().trim())) {
                        newUoMString = input.getText().toString();
                        UoM tmp = new UoM();
                        tmp.setDescription(newUoMString);
                        if (uoMList == null)
                            uoMList = new ArrayList<>();
                        uoMList.add(tmp);
                        selectedUoM = tmp;
                        setUoMDropdown(uoMList, uoMDropdown, true);
                    } else
                        Toast.makeText(getActivity(), "Added Already..", Toast.LENGTH_SHORT).show();
                } else if (8 == identifier) {
                    editUoMString = input.getText().toString();
                    if (selectedUoM != null && selectedUoM.getSr_no() != null) {
                        for (int i = 0; i < uoMList.size(); i++) {
                            if (uoMList.get(i).getSr_no() == selectedUoM.getSr_no()) {
                                uoMList.get(i).setDescription(editUoMString);
                                setUoMDropdown(uoMList, uoMDropdown, true);
                                break;
                            }
                        }
                    } else if (selectedUoM != null && selectedUoM.getSr_no() == null && (selectedUoM.getDescription() != null || selectedUoM.getDescription() != "")) {
                        for (int i = 0; i < uoMList.size(); i++) {
                            if (uoMList.get(i).getDescription().equalsIgnoreCase(selectedUoM.getDescription())) {
                                uoMList.get(i).setDescription(editUoMString);
                                setUoMDropdown(uoMList, uoMDropdown, true);
                                break;
                            }
                        }
                    }
                } else if (9 == identifier) {
                    if (selectedProductType.getTypeId() != null)
                        deleteProductType();
//                    else{
                    for (int i = 0; i < productsTypeList.size(); i++) {
                        if (productsTypeList.get(i).getTypeName().equalsIgnoreCase(selectedProductType.getTypeName())) {
                            productsTypeList.remove(i);
                            break;
                        }
                    }
                    setProductTypesDropdown(productsTypeList, productTypesDropdown, false, false);
                    setBrandsDropdownLabel();
                    setModelsDropdownLabel();
//                    }
                } else if (10 == identifier) {
                    if (selectedBrand.getBrandId() != null)
                        deleteBrand();
//                    else{
                    for (int i = 0; i < brandsList.size(); i++) {
                        if (brandsList.get(i).getBrandName().equalsIgnoreCase(selectedBrand.getBrandName())) {
                            brandsList.remove(i);
                            break;
                        }
                    }
                    setBrandDropdown(brandsList, brandsDropdown, false, false);
                    setModelsDropdownLabel();
//                    }
                } else if (11 == identifier) {
                    if (selectedProduct.getProductId() != null)
                        deleteModel();
//                    else{
                    for (int i = 0; i < productsList.size(); i++) {
                        if (productsList.get(i).getProductName().equalsIgnoreCase(selectedProduct.getProductName())) {
                            productsList.remove(i);
                            break;
                        }
                    }
                    setModelsDropdown(productsList, modelsDropdown, false);
//                    }
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public boolean validateProductType(String name) {
        if (name == null || name.trim().equals(""))
            return false;
        if (productsTypeList == null)
            return true;
        else {
            for (int i = 0; i < productsTypeList.size(); i++) {
                if (productsTypeList.get(i).getTypeName().equalsIgnoreCase(name))
                    return false;
            }
            return true;
        }
    }

    public boolean validateBrand(String name) {
        if (name == null || name.trim().equals(""))
            return false;
        if (brandsList == null)
            return true;
        else {
            for (int i = 0; i < brandsList.size(); i++) {
                if (brandsList.get(i).getBrandName().equalsIgnoreCase(name))
                    return false;
            }
            return true;
        }
    }

    public boolean validateModel(String name) {
        if (name == null || name.trim().equals(""))
            return false;
        if (productsList == null)
            return true;
        else {
            for (int i = 0; i < productsList.size(); i++) {
                if (productsList.get(i).getProductName().equalsIgnoreCase(name))
                    return false;
            }
            return true;
        }
    }

    public boolean validateUoM(String name) {
        if (name == null || name.trim().equals(""))
            return false;
        if (uoMList == null)
            return true;
        else {
            for (int i = 0; i < uoMList.size(); i++) {
                if (uoMList.get(i).getDescription().equalsIgnoreCase(name))
                    return false;
            }
            return true;
        }
    }

    public void deleteModel() {
        PurchaseService service =
                ApiClient.getClient().create(PurchaseService.class);

        Call<Object> call = service.deleteProduct(apiKey, selectedProduct.getProductId());
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                pd.dismiss();
                if (response != null) {
                    if ((boolean) response.body()) {
                        Toast.makeText(getActivity(), "Model deleted successfully.", Toast.LENGTH_SHORT).show();
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

    public void deleteBrand() {
        PurchaseService service =
                ApiClient.getClient().create(PurchaseService.class);

        Call<Object> call = service.deleteBrand(apiKey, selectedBrand.getBrandId());
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                pd.dismiss();
                if (response != null) {
                    if ((boolean) response.body()) {
                        Toast.makeText(getActivity(), "Brand deleted successfully.", Toast.LENGTH_SHORT).show();
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

    public void deleteProductType() {
        PurchaseService service =
                ApiClient.getClient().create(PurchaseService.class);

        Call<Object> call = service.deleteProductType(apiKey, selectedProductType.getTypeId());
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                pd.dismiss();
                if (response != null) {
                    if ((boolean) response.body()) {
                        Toast.makeText(getActivity(), "type deleted successfully.", Toast.LENGTH_SHORT).show();
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

    private void getApiKey() {
        SharedPreferences shared = getActivity().getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        apiKey = shared.getString("api_key", "");

    }

    public void getAllUoM() {
        PurchaseService service =
                ApiClient.getClient().create(PurchaseService.class);

        Call<List<UoM>> call = service.getMeasurementUnits(apiKey);
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<List<UoM>>() {
            @Override
            public void onResponse(Call<List<UoM>> call, Response<List<UoM>> response) {
//                pd.dismiss();
                if (response != null) {
                    uoMList = (ArrayList<UoM>) response.body();

                    UoM tmp = new UoM();
                    tmp.description = "Select UoM";
                    uoMList.add(0, tmp);
                    setUoMDropdown(uoMList, uoMDropdown, false);

                }
                getAllProductTypes();
            }

            @Override
            public void onFailure(Call<List<UoM>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
//                pd.dismiss();
                getAllProductTypes();
            }
        });
    }

    public void getAllProductTypes() {
        SalesService service =
                ApiClient.getClient().create(SalesService.class);

        Call<List<ProductType>> call = service.getAllProductTypes(apiKey);
//        pd.show();
//        pd = ProgressDialog.show(getActivity(), null, "Processing");
//        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<List<ProductType>>() {
            @Override
            public void onResponse(Call<List<ProductType>> call, Response<List<ProductType>> response) {
                pd.dismiss();
                if (response != null) {
                    productsTypeList = (ArrayList<ProductType>) response.body();
                    ProductType tmp = new ProductType();

                    tmp.setTypeName("Select Product Type");
                    productsTypeList.add(0, tmp);
                    setProductTypesDropdown(productsTypeList, productTypesDropdown, false, false);

                }
            }

            @Override
            public void onFailure(Call<List<ProductType>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.dismiss();

            }
        });
    }

    public void setModelsDropdownLabel() {
        productsList = new ArrayList<>();
        JProduct tmp = new JProduct();

        tmp.setProductName("Select Model");
        productsList.add(0, tmp);
        setModelsDropdown(productsList, modelsDropdown, false);
    }

    public void getAllModelsForProductType() {
        SalesService service =
                ApiClient.getClient().create(SalesService.class);

        Call<List<Product>> call = service.getModels(apiKey, selectedProductType.getTypeId(), selectedBrand.getBrandId());
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                pd.dismiss();
                if (response != null) {
                    List<Product> productsListTmp = (ArrayList<Product>) response.body();

                    JProduct tmp = new JProduct();
                    productsList = new ArrayList<JProduct>();
                    tmp.setProductName("Select Model");
                    productsList.add(0, tmp);

                    for (int i = 0; i < productsListTmp.size(); i++) {
                        productsList.add(new JProduct(productsListTmp.get(i)));
                    }

                    if (!modelsMap.containsKey(selectedBrand))
                        modelsMap.put(selectedBrand, productsList);

                    setModelsDropdown(productsList, modelsDropdown, false);

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.dismiss();

            }
        });
    }

    public void setBrandsDropdownLabel() {
        brandsList = new ArrayList<>();
        Brand tmp = new Brand();
        tmp.setBrandName("Select Brand");

        brandsList.add(0, tmp);
        setBrandDropdown(brandsList, brandsDropdown, false, false);
    }

    public void getBrands() {
        SalesService service =
                ApiClient.getClient().create(SalesService.class);
        Call<List<Brand>> call;
//        if (selectedProductType.getTypeId() != null)
        call = service.getBrands(apiKey, selectedProductType.getTypeId());
//        else
//            call = service.getBrands(apiKey, selectedProductType.getTypeId());// get all brands here for a shop
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                pd.dismiss();
                if (response != null && response.body() != null) {
                    brandsList = (ArrayList<Brand>) response.body();
                    Brand tmp = new Brand();
                    tmp.setBrandName("Select Brand");

                    brandsList.add(0, tmp);
                    if (!brandsMap.containsKey(selectedProductType))
                        brandsMap.put(selectedProductType, brandsList);
                    setBrandDropdown(brandsList, brandsDropdown, false, false);

                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.dismiss();

            }
        });
    }

    public void setUoMDropdown(final List<UoM> list, Spinner dropdown, boolean isAddeorEdited) {
        final ArrayAdapter<UoM> spinnerArrayAdapter = new ArrayAdapter<UoM>(
                getActivity(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.collapsed_spinner_reports, parent, false);
//                View view = super.getDropDownView(position, convertView, parent);

//                TextView label = (TextView) view.findViewById(R.id.label);
                TextView value = (TextView) view.findViewById(R.id.value);

//                label.setText(text);
//                label.setMaxLines(1);
                value.setText(list.get(position).description);
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

                selectedUoM = (UoM) parent.getItemAtPosition(position);

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

        if (selectedUoM != null && isAddeorEdited) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getDescription().equalsIgnoreCase(selectedUoM.getDescription())) {
                    dropdown.setSelection(i);
                    break;
                }
            }
        }

    }

    public void setProductTypesDropdown(final List<ProductType> list, Spinner dropdown, final boolean isAddedOrEdited, final boolean isEdited) {
        final ArrayAdapter<ProductType> spinnerArrayAdapter = new ArrayAdapter<ProductType>(
                getActivity(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.collapsed_spinner_reports, parent, false);
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
                if (isAddedOrEdited && selectedProductType.getTypeId() == null && position != 0) {
                    if (!isEdited) {
                        brandsMap.put(selectedProductType, new ArrayList<Brand>());
                        setBrandsDropdownLabel();

                    }
                } else if (isAddedOrEdited && selectedProductType.getTypeId() != null && position != 0) {
                    if (!brandsMap.containsKey(selectedProductType))
                        getBrands();
                    else
                        setBrandDropdown(brandsMap.get(selectedProductType), brandsDropdown, false, false);
                } else if (!isAddedOrEdited && selectedProductType.getTypeId() != null && position != 0) {
                    if (!brandsMap.containsKey(selectedProductType))
                        getBrands();
                    else
                        setBrandDropdown(brandsMap.get(selectedProductType), brandsDropdown, false, false);
                } else if (!isAddedOrEdited && selectedProductType.getTypeId() == null && position != 0) {
                    if (!brandsMap.containsKey(selectedProductType))
                        getBrands();
                    else
                        setBrandDropdown(brandsMap.get(selectedProductType), brandsDropdown, false, false);
                }
                setModelsDropdownLabel();
                /*if(!isFirst && selectedProductType.getTypeId() == null){
                    if(brandsMap.containsKey(selectedProductType))
                        setBrandDropdown(brandsMap.get(selectedProductType), brandsDropdown, false);
                    else
                        setBrandsDropdownLabel();
                }else {
                    if (position != 0 && selectedProductType.getTypeId() != null) {
                        if(!brandsMap.containsKey(selectedProductType))
                            getBrands();
                        else
                            setBrandDropdown(brandsMap.get(selectedProductType), brandsDropdown, false);
                    }else {
                        setBrandsDropdownLabel();
                        setModelsDropdownLabel();
                    }
                }*/
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

        if (selectedProductType != null && isAddedOrEdited) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTypeName().equalsIgnoreCase(selectedProductType.getTypeName())) {
                    dropdown.setSelection(i);
                    break;
                }
            }
        }

    }

    public void setModelsDropdown(final List<JProduct> list, Spinner dropdown, final boolean isAddedOrEdited) {
        final ArrayAdapter<JProduct> spinnerArrayAdapter = new ArrayAdapter<JProduct>(
                getActivity(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.collapsed_spinner_reports, parent, false);
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

                selectedProduct = (JProduct) parent.getItemAtPosition(position);
                if (isAddedOrEdited)
                    modelsMap.put(selectedBrand, list);
                if (selectedProduct.getProductId() != null)
                    edtPrice.setText(selectedProduct.getUnitPrice().toString());

//                edtUoM.setText(selectedProduct.getUnitOfMsrmnt());
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

        if (selectedProduct != null && isAddedOrEdited) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getProductName().equalsIgnoreCase(selectedProduct.getProductName())) {
                    dropdown.setSelection(i);
                    break;
                }
            }
        }

    }

    public void setBrandDropdown(final List<Brand> list, Spinner dropdown, final boolean isAddedOrEdited, final boolean isEdited) {
        final ArrayAdapter<Brand> spinnerArrayAdapter = new ArrayAdapter<Brand>(
                getActivity(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.collapsed_spinner_reports, parent, false);
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
                if (isAddedOrEdited && selectedBrand.getBrandId() == null && position != 0) {
                    if (!isEdited) {
                        modelsMap.put(selectedBrand, new ArrayList<JProduct>());
                        setModelsDropdownLabel();
                    }
                } else if (isAddedOrEdited && selectedBrand.getBrandId() != null && position != 0) {
                    if (!modelsMap.containsKey(selectedBrand))
                        getAllModelsForProductType();
                    else
                        setModelsDropdown(modelsMap.get(selectedBrand), modelsDropdown, false);
                } else if (!isAddedOrEdited && selectedBrand.getBrandId() != null && position != 0) {
                    if (!modelsMap.containsKey(selectedBrand))
                        getAllModelsForProductType();
                    else
                        setModelsDropdown(modelsMap.get(selectedBrand), modelsDropdown, false);
                } else if (!isAddedOrEdited && selectedBrand.getBrandId() == null && position != 0) {
                    if (!modelsMap.containsKey(selectedBrand))
                        getAllModelsForProductType();
                    else
                        setModelsDropdown(modelsMap.get(selectedBrand), modelsDropdown, false);
                }

                /*
                 *//* if(isAddedOrEdited) {
                    brandsMap.put(selectedProductType, list);

                }*//*
                if(!isFirst &&  selectedBrand.getBrandId() == null){
                    if(modelsMap.containsKey(selectedBrand))
                        setModelsDropdown(modelsMap.get(selectedBrand),modelsDropdown,false);
                    else
                        setModelsDropdownLabel();
                }else {
                    if (position != 0 && selectedProductType.getTypeId() != null && selectedBrand.getBrandId() != null) {
                        if (!modelsMap.containsKey(selectedBrand))
                            getAllModelsForProductType();
                        else
                            setModelsDropdown(modelsMap.get(selectedBrand), modelsDropdown, false);
                    } else
                        setModelsDropdownLabel();
                }*/
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

        if (selectedBrand != null && isAddedOrEdited) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getBrandName().equalsIgnoreCase(selectedBrand.getBrandName())) {
                    dropdown.setSelection(i);
                    break;
                }
            }
        }

    }


    public boolean validate() {
        try {
            if (txtDate.getText().toString().trim().equals(""))
                return false;
            else if (edtInvoice.getText().toString().trim().equals(""))
                return false;
            else if (productTypesDropdown.getSelectedItemPosition() == 0)
                return false;
            else if (brandsDropdown.getSelectedItemPosition() == 0)
                return false;
            else if (modelsDropdown.getSelectedItemPosition() == 0)
                return false;
            else if (selectedUoM == null)
                return false;
            else if (edtQty.getText().toString().trim().equalsIgnoreCase(""))
                return false;
            else if (edtPrice.getText().toString().trim().equalsIgnoreCase(""))
                return false;
            else if (edtTotalPrice.getText().toString().trim().equalsIgnoreCase(""))
                return false;
            else if (edtSpecs.getText().toString().trim().equalsIgnoreCase(""))
                return false;
            else if (edtSupplier.getText().toString().trim().equalsIgnoreCase(""))
                return false;
            else
                return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void showDatePickerDialog(final TextView dateInputField) {
        final Calendar current = Calendar.getInstance();
        final Calendar dpdCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDailog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dpdCalendar.set(Calendar.YEAR, year);
                dpdCalendar.set(Calendar.MONTH, monthOfYear);
                dpdCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//                if (dpdCalendar.compareTo(current) > -1) {
                final String myFormat = HALF_MONTH_DATE_FORMAT;
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                pickerDateSring = sdf.format(dpdCalendar.getTime());
                dateInputField.setText(pickerDateSring);

//                    fragment.setDateFrom(dateFrom);

//                }


            }
        }, current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));
        long minDate;

//        minDate = incrementDate(new SimpleDateFormat(HALF_MONTH_DATE_FORMAT).format(current.getTime()));
//        datePickerDailog.getDatePicker().setMinDate(minDate);
        showSelectedDateOnPicker(datePickerDailog, pickerDateSring);
        datePickerDailog.show();
    }

    public void showSelectedDateOnPicker(DatePickerDialog dpd, String date) {
        try {
            if (null != date && !date.equalsIgnoreCase("")) {
                int dateDigits[] = {-1, -1, -1};
                dateDigits = getDateInDigits(date);
                for (int i : dateDigits) {
                    if (i < 0)
                        return;
                }
                dpd.updateDate(dateDigits[0], dateDigits[1], dateDigits[2]);
                dpd.setTitle(null);
            }
        } catch (Exception e) {
        }

    }

    public int[] getDateInDigits(String dateString) {
        int dateDigits[] = {-1, -1, -1};
        try {
            Date date = new SimpleDateFormat(HALF_MONTH_DATE_FORMAT).parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            dateDigits[0] = cal.get(Calendar.YEAR);
            dateDigits[1] = cal.get(Calendar.MONTH);
            dateDigits[2] = cal.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
        }

        return dateDigits;
    }

    private long incrementDate(String date) {
        try {
            Date parsedDate = new SimpleDateFormat(HALF_MONTH_DATE_FORMAT, Locale.getDefault()).parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            calendar.add(Calendar.DAY_OF_MONTH, 0);

            Date finalDate = calendar.getTime();
            return finalDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
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
        void onCheckoutListenerPurchase();
    }
}