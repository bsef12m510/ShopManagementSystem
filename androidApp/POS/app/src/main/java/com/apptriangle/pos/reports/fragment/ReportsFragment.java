package com.apptriangle.pos.reports.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apptriangle.pos.R;
import com.apptriangle.pos.reports.adaptor.ReportsAdaptor;
import com.apptriangle.pos.reports.adaptor.TableViewAdapter;
import com.apptriangle.pos.reports.model.Cell;
import com.apptriangle.pos.reports.model.ColumnHeader;
import com.apptriangle.pos.reports.model.RowHeader;
import com.apptriangle.pos.sales.response.SalesResponse;
import com.apptriangle.pos.tableview.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zeeshan on 4/5/2018.
 */
public class ReportsFragment  extends Fragment {

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
        List<SalesResponse> list = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            SalesResponse tmp = new SalesResponse();
            list.add(tmp);
        }
        tableView = (TableView)contentView.findViewById(R.id.content_container);
        // Create our custom TableView Adapter
        TableViewAdapter adapter = new TableViewAdapter(getActivity());

        // Set this adapter to the our TableView
        tableView.setAdapter(adapter);
        initData();
        loadData();
        // Let's set datas of the TableView on the Adapter
        adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);

       /* recyclerView = (RecyclerView)contentView.findViewById(R.id.rcView);
        adapter = new ReportsAdaptor(getActivity(), list);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setNestedScrollingEnabled(false);
//        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);*/
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
        ((Activity) getActivity()).setTitle(getResources().getString(R.string.app_name));
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
//        void onCheckoutListener();
    }
}