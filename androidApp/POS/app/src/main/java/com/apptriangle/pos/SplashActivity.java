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

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class SplashActivity extends AppCompatActivity {
    public boolean is_remember_me, trialExpired;
    public LoginResponse loginResponse;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final long ONE_DAY = 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String installDate = preferences.getString("InstallDate", null);
        if (installDate == null) {
            // First run, so save the current date
            SharedPreferences.Editor editor = preferences.edit();
            Date now = new Date();
            String dateString = formatter.format(now);
            editor.putString("InstallDate", dateString);
            // Commit the edits!
            editor.commit();
        } else {
            // This is not the 1st run, check install date
            try {
                Date before = (Date) formatter.parse(installDate);
                Date now = new Date();
                long diff = now.getTime() - before.getTime();
                long days = diff / ONE_DAY;
                if (days > 10) { // More than 30 days?
                    // Expired !!!
                    trialExpired = true;
                }
            } catch (Exception e) {
            }
        }
        if (trialExpired)
            finish();
        else {
            setContentView(R.layout.activity_splash);
            checkRememberMe();
            if (is_remember_me) {
                getUserData();
                startDrawerActivity(loginResponse);
            } else {
                // Start home activity
                Intent intent = new Intent(SplashActivity.this, PublicActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            // close splash activity
            finish();
        }
    }

    public void startDrawerActivity(LoginResponse response) {

        Intent intent;
        /*if(userId.equalsIgnoreCase("zawan"))
            intent = new Intent(PublicActivity.this,MainDrawerActivity.class);
        else
            intent = new Intent(PublicActivity.this,SecureActivity.class);*/
        if (response.getRole_id().equalsIgnoreCase("admin"))
            intent = new Intent(SplashActivity.this, MainDrawerActivity.class);
        else
            intent = new Intent(SplashActivity.this, SecureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void checkRememberMe() {
        SharedPreferences shared = SplashActivity.this.getSharedPreferences("com.appTriangle.pos", MODE_PRIVATE);
        is_remember_me = shared.getBoolean("is_remember_me", false);

    }

    public void getUserData() {
        loginResponse = new LoginResponse();
        SharedPreferences prefs = this.getSharedPreferences(
                "com.appTriangle.pos", MODE_PRIVATE);

        loginResponse.setApi_key(prefs.getString("api_key", ""));
        loginResponse.setRole_id(prefs.getString("role", ""));
        loginResponse.setUsername(prefs.getString("username", ""));

    }
}
