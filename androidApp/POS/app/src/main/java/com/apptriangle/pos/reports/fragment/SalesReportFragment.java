package com.apptriangle.pos.reports.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.apptriangle.pos.R;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.model.CSale;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.model.ProductType;
import com.apptriangle.pos.model.Sale;
import com.apptriangle.pos.model.User;
import com.apptriangle.pos.reports.adaptor.ReportsAdaptor;
import com.apptriangle.pos.reports.adaptor.TableViewAdapter;
import com.apptriangle.pos.reports.model.Cell;
import com.apptriangle.pos.reports.model.ColumnHeader;
import com.apptriangle.pos.reports.model.RowHeader;
import com.apptriangle.pos.reports.service.ReportService;
import com.apptriangle.pos.sales.response.SalesResponse;
import com.apptriangle.pos.sales.restInterface.SalesService;
import com.apptriangle.pos.tableview.TableView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zeeshan on 5/3/2018.
 */
public class SalesReportFragment extends Fragment {
    public static String HALF_MONTH_DATE_FORMAT = "MMM dd, yyyy";
    public static String SERVER_DATE_FORMAT = "MM/dd/yyyy";
    String pickerDateSring;
    private CardView customContainer;
    private LinearLayout dateFromContainer, dateToContainer;
    private Button btnGo;
    private TextView txtDateFrom, txtDateTo;
    private Spinner productsDropdown, brandsDropdown, usersDropdown;
    private RadioGroup radioGroup;
    private List<RowHeader> mRowHeaderList;
    private List<ColumnHeader> mColumnHeaderList;
    private List<List<Cell>> mCellList;
    private ProductType selectedProductType;
    private Brand selectedBrand;
    private OnFragmentInteractionListener mListener;
    public static final int COLUMN_SIZE = 10;
    public static int ROW_SIZE = 1;
    // Columns indexes
    public static final int MOOD_COLUMN_INDEX = 3;
    public static final int GENDER_COLUMN_INDEX = 4;

    // Constant values for icons
    public static final int SAD = 0;
    public static final int HAPPY = 1;
    public static final int BOY = 0;
    public static final int GIRL = 1;
    private View contentView;
    String[] listItems = {"item 1", "item 2 ", "list", "android" };
    private Button checkoutBtn;
    TableView tableView;
    private ProgressDialog pd;
    RecyclerView recyclerView;
    private String apiKey;
    private User selectedUser;
    ReportsAdaptor adapter;
    ArrayList<ProductType> productsList;
    ArrayList<Brand> brandsList;
    ArrayList<User> usersList;
    ArrayList<Sale> responseList;
    HashMap<Integer,CSale> salesMap = new HashMap();

    public SalesReportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        setTitle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_reports, container, false);
        return contentView;
    }
    private void getApiKey() {
        SharedPreferences shared = getActivity().getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        apiKey = shared.getString("api_key", "");

    }
    public void initialize(){
        getApiKey();
      /*  pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");*/
        /*pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);*/
        tableView = (TableView)contentView.findViewById(R.id.content_container);
        customContainer = (CardView)contentView.findViewById(R.id.customContainer);
        productsDropdown = (Spinner) contentView.findViewById(R.id.productsDropdown);
        brandsDropdown = (Spinner) contentView.findViewById(R.id.brandsDropdown);
        usersDropdown = (Spinner) contentView.findViewById(R.id.usersDropdown);
        dateFromContainer = (LinearLayout)contentView.findViewById(R.id.dateFromContainer);
        dateToContainer = (LinearLayout)contentView.findViewById(R.id.dateToContainer);
        btnGo =(Button)contentView.findViewById(R.id.btnGo);
        txtDateFrom = (TextView)contentView.findViewById(R.id.txtDateFrom);
        txtDateTo = (TextView)contentView.findViewById(R.id.txtDateTo);

        setupDropdowns();

        dateFromContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(txtDateFrom);
            }
        });
        dateToContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(txtDateTo);
            }
        });
        btnGo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tableView.setVisibility(View.VISIBLE);
                getReportData();

            }
        });

        radioGroup =(RadioGroup)contentView.findViewById(R.id.radioGroup1);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.radio0:
                        customContainer.setVisibility(View.GONE);
                        tableView.setVisibility(View.VISIBLE);
                        displayReport();
                        break;
                    case R.id.radio1:
                        customContainer.setVisibility(View.GONE);
                        tableView.setVisibility(View.VISIBLE);
                        displayReport();
                        break;
                    case R.id.radio2:
                        customContainer.setVisibility(View.VISIBLE);
                        tableView.setVisibility(View.GONE);
                        break;
                }
            }
        });
        List<SalesResponse> list = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            SalesResponse tmp = new SalesResponse();
            list.add(tmp);
        }



       /* recyclerView = (RecyclerView)contentView.findViewById(R.id.rcView);
        adapter = new ReportsAdaptor(getActivity(), list);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setNestedScrollingEnabled(false);
//        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);*/
    }


    public void getReportData(){
        ReportService service =
                ApiClient.getClient().create(ReportService.class);

        Call<List<Sale>> call = service.getSalesReportData(apiKey, convertDate(txtDateFrom.getText().toString()),
                convertDate(txtDateTo.getText().toString()), ((selectedProductType != null) ? selectedProductType.getTypeId() : -1),
                ((selectedBrand != null) ? selectedBrand.getBrandId() : -1), ((selectedUser != null) ? selectedUser.user_id : ""));
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<List<Sale>>() {
            @Override
            public void onResponse(Call<List<Sale>> call, Response<List<Sale>> response) {
                pd.dismiss();
                if (response != null && response.body() != null) {
                    ROW_SIZE = response.body().size();
                    responseList = (ArrayList<Sale>) response.body();
                    if(responseList != null) {
                       /* for (int i = 0; i < responseList.size(); i++) {
                            if(salesMap.containsKey(responseList.get(i).sale_id)){
                                salesMap.get(responseList.get(i).sale_id).cust_name = responseList.get(i).cust_name;
                                responseList.get(i).cproduct.setQty(responseList.get(i).prod_qty);
                                salesMap.get(responseList.get(i).sale_id).cproduct.add(responseList.get(i).cproduct);
                            }else{
                                responseList.get(i).cproduct.setQty(responseList.get(i).prod_qty);
                                salesMap.put(responseList.get(i).sale_id,new CSale(responseList.get(i)));

                            }
                        }*/
                        displayReport();
                    }


                }
            }

            @Override
            public void onFailure(Call<List<Sale>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.dismiss();

            }
        });
    }

    public String  convertDate(String dateString){
        SimpleDateFormat df = new SimpleDateFormat(HALF_MONTH_DATE_FORMAT);
        Date startDate;
        try {
            startDate = df.parse(dateString);
            SimpleDateFormat df2 = new SimpleDateFormat(SERVER_DATE_FORMAT);
            String newDateString = df2.format(startDate);
            return newDateString;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void getUsers(){
        ReportService service =
                ApiClient.getClient().create(ReportService.class);

        Call<List<User>> call = service.getUsers(apiKey);
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                pd.dismiss();
                if (response != null) {
                    usersList = (ArrayList<User>) response.body();
                    User tmp = new User();

                    tmp.user_id = "Select User";
                    usersList.add(0,tmp);
                    setUsersDropdown(usersList, usersDropdown);

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.dismiss();

            }
        });
    }

    public void getAllProductTypes(){
        SalesService service =
                ApiClient.getClient().create(SalesService.class);

        Call<List<ProductType>> call = service.getAllProductTypes(apiKey);
//        pd.show();
        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<List<ProductType>>() {
            @Override
            public void onResponse(Call<List<ProductType>> call, Response<List<ProductType>> response) {
                pd.dismiss();
                if (response != null) {
                    productsList = (ArrayList<ProductType>) response.body();
                    ProductType tmp = new ProductType();

                    tmp.setTypeName("Select Product Type");
                    productsList.add(0,tmp);
                    setProductTypesDropdown(productsList, productsDropdown);

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

    public void getBrands(){
        SalesService service =
                ApiClient.getClient().create(SalesService.class);

        Call<List<Brand>> call = service.getBrands(apiKey, selectedProductType.getTypeId());
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

                    brandsList.add(0,tmp);
                    setBrandDropdown(brandsList, brandsDropdown);

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

    public void setupDropdowns(){
        setBrandsDropdownLabel();
        getAllProductTypes();
        getUsers();

    }

    public void displayReport(){
        // Create our custom TableView Adapter
        TableViewAdapter adapter = new TableViewAdapter(getActivity());

        // Set this adapter to the our TableView
        tableView.setAdapter(adapter);
        initData();
        loadData();
        // Let's set datas of the TableView on the Adapter
        adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
    }

    private void initData() {
        mRowHeaderList = new ArrayList<>();
        mColumnHeaderList = new ArrayList<>();
        mCellList = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            mCellList.add(new ArrayList<Cell>());
        }
    }

    private void loadData() {
        List<RowHeader> rowHeaders = getRowHeaderList();
        List<List<Cell>> cellList = getCellListForSortingTest(); // getCellList();
        List<ColumnHeader> columnHeaders = getColumnHeaderList(); //getRandomColumnHeaderList(); //

        mRowHeaderList.addAll(rowHeaders);
        for (int i = 0; i < cellList.size(); i++) {
            mCellList.get(i).addAll(cellList.get(i));
        }

        // Load all data
        mColumnHeaderList.addAll(columnHeaders);


        // Example: Set row header width manually
        // DisplayMetrics metrics = getResources().getDisplayMetrics();
        // int rowHeaderWidth = Math.round(65 * (metrics.densityDpi / 160f));
        // mTableView.setRowHeaderWidth(rowHeaderWidth);

    }

    private List<RowHeader> getRowHeaderList() {
        List<RowHeader> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            String rh = Integer.toString(i+1) ;

            RowHeader header = new RowHeader(String.valueOf(i), rh);
            list.add(header);
        }

        return list;
    }


    private List<ColumnHeader> getColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();
        String title;
        for (int i = 0; i < COLUMN_SIZE; i++) {
            if(i == 0)
                title = "Date";
            else if(i == 1)
                title = "Invoice No";
            else if(i == 2)
                title = "Customer";
            else if(i == 3)
                title = "Product";
            else if(i == 4)
                title = "Brand";
            else if(i == 5)
                title = "UoM";
            else if(i == 6)
                title = "Unit/S-Price";
            else if(i == 7)
                title = "Unit/Quantity";
            else if(i == 8)
                title = "Total Price";
            else
                title = "Sold By";

            ColumnHeader header = new ColumnHeader(String.valueOf(i), title);
            list.add(header);
        }

        return list;
    }


    private List<List<Cell>> getCellListForSortingTest() {
        List<List<Cell>> list = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < COLUMN_SIZE; j++) {
                Object text = "cell " + j + " " + i;

                final int random = new Random().nextInt();
                if (j == 0) {
                    text = responseList.get(i).saleDate.toString();
                } else if (j == 1) {
                    text = Integer.toString(responseList.get(i).sale_id);
                } else if (j == 2) {
                    text = responseList.get(i).cust_name;
                } else if (j == 3) {
                    text = responseList.get(i).cproduct.getProductName();
                } else if (j == 4) {
                    text = responseList.get(i).cproduct.getBrand().getBrandName();
                } else if (j == 5) {
                    text = responseList.get(i).cproduct.getUnitOfMsrmnt().description;
                } else if (j == 6){
                    text = Double.toString(responseList.get(i).cproduct.getUnitPrice());
                } else if (j == 7){
                    text = Integer.toString(responseList.get(i).prod_qty);
                } else if (j == 8){
                    text = Double.toString(responseList.get(i).cproduct.getUnitPrice() * responseList.get(i).prod_qty);
                } else {
                    text = responseList.get(i).agent.user_id;
                }


                // Create dummy id.
                String id = j + "-" + i;

                Cell cell;
                cell = new Cell(id, text);

                cellList.add(cell);
            }
            list.add(cellList);
        }

        return list;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onCheckoutButtonPressed() {
        if (mListener != null) {
//            mListener.onCheckoutListener();
        }
    }

    void setTitle()
    {
        ((Activity) getActivity()).setTitle("REPORT");
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
                if(position != 0) {
                    selectedProductType = (ProductType) parent.getItemAtPosition(position);

                    getBrands();
                }else{
                    selectedProductType = null;
                    setBrandsDropdownLabel();
                }
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

    }

    public void setBrandsDropdownLabel() {
        brandsList = new ArrayList<>();
        Brand tmp = new Brand();
        tmp.setBrandName("Select Brand");

        brandsList.add(0, tmp);
        setBrandDropdown(brandsList, brandsDropdown);
    }

    public void setBrandDropdown(final List<Brand> list, Spinner dropdown) {
        final ArrayAdapter<Brand> spinnerArrayAdapter = new ArrayAdapter<Brand>(
                getActivity(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return true;
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

                if(position != 0)
                    selectedBrand = (Brand) parent.getItemAtPosition(position);
                else
                    selectedBrand = null;
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
    }

    public void setUsersDropdown(final List<User> list, Spinner dropdown) {
        final ArrayAdapter<User> spinnerArrayAdapter = new ArrayAdapter<User>(
                getActivity(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                return true;
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
                value.setText(list.get(position).user_id);
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

                if(position != 0)
                    selectedUser = (User) parent.getItemAtPosition(position);
                else
                    selectedUser = null;
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
//        void onCheckoutListener();
    }
}