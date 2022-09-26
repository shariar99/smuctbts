package com.approxsoft.buslocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class Login extends AppCompatActivity {


    EditText email, password;
    Button logInBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.InputEmail);
        password = findViewById(R.id.inputPassword);
        logInBtn = findViewById(R.id.forgtBtn);
        progressBar = findViewById(R.id.progressbar);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN );

        String sm= "shariar@gmail.com";

        String numb= email.getText().toString();
        String em = numb.concat(sm);
        //hide keyboard
    email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });



        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Enter pass and id", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.signInWithEmailAndPassword(email.getText().toString().concat(sm), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Successfully Log In", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                progressBar.setVisibility(View.INVISIBLE);
                                startActivity(intent);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);

                                Toast.makeText(Login.this, "Please Enter Right ID or PASSWORD", Toast.LENGTH_LONG).show();


                            }
                        }
                    });
                }
            }
        });




    }
    public void signUpBtn(View view){
        Intent intent = new Intent(Login.this,SignUp.class);
        startActivity(intent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void forgetPass(View view){
        Intent intent = new Intent(Login.this,forgetPass.class);
        startActivity(intent);
    }

    //double backpress to exit
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }




}