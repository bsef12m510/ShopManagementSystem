package com.apptriangle.pos.stock.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apptriangle.pos.R;
import com.apptriangle.pos.stock.response.StockResponse;

import java.util.List;

/**
 * Created by zawan on 3/30/18.
 */

public class StockAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private List<StockResponse> transactionsList;
    private LayoutInflater mLayoutInflater;

    private String oldDate = "";
    private int count;
    private String currencySymbol="";

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }


    public StockAdaptor(Context _mContext, List<StockResponse> _transactionsList) {
        this.mContext = _mContext;
        this.transactionsList = _transactionsList;

        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_stock, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final StockResponse transactionInfo = transactionsList.get(position);
        String transactionDate ="";


        viewHolder.product.setText("product");




    }

    @Override
    public int getItemCount() {
//        return count;
        return transactionsList.size();
    }


    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public List<StockResponse> getItems() {
        return transactionsList;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView product, brand, stock;

        MyViewHolder(View itemView) {
            super(itemView);

            product = (TextView) itemView.findViewById(R.id.product);
            brand = (TextView) itemView.findViewById(R.id.brand);
            stock = (TextView) itemView.findViewById(R.id.stock);

        }
    }
}
