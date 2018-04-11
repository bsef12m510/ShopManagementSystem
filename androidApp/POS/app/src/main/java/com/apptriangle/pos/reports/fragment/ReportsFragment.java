package com.apptriangle.pos.reports.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.reports.adaptor.ReportsAdaptor;
import com.apptriangle.pos.reports.adaptor.TableViewAdapter;
import com.apptriangle.pos.reports.model.Cell;
import com.apptriangle.pos.reports.model.ColumnHeader;
import com.apptriangle.pos.reports.model.RowHeader;
import com.apptriangle.pos.sales.response.SalesResponse;
import com.apptriangle.pos.tableview.TableView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by zeeshan on 4/5/2018.
 */
public class ReportsFragment  extends Fragment {
    public static String HALF_MONTH_DATE_FORMAT = "MMM dd, yyyy";
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
    private OnFragmentInteractionListener mListener;
    public static final int COLUMN_SIZE = 100;
    public static final int ROW_SIZE = 100;
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
    RecyclerView recyclerView;
    ReportsAdaptor adapter;
    public ReportsFragment() {
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

    public void initialize(){
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
                displayReport();
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


    public void setupDropdowns(){
        List<Product> products = new ArrayList<>();
        Product placeholder = new Product();
        placeholder.setProductName("Product");
        products.add(placeholder);
        for (int i = 0; i < 10; i++) {
            Product tmp = new Product();
            tmp.setProductName("Product");
            products.add(tmp);
        }
        setDropdown(products, productsDropdown);

        List<Brand> brands = new ArrayList<>();
        Brand brandPlaceholder = new Brand();
        brandPlaceholder.setBrandName("Brand");
        brands.add(brandPlaceholder);
        for (int i = 0; i < 10; i++) {
            Brand tmp = new Brand();
            tmp.setBrandName("Brand");
            brands.add(tmp);
        }
        setBrandDropdown(brands, brandsDropdown);

        List<Brand> users = new ArrayList<>();
        Brand user = new Brand();
        user.setBrandName("User");
        users.add(user);
        for (int i = 0; i < 10; i++) {
            Brand tmp = new Brand();
            tmp.setBrandName("User");
            users.add(tmp);
        }
        setBrandDropdown(users, usersDropdown);
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
            String rh = "row " + i ;

            RowHeader header = new RowHeader(String.valueOf(i), rh);
            list.add(header);
        }

        return list;
    }


    private List<ColumnHeader> getColumnHeaderList() {
        List<ColumnHeader> list = new ArrayList<>();

        for (int i = 0; i < COLUMN_SIZE; i++) {
            String title = "column " + i;
            if (i % 6 == 2) {
                title = "large column " + i;
            } else if (i == MOOD_COLUMN_INDEX) {
                title = "mood";
            } else if (i == GENDER_COLUMN_INDEX) {
                title = "gender";
            }
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
                    text = i;
                } else if (j == 1) {
                    text = random;
                } else if (j == MOOD_COLUMN_INDEX) {
                    text = random % 2 == 0 ? HAPPY : SAD;
                } else if (j == GENDER_COLUMN_INDEX) {
                    text = random % 2 == 0 ? BOY : GIRL;
                }

                // Create dummy id.
                String id = j + "-" + i;

                Cell cell;
                if (j == 3) {
                    cell = new Cell(id, text, random % 2 == 0 ? "happy" : "sad");
                } else if (j == 4) {
                    // NOTE female and male keywords for filter will have conflict since "female"
                    // contains "male"
                    cell = new Cell(id, text, random % 2 == 0 ? "boy" : "girl");
                } else {
                    cell = new Cell(id, text);
                }
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

                if (dpdCalendar.compareTo(current) > -1) {
                    final String myFormat = HALF_MONTH_DATE_FORMAT;
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    pickerDateSring = sdf.format(dpdCalendar.getTime());
                    dateInputField.setText(pickerDateSring);

//                    fragment.setDateFrom(dateFrom);

                }


            }
        }, current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH));
        long minDate;

        minDate = incrementDate(new SimpleDateFormat(HALF_MONTH_DATE_FORMAT).format(current.getTime()));
        datePickerDailog.getDatePicker().setMinDate(minDate);
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


    public void setDropdown(final List<Product> list, Spinner dropdown) {
        final ArrayAdapter<Product> spinnerArrayAdapter = new ArrayAdapter<Product>(
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

                Product selectedItemText = (Product) parent.getItemAtPosition(position);
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

                Brand selectedItemText = (Brand) parent.getItemAtPosition(position);
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