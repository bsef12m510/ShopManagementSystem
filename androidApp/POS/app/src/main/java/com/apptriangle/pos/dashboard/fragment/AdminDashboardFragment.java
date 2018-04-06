package com.apptriangle.pos.dashboard.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptriangle.pos.R;
import com.apptriangle.pos.dashboard.adapter.SalesAdminAdapter;
import com.apptriangle.pos.sales.response.SalesResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeeshan on 3/28/2018.
 */
public class AdminDashboardFragment extends Fragment {
    private PieChart mChart;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private View contentView;
    MyPagerAdapter adaptor;
    TabLayout tabLayout;
    ViewPager pager;
    private SalesAdminAdapter adapter;
    String[] listItems = {"item 1", "item 2 ", "list", "android"};


    public AdminDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pager = (ViewPager) contentView.findViewById(R.id.viewPager);
        adaptor = new MyPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager());
        pager.setAdapter(adaptor);
        tabLayout = (TabLayout) contentView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        initialize();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        return contentView;
    }


    public void initialize() {
        List<SalesResponse> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SalesResponse tmp = new SalesResponse();
            list.add(tmp);
        }
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        adapter = new SalesAdminAdapter(getActivity(), list);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setNestedScrollingEnabled(false);
//        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
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
        void onFragmentInteraction();
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
