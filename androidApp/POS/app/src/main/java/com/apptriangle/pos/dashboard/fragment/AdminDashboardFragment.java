package com.apptriangle.pos.dashboard.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apptriangle.pos.InvoiceSearchFragment.adapter.InvoiceAdapter;
import com.apptriangle.pos.InvoiceSearchFragment.fragment.InvoiceSearchFragment;
import com.apptriangle.pos.InvoiceSearchFragment.service.InvoiceService;
import com.apptriangle.pos.R;
import com.apptriangle.pos.api.ApiClient;
import com.apptriangle.pos.dashboard.adapter.SalesAdminAdapter;
import com.apptriangle.pos.dashboard.service.DashboardService;
import com.apptriangle.pos.model.Invoice;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.response.SalesResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zeeshan on 3/28/2018.
 */
public class AdminDashboardFragment extends Fragment {
    private String apiKey;
    private ProgressDialog pd;
    private PieChart mChart;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private View contentView;
    MyPagerAdapter adaptor;
    TabLayout tabLayout;
    ViewPager pager;
    private SalesAdminAdapter adapter;
    String[] listItems = {"item 1", "item 2 ", "list", "android"};
    public ArrayList<Product> topSellingProductsList = new ArrayList<>();
    public ArrayList<Product> lowStockProductsList = new ArrayList<>();
    public ArrayList<Product> inventoryList = new ArrayList<>();
    ArrayList<Product> dataList = new ArrayList<>();
    TextView tvLowStockCount, tvInventoryCount;
    LinearLayout lytLowStock, lytInventory;
    public boolean isSale;

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    private void getApiKey() {
        SharedPreferences shared = getActivity().getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        apiKey = shared.getString("api_key", "");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getApiKey();
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Processing...");
        pd.setCanceledOnTouchOutside(false);
        pager = (ViewPager) contentView.findViewById(R.id.viewPager);
        lytLowStock = (LinearLayout) contentView.findViewById(R.id.lytLowStock);
        lytInventory = (LinearLayout) contentView.findViewById(R.id.lytInventory);
        tvLowStockCount = (TextView) contentView.findViewById(R.id.lowStockCount);
        tvInventoryCount = (TextView) contentView.findViewById(R.id.inventoryCount);
        adaptor = new MyPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());
        pager.setAdapter(adaptor);
        tabLayout = (TabLayout) contentView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        lytLowStock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(lowStockProductsList != null && lowStockProductsList.size() > 0) {
                    dataList = lowStockProductsList;
                    isSale = false;
                    onButtonPressed();
                }
            }
        });

        lytInventory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(inventoryList != null && inventoryList.size() > 0) {
                    dataList = inventoryList;
                    isSale = false;
                    onButtonPressed();
                }
            }
        });

        initialize();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        return contentView;
    }

    public void getTopSellingProducts() {
        DashboardService service =
                ApiClient.getClient().create(DashboardService.class);
        Call<List<Product>> call;
        call = service.getTopSellingProducts(apiKey);
        pd.show();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                pd.hide();
                if (response != null && response.body() != null) {
                    topSellingProductsList.clear();
                    topSellingProductsList = (ArrayList<Product>) response.body();
//                    invoiceList.add(invObj);
                    if (topSellingProductsList != null) {
                        if (topSellingProductsList.size() <= 5) {
                            adapter = new SalesAdminAdapter(getActivity(), topSellingProductsList);
                            adapter.parentFrag = AdminDashboardFragment.this;
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setNestedScrollingEnabled(false);
                            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(adapter);

                        } else if (topSellingProductsList.size() > 5) {
                            dataList = new ArrayList<>();
                            dataList.addAll(topSellingProductsList);
                            topSellingProductsList.subList(5, topSellingProductsList.size()).clear();
                            Product tmp = new Product();
                            tmp.setProductName("More...");
                            topSellingProductsList.add(tmp);
                            adapter = new SalesAdminAdapter(getActivity(), topSellingProductsList);
                            adapter.parentFrag = AdminDashboardFragment.this;
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setNestedScrollingEnabled(false);
                            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }
                getInventory();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
//                pd.hide();
                getInventory();
            }
        });
    }

    public void getInventory() {
        DashboardService service =
                ApiClient.getClient().create(DashboardService.class);
        Call<List<Product>> call;
        call = service.getInventory(apiKey);
//        pd.show();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                pd.hide();
                if (response != null && response.body() != null) {
                    inventoryList.clear();
                    inventoryList = (ArrayList<Product>) response.body();
                    if (inventoryList != null) {
                        tvInventoryCount.setText(Integer.toString(inventoryList.size()));
                    }
                }
                getLowStockProducts();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Log error here since request failed
                Log.e("failure", "failure");
//                pd.hide();
                getLowStockProducts();

            }
        });
    }

    public void getLowStockProducts() {
        DashboardService service =
                ApiClient.getClient().create(DashboardService.class);
        Call<List<Product>> call;
        call = service.getLowStockProducts(apiKey);
//        pd.show();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                pd.hide();
                if (response != null && response.body() != null) {
                    lowStockProductsList.clear();
                    lowStockProductsList = (ArrayList<Product>) response.body();
                    if (lowStockProductsList != null) {
                        tvLowStockCount.setText(Integer.toString(lowStockProductsList.size()));
                    }
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




    public void initialize() {
        List<SalesResponse> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SalesResponse tmp = new SalesResponse();
            list.add(tmp);
        }
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        getTopSellingProducts();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction(dataList, isSale);
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

    public void openMoreTopSellingProducts() {
        isSale = true;
        onButtonPressed();
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
        void onFragmentInteraction(ArrayList<Product> dataList , boolean isSale);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adaptor != null)
            adaptor.notifyDataSetChanged();
        if (tabLayout != null && pager != null)
            tabLayout.setupWithViewPager(pager);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final List<String> mFragmentTitleList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentTitleList.add("Pie");
            mFragmentTitleList.add("Bar");
        }

        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return PieChartFragment.newInstance("FirstFragment, Instance 1");
                case 1:
                    return BarChartFragment.newInstance("SecondFragment, Instance 1");

                default:
                    return PieChartFragment.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
