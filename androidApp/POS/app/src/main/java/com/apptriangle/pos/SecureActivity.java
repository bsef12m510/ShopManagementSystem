package com.apptriangle.pos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.apptriangle.pos.dashboard.fragment.DashboardFragment;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.purchase.fragemnt.PurchaseFragment;
import com.apptriangle.pos.sales.fragment.InvoiceFragment;
import com.apptriangle.pos.sales.fragment.SalesFragment;
import com.apptriangle.pos.sales.fragment.VerifySalesFragment;
import com.apptriangle.pos.stock.fragment.StockFragment;

import java.util.List;

/**
 * Created by zeeshan on 3/28/2018.
 */
public class SecureActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener, SalesFragment.OnFragmentInteractionListener, VerifySalesFragment.OnFragmentInteractionListener, InvoiceFragment.OnFragmentInteractionListener, PurchaseFragment.OnFragmentInteractionListener {
    private FrameLayout fragmentContainer;
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialize();
        displayFragment(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initialize() {
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);
    }


    public void displayFragment(int key) {
        // get fragment manager
        fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);
        if (key == 1)
            ft.replace(R.id.fragmentContainer, new DashboardFragment(),"salesmanFragment");
       /* else if (key == 2)
            ft.replace(R.id.fragmentContainer, new RegisterFragment());
        else if (key == 3) {

        }*/

        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onSalesClickListener() {
        fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);

            ft.replace(R.id.fragmentContainer, new SalesFragment(),"salesFragment");

        ft.addToBackStack("salesFragment");
        ft.commit();
    }

    @Override
    public void onStockClickListener() {
        fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);

        ft.replace(R.id.fragmentContainer, new StockFragment(),"stockFragment");

        ft.addToBackStack("stockFragment");
        ft.commit();
    }

    @Override
    public void onPurchaseClickListener() {
        fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);

        PurchaseFragment fragment = new PurchaseFragment();

        ft.replace(R.id.fragmentContainer,fragment ,"purchaseFragment");

        ft.addToBackStack("purchaseFragment");
        ft.commit();
    }


    @Override
    public void onInvoiceClickListener() {
        fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);

        InvoiceFragment fragment = new InvoiceFragment();
        fragment.fromHome = true;
        ft.replace(R.id.fragmentContainer,fragment ,"invoiceFragment");

        ft.addToBackStack("invoiceFragment");
        ft.commit();
    }

    @Override
    public void onCheckoutListener(List<Product> cart) {
        fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);

        VerifySalesFragment frg =  new VerifySalesFragment();
        frg.setCart(cart);
        ft.replace(R.id.fragmentContainer, frg,"verifySalesFragment");

        ft.addToBackStack("verifySalesFragment");
        ft.commit();
    }

    @Override
    public void onFinishClicked() {
        fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);

        ft.replace(R.id.fragmentContainer, new InvoiceFragment(),"invoiceFragment");

        ft.addToBackStack("invoiceFragment");
        ft.commit();
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onCheckoutListenerPurchase() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentByTag("plagFrag");
        fragment.onActivityResult(requestCode, resultCode, data);
    }



}
