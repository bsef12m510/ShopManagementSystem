package com.apptriangle.pos.sales.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.apptriangle.pos.R;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.fragment.VerifySalesFragment;
import com.apptriangle.pos.sales.response.SalesResponse;
import com.apptriangle.pos.stock.response.StockResponse;

import java.util.List;

/**
 * Created by zeeshan on 3/31/2018.
 */
public class VerifySaleAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private List<Product> transactionsList;
    private LayoutInflater mLayoutInflater;
    private int count;
    private boolean invoiceScreen;
    public Double totalAmount;
    public VerifySalesFragment parentFrag;

    public VerifySaleAdaptor(Context _mContext, List<Product> _transactionsList, boolean invoiceScreen) {
        this.mContext = _mContext;
        this.transactionsList = _transactionsList;
        this.invoiceScreen = invoiceScreen;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_verify_sales, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final Product product = transactionsList.get(position);
        viewHolder.product.setText(product.getProductName());
        if(invoiceScreen)
            viewHolder.checkbox.setVisibility(View.GONE);
        else{
            if(viewHolder.checkbox.isChecked()) {
                product.setChecked(true);
                totalAmount = totalAmount + (product.getOtherThanCurrentInventoryQty() * product.getQty());
            }else {
                product.setChecked(false);
                totalAmount = totalAmount - (product.getOtherThanCurrentInventoryQty() * product.getQty());
            }
            parentFrag.totalAmount = totalAmount;
            parentFrag.edtTotalAmnt.setText(totalAmount.toString());
        }
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

    public List<Product> getItems() {
        return transactionsList;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView product, brand, stock;
        CheckBox checkbox;

        MyViewHolder(View itemView) {
            super(itemView);

            product = (TextView) itemView.findViewById(R.id.verifyProduct);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);


        }
    }
}
