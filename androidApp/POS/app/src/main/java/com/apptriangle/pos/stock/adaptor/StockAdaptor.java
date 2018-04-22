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
        final StockResponse inventoryObj = transactionsList.get(position);
        String transactionDate ="";


        viewHolder.product.setText(inventoryObj.product.getProductType().getTypeName());
        viewHolder.brand.setText(inventoryObj.product.getBrand().getBrandName());
        viewHolder.model.setText(inventoryObj.product.getProductName());
        viewHolder.stock.setText(Integer.toString(inventoryObj.prod_quant));




    }

    @Override
    public int getItemCount() {
//        return count;
        if(transactionsList != null)
            return transactionsList.size();
        return 0;
    }


    public List<StockResponse> getItems() {
        return transactionsList;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView product, brand, stock, model;

        MyViewHolder(View itemView) {
            super(itemView);

            product = (TextView) itemView.findViewById(R.id.product);
            brand = (TextView) itemView.findViewById(R.id.brand);
            stock = (TextView) itemView.findViewById(R.id.stock);
            model = (TextView) itemView.findViewById(R.id.model);

        }
    }
}
