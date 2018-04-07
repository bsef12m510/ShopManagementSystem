package com.apptriangle.pos.purchase.fragemnt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
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
import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.model.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zeeshan on 4/4/2018.
 */
public class PurchaseFragment extends Fragment {
    public static String HALF_MONTH_DATE_FORMAT = "MMM dd, yyyy";
    String m_Text = "Product";
    private OnFragmentInteractionListener mListener;
    private View contentView;
    String[] listItems = {"item 1", "item 2 ", "list", "android"};
    private Button checkoutBtn;
    private ImageButton addProductBtn, addBrandBtn, editProductBtn, editBrandBtn;
    private Spinner productsDropdown, brandsDropdown;
    private String pickerDateSring;
    private String newProductString, editProductSting, newBrandString, editBrandString;
    private TextView txtDate;
    private LinearLayout dateContainer;
    private EditText seller;
    private ScrollView scrollView;

    public PurchaseFragment() {
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
        contentView = inflater.inflate(R.layout.fragment_purchase, container, false);
        return contentView;
    }

    public void initialize(View view) {
        checkoutBtn = (Button) view.findViewById(R.id.checkoutBtn);
        addProductBtn = (ImageButton) view.findViewById(R.id.addProductBtn);
        addBrandBtn = (ImageButton) view.findViewById(R.id.addBrandBtn);
        editProductBtn = (ImageButton) view.findViewById(R.id.editProductBtn);
        editBrandBtn = (ImageButton) view.findViewById(R.id.editBrandBtn);
        productsDropdown = (Spinner) view.findViewById(R.id.productsDropdown);
        brandsDropdown = (Spinner) view.findViewById(R.id.brandsDropdown);
        dateContainer = (LinearLayout) view.findViewById(R.id.dateContainer);
        txtDate = (TextView) view.findViewById(R.id.txtDate);

        seller = (EditText) view.findViewById(R.id.seller);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        seller.setSingleLine(false);
        seller.setHorizontallyScrolling(false);
        seller.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        List<Product> products = new ArrayList<>();
        Product placeholder = new Product();
        placeholder.title = "Select Product";
        products.add(placeholder);
        for (int i = 0; i < 10; i++) {
            Product tmp = new Product();
            tmp.title = "Product";
            products.add(tmp);
        }
        setDropdown(products, productsDropdown);

        List<Brand> brands = new ArrayList<>();
        Brand brandPlaceholder = new Brand();
        brandPlaceholder.title = "Select Brand";
        brands.add(brandPlaceholder);
        for (int i = 0; i < 10; i++) {
            Brand tmp = new Brand();
            tmp.title = "Brand";
            brands.add(tmp);
        }
        setBrandDropdown(brands, brandsDropdown);

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

        dateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(txtDate);
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckoutButtonPressed();
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

// Set up the input
        final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton((identifier == 1 || identifier == 3) ? "Add" : "Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (1 == identifier)
                    newProductString = input.getText().toString();
                else if (2 == identifier)
                    editProductSting = input.getText().toString();
                else if (3 == identifier)
                    newBrandString = input.getText().toString();
                else if (4 == identifier)
                    editBrandString = input.getText().toString();
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
                View view = inflater.inflate(R.layout.collapsed_spinner, parent, false);
//                View view = super.getDropDownView(position, convertView, parent);

//                TextView label = (TextView) view.findViewById(R.id.label);
                TextView value = (TextView) view.findViewById(R.id.value);

//                label.setText(text);
//                label.setMaxLines(1);
                value.setText(list.get(position).title);
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
                View view = inflater.inflate(R.layout.collapsed_spinner, parent, false);
//                View view = super.getDropDownView(position, convertView, parent);

//                TextView label = (TextView) view.findViewById(R.id.label);
                TextView value = (TextView) view.findViewById(R.id.value);

//                label.setText(text);
//                label.setMaxLines(1);
                value.setText(list.get(position).title);
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