package com.apptriangle.pos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.apptriangle.pos.InvoiceSearchFragment.fragment.InvoiceSearchFragment;
import com.apptriangle.pos.dashboard.fragment.AdminDashboardFragment;
import com.apptriangle.pos.dashboard.fragment.DashboardFragment;
import com.apptriangle.pos.model.Invoice;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.purchase.fragemnt.PurchaseFragment;
import com.apptriangle.pos.reports.fragment.ReportsFragment;
import com.apptriangle.pos.reports.fragment.SalesReportFragment;
import com.apptriangle.pos.sales.fragment.InvoiceFragment;
import com.apptriangle.pos.sales.fragment.SalesFragment;
import com.apptriangle.pos.sales.fragment.VerifySalesFragment;
import com.apptriangle.pos.sales.response.SalesResponse;
import com.apptriangle.pos.stock.fragment.StockFragment;
import com.apptriangle.pos.util.expandableListAdapter.ExpandableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdminDashboardFragment.OnFragmentInteractionListener, ReportsFragment.OnFragmentInteractionListener, SalesReportFragment.OnFragmentInteractionListener,SalesFragment.OnFragmentInteractionListener, VerifySalesFragment.OnFragmentInteractionListener, InvoiceFragment.OnFragmentInteractionListener, InvoiceSearchFragment.OnFragmentInteractionListener, PurchaseFragment.OnFragmentInteractionListener, StockFragment.OnFragmentInteractionListener {

    ExpandableAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private boolean fromPlagFragment = false;
    private static String TAG_PLAG = "plagFrag";
    private static String TAG_ACCOUNT_INFO = "infoFrag";
    public static String TAG_PLANS = "plansFragment";
    public NavigationView navigationView;

    private TextView userNameTextView, emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = (View) navigationView.getHeaderView(0);
        userNameTextView = (TextView) headerView.findViewById(R.id.header_username);
        emailTextView = (TextView) headerView.findViewById(R.id.header_email);
        getSavedHeaderData();

       /* // get the listview
        expListView = (ExpandableListView) findViewById(R.id.navigationmenu);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
*/
//        getSavedHeaderData();
        replaceFragment(new AdminDashboardFragment(), "adminDashboardFragment");
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Dashboard");
        listDataHeader.add("Inventory");
        listDataHeader.add("Reports");
        listDataHeader.add("Logout");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Daily Report");
        top250.add("Monthly Report");
        top250.add("Customize Report");


        // Header, Child data

        listDataChild.put(listDataHeader.get(2), top250);
    }


    private void getSavedHeaderData() {
        SharedPreferences shared = getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        userNameTextView.setText(shared.getString("username", ""));
        emailTextView.setText(shared.getString("role", ""));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Fragment fragment = new AdminDashboardFragment();
            replaceFragment(fragment, "adminDashboardFragment");
        } else if (id == R.id.nav_sales) {
            Fragment fragment = new SalesReportFragment();
            replaceFragment(fragment, "salesReportFragment");

        } else if (id == R.id.nav_inventory) {
            Fragment fragment = new ReportsFragment();
            replaceFragment(fragment, "reportsFragment");
        } else if (id == R.id.sales) {
            Fragment fragment = new SalesFragment();
            replaceFragment(fragment, "salesFragment");
        }else if (id == R.id.purchase) {
            Fragment fragment = new PurchaseFragment();
            replaceFragment(fragment, "purchaseFragment");
        } else if (id == R.id.nav_logout) {
            confirmAndLogout(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void confirmAndLogout(final Activity activity) {
        if (!activity.isFinishing()) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle("Are you sure you want to logout?")
                    // set dialog message
                    .setCancelable(false).setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    logout();

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
//		alertDialog.setOutsideTouchable(true);

            alertDialog.show();
        }

    }

    void logout() {
        deleteApiKey();
        Intent intent = new Intent(MainDrawerActivity.this, PublicActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void deleteApiKey() {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.appTriangle.pos", Context.MODE_PRIVATE);

        prefs.edit().putString("api_key", "").apply();
        prefs.edit().putBoolean("is_remember_me", false).apply();
    }

    /*    @Override
        public void onFragmentInteraction(Uri uri) {

            fromPlagFragment = true;
        }*/
    public void replaceFragment(Fragment fragment, String TAG) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, TAG)
                .addToBackStack(TAG)
                .commit();
    }

    @Override
    public void onFragmentInteraction(ArrayList<Product> dataList, boolean isSale) {
        Fragment fragment = new ReportsFragment();
        navigationView.setCheckedItem(R.id.nav_inventory);
        ((ReportsFragment) fragment).responseList = dataList;
        ((ReportsFragment) fragment).showDashboardData = true;
        ((ReportsFragment) fragment).isSale = isSale;
        replaceFragment(fragment, "reportsFragment");


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AdminDashboardFragment myFragment = (AdminDashboardFragment)getFragmentManager().findFragmentByTag("adminDashboardFragment");
        if (keyCode == KeyEvent.KEYCODE_BACK && myFragment != null && myFragment.isVisible()) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }

        SalesFragment myFragment3 = (SalesFragment)getFragmentManager().findFragmentByTag("salesFragment");
        if (keyCode == KeyEvent.KEYCODE_BACK && myFragment3 != null && myFragment3.isVisible()) {
            Intent intent = new Intent(MainDrawerActivity.this, MainDrawerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        SalesReportFragment myFragment1 = (SalesReportFragment)getFragmentManager().findFragmentByTag("salesReportFragment");
        if (keyCode == KeyEvent.KEYCODE_BACK && myFragment1 != null && myFragment1.isVisible()) {
            Intent intent = new Intent(MainDrawerActivity.this, MainDrawerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        ReportsFragment myFragment2 = (ReportsFragment)getFragmentManager().findFragmentByTag("reportsFragment");
        if (keyCode == KeyEvent.KEYCODE_BACK && myFragment2 != null && myFragment2.isVisible()) {
            Intent intent = new Intent(MainDrawerActivity.this, MainDrawerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        PurchaseFragment myFragment4 = (PurchaseFragment) getFragmentManager().findFragmentByTag("purchaseFragment");
        if (keyCode == KeyEvent.KEYCODE_BACK && myFragment4 != null && myFragment4.isVisible()) {
            Intent intent = new Intent(MainDrawerActivity.this, MainDrawerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = null;
        if (requestCode == 1) {
            fragment = getFragmentManager().findFragmentByTag("plagFrag");
        }
        if (null != fragment)
            fragment.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCheckoutListenerPurchase() {

    }

    @Override
    public void onCheckoutListener(SalesResponse sale) {
        FragmentManager fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);

        VerifySalesFragment frg =  new VerifySalesFragment();
        frg.setCart(sale);
        ft.replace(R.id.content_frame, frg,"verifySalesFragment");

        ft.addToBackStack("verifySalesFragment");
        ft.commit();
    }


    @Override
    public void onFinishClicked(SalesResponse sale) {
        FragmentManager fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);

        InvoiceFragment frg = new InvoiceFragment();
        frg.cart =sale;
        ft.replace(R.id.content_frame,frg,"invoiceFragment");

        ft.addToBackStack("invoiceFragment");
        ft.commit();
    }

    @Override
    public void onFragmentInteraction() {

    }

    @Override
    public void onStockFragmentListener(){

    }


    @Override
    public void onInvoiceSearchFragmentInteraction(Invoice selectedInvoice) {
        FragmentManager fm = getFragmentManager();

        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left,
                R.anim.slide_out_right,R.anim.slide_in_left,
                R.anim.slide_out_right);

        InvoiceFragment fragment = new InvoiceFragment();

        fragment.selectedInvoice = selectedInvoice;
        ft.replace(R.id.content_frame,fragment ,"invoiceDetailFragment");

        ft.addToBackStack("invoiceDetailFragment");
        ft.commit();
    }
}
