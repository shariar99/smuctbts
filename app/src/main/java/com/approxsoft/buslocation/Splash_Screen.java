package com.approxsoft.buslocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class Splash_Screen extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(manager).getActiveNetworkInfo();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (null!=activeNetwork)
        {
            SystemClock.sleep(3000);
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                Intent loginIntent;
                if (currentUser != null){
                    loginIntent = new Intent(Splash_Screen.this, MainActivity.class);
                }
                else {
                    loginIntent = new Intent(Splash_Screen.this, Login.class);
                }
                startActivity(loginIntent);
                finish();

            }if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

            if (currentUser != null) {
                Intent loginIntent = new Intent(Splash_Screen.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
            } else {
                Intent loginIntent = new Intent(Splash_Screen.this, Login.class);
                startActivity(loginIntent);
                finish();
            }
        }
        }else {
            ///Toast.makeText(SplashActivity.this,"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
            Intent splashIntent = new Intent(Splash_Screen.this, Splash_Screen.class);
            startActivity(splashIntent);
        }
        super.onStart();
    }
}