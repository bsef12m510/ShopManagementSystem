package com.apptriangle.pos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.apptriangle.pos.Login.response.LoginResponse;


public class SplashActivity extends AppCompatActivity {
    public boolean is_remember_me;
    public LoginResponse loginResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkRememberMe();
        if(is_remember_me){
            getUserData();
            startDrawerActivity(loginResponse);
        }else {
            // Start home activity
            Intent intent = new Intent(SplashActivity.this, PublicActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        // close splash activity
        finish();
    }

    public void startDrawerActivity(LoginResponse response){

        Intent intent;
        /*if(userId.equalsIgnoreCase("zawan"))
            intent = new Intent(PublicActivity.this,MainDrawerActivity.class);
        else
            intent = new Intent(PublicActivity.this,SecureActivity.class);*/
        if(response.getRole_id().equalsIgnoreCase("admin"))
            intent = new Intent(SplashActivity.this,MainDrawerActivity.class);
        else
            intent = new Intent(SplashActivity.this,SecureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void checkRememberMe()
    {
        SharedPreferences shared = SplashActivity.this.getSharedPreferences("com.appTriangle.pos", Context.MODE_PRIVATE);
        is_remember_me = shared.getBoolean("is_remember_me", false);

    }

    public void getUserData() {
        loginResponse = new LoginResponse();
        SharedPreferences prefs = this.getSharedPreferences(
                "com.appTriangle.pos", Context.MODE_PRIVATE);

        loginResponse.setApi_key(prefs.getString("api_key", ""));
        loginResponse.setRole_id(prefs.getString("role", ""));
        loginResponse.setUsername(prefs.getString("username", ""));

    }
}
