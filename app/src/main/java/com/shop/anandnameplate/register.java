package com.shop.anandnameplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {

    private View divider;
    private TextInputEditText mobile;
    private Button sendotp;
    private TextInputLayout textInputLayout;
    private TextView signin;
    private Spinner spinner;
    private ProgressBar progressBar,mainProgressBar;
    private String code,phonenumber;
    private InputMethodManager imm;
    DatabaseReference reference;
    Query checkuser;
    public static String NO = "";
    public static String user_Name = "";
    public static String userImageUrl = "";

    public void send_otp(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

        phonenumber = mobile.getText().toString().trim();

        if (phonenumber.isEmpty() || phonenumber.length() < 10) {
            progressBar.setVisibility(View.GONE);
            mobile.setError("Enter a Valid Phone Number");
            mobile.requestFocus();
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mobile, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        sendotp.setEnabled(false);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        phonenumber = "+" + code + phonenumber;

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        checkuser = reference.orderByChild("phone_number").equalTo(phonenumber);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String used_Referral = snapshot.child(phonenumber).child("used_Referral").getValue(String.class);
                    String name = snapshot.child(phonenumber).child("name").getValue(String.class);
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(register.this, found_otp.class);
                    intent.putExtra("phonenumber", phonenumber);
                    intent.putExtra("used_Referral", used_Referral);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(register.this, notfound_otp.class);
                    intent.putExtra("phonenumber", phonenumber);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                sendotp.setEnabled(true);
                Toast.makeText(register.this, error.getMessage() , Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hooks();

        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));
    }

    private void hooks()
    {
        spinner = findViewById(R.id.country);
        mobile = findViewById(R.id.mobile);
        sendotp = findViewById(R.id.sendotp);
        textInputLayout=findViewById(R.id.textInputLayout);
        progressBar = findViewById(R.id.progressBar);
        mainProgressBar = findViewById(R.id.mainProgressBar);
        signin = findViewById(R.id.userPhoneNumber);
        divider = findViewById(R.id.divider1);
    }

    @Override
    protected void onStart() {
        super.onStart();

            if (FirebaseAuth.getInstance().getCurrentUser() != null)
            {
                if(!isNetworkAvailable(getApplicationContext()))
                    Toast.makeText(this, "Internet Not Found", Toast.LENGTH_SHORT).show();
                else
                {
                    signin.setVisibility(View.INVISIBLE);
                    divider.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    textInputLayout.setVisibility(View.INVISIBLE);
                    sendotp.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    mainProgressBar.setVisibility(View.VISIBLE);

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users");
                    checkuser = reference.orderByChild("uid").equalTo(uid);

                    checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                String full;
                                int a, b;
                                full = snapshot.getValue().toString();
                                a = full.indexOf('+');
                                b = full.indexOf('=');
                                NO = full.substring(a, b);

                                String used_Referral = snapshot.child(NO).child("used_Referral").getValue(String.class);
                                String name = snapshot.child(NO).child("name").getValue(String.class);
                                userImageUrl = snapshot.child(NO).child("imageUrl").getValue(String.class);
                                user_Name = name;

                                if(name.isEmpty())
                                {
                                    Intent intent = new Intent(register.this, user_filling_details.class);
                                    intent.putExtra("phonenumber", NO);
                                    intent.putExtra("used_Referral", used_Referral);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                                else
                                {
                                    Intent intent = new Intent(register.this, main.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(register.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        }
    }

    public boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}