package com.apptriangle.pos.sales.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    public Double totalAmount = 0.0;
    public VerifySalesFragment parentFrag;
    public boolean isFromInvoiceSearchScreen;

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
        if (!isFromInvoiceSearchScreen) {
            viewHolder.qty.setText(Integer.toString(product.getOtherThanCurrentInventoryQty()));
            viewHolder.price.setText(Double.toString(product.getOtherThanCurrentInventoryQty() * product.getUnitPrice())+"TK");
        } else {
            viewHolder.qty.setText(Integer.toString(product.getQty()));
            viewHolder.price.setText(Double.toString(product.getQty() * product.getUnitPrice()) + "TK");
        }
        if (invoiceScreen)
            viewHolder.checkbox.setVisibility(View.GONE);
        else {
            /*if(viewHolder.checkbox.isChecked()) {
                product.setChecked(true);
                totalAmount = totalAmount + (product.getOtherThanCurrentInventoryQty() * product.getUnitPrice());
            }else {
                product.setChecked(false);
                totalAmount = totalAmount - (product.getOtherThanCurrentInventoryQty() * product.getUnitPrice());
                if(totalAmount < 0)
                    totalAmount = 0.0;
            }*/
            if (product.isChecked()) {
                viewHolder.checkbox.setChecked(true);
                product.setChecked(true);
                totalAmount = totalAmount + (product.getOtherThanCurrentInventoryQty() * product.getUnitPrice());
            } else {
                product.setChecked(false);
                viewHolder.checkbox.setChecked(false);
                totalAmount = totalAmount - (product.getOtherThanCurrentInventoryQty() * product.getUnitPrice());
                if (totalAmount < 0)
                    totalAmount = 0.0;
            }
            parentFrag.totalAmount = totalAmount;
            parentFrag.edtTotalAmnt.setText(totalAmount.toString() + "TK");


            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        product.setChecked(true);
                        totalAmount = totalAmount + (product.getOtherThanCurrentInventoryQty() * product.getUnitPrice());
                    } else {
                        product.setChecked(false);
                        totalAmount = totalAmount - (product.getOtherThanCurrentInventoryQty() * product.getUnitPrice());
                        if (totalAmount < 0)
                            totalAmount = 0.0;
                    }
                    parentFrag.totalAmount = totalAmount;
                    parentFrag.edtTotalAmnt.setText(totalAmount.toString() + "TK");

                    if (parentFrag.paidAmount != null && !parentFrag.edtPaidAmnt.getText().toString().trim().equals("")) {
                        if (parentFrag.totalAmount - parentFrag.paidAmount < 0) {
                            parentFrag.dueAmount = 0.0;
                            parentFrag.edtDueAmnt.setText("0.0TK");
                        } else {
                            parentFrag.edtDueAmnt.setText(Double.toString(parentFrag.totalAmount - parentFrag.paidAmount) + "TK");
                            parentFrag.dueAmount = parentFrag.totalAmount - parentFrag.paidAmount;
                        }
                    }
                }
            });
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
        TextView product, qty, price;
        CheckBox checkbox;

        MyViewHolder(View itemView) {
            super(itemView);

            product = (TextView) itemView.findViewById(R.id.verifyProduct);
            qty = (TextView) itemView.findViewById(R.id.qty);
            price = (TextView) itemView.findViewById(R.id.price);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);


        }
    }
}
