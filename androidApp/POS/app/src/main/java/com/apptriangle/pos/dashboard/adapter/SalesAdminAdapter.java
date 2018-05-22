package com.apptriangle.pos.dashboard.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apptriangle.pos.R;
import com.apptriangle.pos.dashboard.fragment.AdminDashboardFragment;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.fragment.SalesFragment;
import com.apptriangle.pos.sales.response.SalesResponse;

import java.io.File;
import java.util.List;

/**
 * Created by zawan on 4/2/18.
 */

public class SalesAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private List<Product> attachmentList;
    private LayoutInflater mLayoutInflater;
    public AdminDashboardFragment parentFrag;
    File attFile;
    String fileExtension;


    public SalesAdminAdapter(Context _mContext, List<Product> attachmentList) {
        this.mContext = _mContext;
        this.attachmentList = attachmentList;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_sales_admin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final Product attachment = attachmentList.get(position);

        if (attachment.getQty() != null)
            viewHolder.qty.setText(Integer.toString(attachment.getQty()));
        viewHolder.name.setText(attachment.getProductName());

        if (position == 0)
            viewHolder.container.setBackground(mContext.getResources().getDrawable(R.drawable.round_bg));
        else if (position == 1)
            viewHolder.container.setBackground(mContext.getResources().getDrawable(R.drawable.round_bg1));
        else if (position == 2)
            viewHolder.container.setBackground(mContext.getResources().getDrawable(R.drawable.round_bg2));
        else if (position == 3)
            viewHolder.container.setBackground(mContext.getResources().getDrawable(R.drawable.round_bg3));
        else if (position == 4)
            viewHolder.container.setBackground(mContext.getResources().getDrawable(R.drawable.round_bg4));
        else
            viewHolder.container.setBackground(mContext.getResources().getDrawable(R.drawable.round_bg5));

        if (position == 5) {
            viewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parentFrag.openMoreTopSellingProducts();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return attachmentList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView attachment, download_view;
        TextView name, qty;
        LinearLayout container;

        MyViewHolder(View itemView) {
            super(itemView);
            qty = (TextView) itemView.findViewById(R.id.qty);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            name = (TextView) itemView.findViewById(R.id.name);

        }
    }


}
