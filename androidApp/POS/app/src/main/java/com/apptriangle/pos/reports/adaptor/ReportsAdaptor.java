package com.apptriangle.pos.reports.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apptriangle.pos.R;
import com.apptriangle.pos.sales.response.SalesResponse;

import java.io.File;
import java.util.List;

/**
 * Created by zeeshan on 4/5/2018.
 */
public class ReportsAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private List<SalesResponse> attachmentList;
    private LayoutInflater mLayoutInflater;

    File attFile;
    String fileExtension;


    public ReportsAdaptor(Context _mContext, List<SalesResponse> attachmentList) {
        this.mContext = _mContext;
        this.attachmentList = attachmentList;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_reports, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final SalesResponse attachment = attachmentList.get(position);




    }



    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView attachment, download_view;
        TextView name;


        MyViewHolder(View itemView) {
            super(itemView);


        }
    }



}
