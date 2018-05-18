package com.apptriangle.pos.InvoiceSearchFragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apptriangle.pos.InvoiceSearchFragment.fragment.InvoiceSearchFragment;
import com.apptriangle.pos.R;
import com.apptriangle.pos.model.Invoice;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.sales.adaptor.VerifySaleAdaptor;
import com.apptriangle.pos.sales.fragment.VerifySalesFragment;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private List<Invoice> transactionsList;
    private LayoutInflater mLayoutInflater;
    private int count;
    private boolean invoiceScreen;
    public Double totalAmount =0.0;
    public InvoiceSearchFragment parentFrag;

    public InvoiceAdapter(Context _mContext, List<Invoice> _transactionsList, boolean invoiceScreen) {
        this.mContext = _mContext;
        this.transactionsList = _transactionsList;
        this.invoiceScreen = invoiceScreen;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_invoice, parent, false);
        return new InvoiceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final InvoiceAdapter.MyViewHolder viewHolder = (InvoiceAdapter.MyViewHolder) holder;
        final Invoice product = transactionsList.get(position);
        viewHolder.product.setText(Integer.toString(product.invoiceId));
        viewHolder.qty.setText(Double.toString(product.amount_paid));
        viewHolder.price.setText(Double.toString(product.total_amount ));


        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentFrag.selectedInvoice = product;
                parentFrag.onButtonPressed();
            }
        });

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

    public List<Invoice> getItems() {
        return transactionsList;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView product, qty, price;
        CheckBox checkbox;
        LinearLayout container;

        MyViewHolder(View itemView) {
            super(itemView);

            product = (TextView) itemView.findViewById(R.id.verifyProduct);
            qty = (TextView) itemView.findViewById(R.id.qty);
            price = (TextView) itemView.findViewById(R.id.price);
            container = (LinearLayout) itemView.findViewById(R.id.container);



        }
    }
}
