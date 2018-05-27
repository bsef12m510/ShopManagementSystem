package com.apptriangle.pos.dashboard.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.apptriangle.pos.MainDrawerActivity;
import com.apptriangle.pos.PublicActivity;
import com.apptriangle.pos.R;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.dashboard.response.MonthlySalesResponse;
import com.apptriangle.pos.dashboard.response.TodaySaleResponse;
import com.apptriangle.pos.dashboard.service.DashboardService;
import com.hbb20.GThumb;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DashboardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View contentView;
    String[] listItems = {"item 1", "item 2 ", "list", "android"};
    LinearLayout salesBtn, stockBtn, invoiceBtn, purchaseBtn;
    private ProgressDialog pd;
    private String apiKey, username;
    List<TodaySaleResponse> salesList = new ArrayList<>();
    private TextView tvName, tvItemCount, tvItemValue;
    private int totalItems;
    private double totalSaleAmt;
    private GThumb gThumb;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setOnBackListener();
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getApiKey();
        setTitle();
        gThumb = (GThumb) contentView.findViewById(R.id.gthumb);
        String[] arr = username.split(" ");
        if (arr.length > 1)
            gThumb.loadThumbForName(null, arr[0], arr[1]);
        else
            gThumb.loadThumbForName(null, username);
        tvName = (TextView) contentView.findViewById(R.id.name);
        tvItemCount = (TextView) contentView.findViewById(R.id.itemCount);
        tvItemValue = (TextView) contentView.findViewById(R.id.itemValue);
        salesBtn = (LinearLayout) contentView.findViewById(R.id.button1);
        stockBtn = (LinearLayout) contentView.findViewById(R.id.button3);
        invoiceBtn = (LinearLayout) contentView.findViewById(R.id.button4);
        purchaseBtn = (LinearLayout) contentView.findViewById(R.id.button2);
        salesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSalesClick();
            }
        });

        stockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStockClick();
            }
        });

        invoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInvoiceClick();
            }
        });

        purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPurchaseClick();
            }
        });
        tvName.setText("Hello, " + username + " !");
        getTodaySale();
    }

    private void getApiKey() {
        SharedPreferences shared = getActivity().getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        apiKey = shared.getString("api_key", "");
        username = shared.getString("username", "");

    }

    public void getTodaySale() {
        DashboardService service =
                ApiClient.getClient().create(DashboardService.class);
        Call<List<TodaySaleResponse>> call;
        call = service.getTodaySale(apiKey);

        pd = ProgressDialog.show(getActivity(), null, "Processing");
        pd.setCanceledOnTouchOutside(false);
        call.enqueue(new Callback<List<TodaySaleResponse>>() {
            @Override
            public void onResponse(Call<List<TodaySaleResponse>> call, Response<List<TodaySaleResponse>> response) {
                pd.dismiss();
                if (response != null && response.body() != null) {
                    if (salesList != null)
                        salesList.clear();
                    salesList = (ArrayList<TodaySaleResponse>) response.body();
                    if (salesList != null) {
                        totalItems = 0;
                        totalSaleAmt = 0;
                        for (int i = 0; i < salesList.size(); i++) {
                            totalItems = totalItems + salesList.get(i).total_items;
                            totalSaleAmt = totalSaleAmt + salesList.get(i).total_sale;
                        }
                        tvItemCount.setText(Integer.toString(totalItems));
                        tvItemValue.setText(Double.toString(totalSaleAmt));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TodaySaleResponse>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
                pd.dismiss();

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onSalesClick() {
        if (mListener != null) {
            mListener.onSalesClickListener();
        }
    }

    public void onStockClick() {
        if (mListener != null) {
            mListener.onStockClickListener();
        }
    }

    public void onInvoiceClick() {
        if (mListener != null) {
            mListener.onInvoiceClickListener();
        }
    }

    public void onPurchaseClick() {
        if (mListener != null) {
            mListener.onPurchaseClickListener();
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

    void setTitle() {
        ((Activity) getActivity()).setTitle(getResources().getString(R.string.app_name));
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void setOnBackListener() {
        contentView.setFocusableInTouchMode(true);
        contentView.requestFocus();
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK)
                    confirmAndLogout(getActivity());
                return false;
            }
        });
    }


    public void confirmAndLogout(final Activity activity) {
        if (!activity.isFinishing()) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle("Are you sure you want to logout?")
                    // set dialog message
                    .setCancelable(false).setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    logout();

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
//		alertDialog.setOutsideTouchable(true);

            alertDialog.show();
        }

    }

    void logout() {
        deleteApiKey();
        Intent intent = new Intent(getActivity(), PublicActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    public void deleteApiKey() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.appTriangle.pos", Context.MODE_PRIVATE);

        prefs.edit().putString("api_key", "").apply();
        prefs.edit().putBoolean("is_remember_me", false).apply();
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
        void onSalesClickListener();

        void onStockClickListener();

        void onInvoiceClickListener();

        void onPurchaseClickListener();
    }
}
