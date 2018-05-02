package com.apptriangle.pos;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.apptriangle.pos.Login.fragment.LoginFragment;
import com.apptriangle.pos.Login.response.LoginResponse;

/**
 * Created by zeeshan on 3/27/2018.
 */
public class PublicActivity extends AppCompatActivity implements LoginFragment.OnLoginResponseListener{

    private Integer user_id;
    private FrameLayout fragmentContainer;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        initialize();
        displayFragment(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_public, menu);
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
            ft.replace(R.id.fragmentContainer, new LoginFragment());
        else if (key == 2){}
//            ft.replace(R.id.fragmentContainer, new RegisterFragment());
        else if (key == 3) {
//            VerifyFragment verifyFragment = new VerifyFragment();
//            verifyFragment.setUser_id(user_id.toString());
//            ft.replace(R.id.fragmentContainer, verifyFragment);
        }

        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onLoginResponse(LoginResponse loginResponseData,String userId) {
        if(loginResponseData != null /*&& loginResponseData.getVerified().equalsIgnoreCase("1")*/) {
            if(loginResponseData.getApi_key()!=null)
                storeUserData(loginResponseData);
            Toast.makeText(PublicActivity.this, "Login with user : " + loginResponseData.getUsername(), Toast.LENGTH_SHORT).show();
            startDrawerActivity(loginResponseData,userId);
        }else{
            startDrawerActivity(loginResponseData,userId);
            Toast.makeText(PublicActivity.this, "User not verified" , Toast.LENGTH_SHORT).show();
        }
    }

 /*   @Override
    public void onRegisterClick() {
        displayFragment(2);
    }*/

    @Override
    public void onRegisterClick() {
        displayFragment(2);
    }


/*    @Override
    public void onRegisterResponse(RegisterResponse registerResponse, boolean isGoogleSignin) {
        if (registerResponse.getApiKey() != null) {
            if (registerResponse.getUserId() != null) {
                user_id = registerResponse.getUserId();
                displayFragment(1);
            }
            if (!isGoogleSignin)
                displayFragment(3);
            else {
                displayFragment(1);
            }
        }else{
            Toast.makeText(PublicActivity.this, registerResponse.getError(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onVerifyResponse(boolean isVerified) {
        if (isVerified) {
            Toast.makeText(PublicActivity.this, "Verified", Toast.LENGTH_LONG).show();
            startDrawerActivity();
        }
    }*/

    public void storeUserData(LoginResponse response) {
        SharedPreferences prefs = this.getSharedPreferences(
                "com.appTriangle.pos", Context.MODE_PRIVATE);

        prefs.edit().putString("api_key", response.getApi_key()).apply();
        prefs.edit().putString("role", response.getRole_id() ).apply();
        prefs.edit().putString("username", response.getUsername() ).apply();
        boolean isPremium=false;
        /*if(response.getPremium().equals("1"))
        {
            isPremium = true;
        }*/
//        prefs.edit().putBoolean("membership", isPremium ).apply();
    }

    public void startDrawerActivity(LoginResponse response, String userId){

        Intent intent;
        /*if(userId.equalsIgnoreCase("zawan"))
            intent = new Intent(PublicActivity.this,MainDrawerActivity.class);
        else
            intent = new Intent(PublicActivity.this,SecureActivity.class);*/
        if(response.getRole_id().equalsIgnoreCase("admin"))
            intent = new Intent(PublicActivity.this,MainDrawerActivity.class);
        else
            intent = new Intent(PublicActivity.this,SecureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
