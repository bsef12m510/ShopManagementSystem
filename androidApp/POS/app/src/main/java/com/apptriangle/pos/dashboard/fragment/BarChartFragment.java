package com.apptriangle.pos.dashboard.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apptriangle.pos.R;
import com.apptriangle.pos.dashboard.response.MonthlySalesResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by zawan on 4/3/18.
 */

public class BarChartFragment extends Fragment implements OnChartGestureListener {

    public List<MonthlySalesResponse> monthlySalesResponseList;
    public String[] labelsArray = new String[]{};
    public static BarChartFragment newInstance(String text, ArrayList<MonthlySalesResponse> monthlySalesResponse) {

        BarChartFragment f = new BarChartFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.monthlySalesResponseList = monthlySalesResponse;
        f.labelsArray = new String[]{};
        f.setArguments(b);

        return f;
    }


    private BarChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bar_frag, container, false);
        createLabelsArray();
        // create a new chart object
        mChart = new BarChart(getActivity());
        mChart.getDescription().setEnabled(false);
        mChart.setOnChartGestureListener(this);

      /*  MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv);*/

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        findHighestSale();
        mChart.setData(generateBarData(1, (float) findHighestSale(), 12));

        Legend l = mChart.getLegend();


        YAxis leftAxis = mChart.getAxisLeft();

        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
//        xAxis.setEnabled(false);

        xAxis.setDrawLabels(true);

        final String[] mLabels = new String[]{ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };

        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Log.i("zain", "value " + value);
                return labelsArray[(int) value % labelsArray.length];
            }


        });

        // programatically add the chart
        FrameLayout parent = (FrameLayout) v.findViewById(R.id.parentLayout);
        parent.addView(mChart);

        return v;
    }

    private double findHighestSale() {
        List<Double> saleAmtList = new ArrayList<>();
        for (int i = 0; i< monthlySalesResponseList.size(); i++)
            saleAmtList.add(monthlySalesResponseList.get(i).getSaleAmount());
        return Collections.max(saleAmtList);
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END");
        mChart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }


    protected BarData generateBarData(int dataSets, float range, int count) {

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();

        for (int i = 0; i < dataSets; i++) {

            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

//            entries = FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "stacked_bars.txt");

            for (int j = 0; j < count; j++) {
                entries.add(new BarEntry(j, (float) monthlySalesResponseList.get(j).getSaleAmount() , labelsArray[j]));   //(Math.random() * range) + range / 4
            }

            BarDataSet ds = new BarDataSet(entries, getLabel(i));
//            ds.setStackLabels(mLabels);
            ds.setColors(ColorTemplate.VORDIPLOM_COLORS);
            sets.add(ds);
        }

        BarData d = new BarData(sets);

        return d;
    }

    private String[] mLabels = new String[]{ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };
//    private String[] mXVals = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };

    private String getLabel(int i) {
        return labelsArray[i];
    }


    public void createLabelsArray(){
        labelsArray = new String[monthlySalesResponseList.size()];
        for(int i = 0; i< monthlySalesResponseList.size(); i++){
            labelsArray[i] = (getMonthLabel(convertDate(monthlySalesResponseList.get(i).getDate()).getMonth()));
        }
    }

    public Date  convertDate(String dateString){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date startDate;
        try {
            startDate = df.parse(dateString);
            return startDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMonthLabel(int i){
        if(i == 0)
            return "Jan";
        else if(i ==1)
            return "Feb";
        else if(i ==2)
            return "Mar";
        else if(i ==3)
            return "Apr";
        else if(i ==4)
            return "May";
        else if(i ==5)
            return "Jun";
        else if(i ==6)
            return "Jul";
        else if(i ==7)
            return "Aug";
        else if(i ==8)
            return "Sep";
        else if(i ==9)
            return "Oct";
        else if(i ==10)
            return "Nov";
        else
            return "Dec";
    }
}