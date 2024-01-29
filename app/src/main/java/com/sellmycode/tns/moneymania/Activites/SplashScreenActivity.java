package com.sellmycode.tns.moneymania.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import com.sellmycode.tns.moneymania.AdMobAds.Admob;
import com.sellmycode.tns.moneymania.R;

public class SplashScreenActivity extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Admob.loadInterstitialAd(this);
        Admob.loadVideoRewarded(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isConnected()){
                    Intent intent = new Intent(SplashScreenActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },3000);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}