package com.shop.anandnameplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class refer extends AppCompatActivity {

    private TextInputEditText referral;
    private String code, phonenumber;
    private ProgressBar progressBar;
    private Button have_referral;
    private InputMethodManager imm;
    DatabaseReference reference;
    Query checkuser;

    public void btncontinue(View view)
    {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        progressBar.setVisibility(View.VISIBLE);
        code = referral.getText().toString();
        if(code.isEmpty() || code.length() < 6)
        {
            referral.setError("Enter a Valid Referral Code");
            progressBar.setVisibility(View.GONE);
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(referral, InputMethodManager.SHOW_IMPLICIT);
            return;
        }
        have_referral.setEnabled(false);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        checkuser = reference.orderByChild("code").equalTo(code);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String full, no;
                    int a, b;
                    full = snapshot.getValue().toString();
                    a = full.indexOf('+');
                    b = full.indexOf('=');
                    no = full.substring(a, b);
                    if(!no.equals(phonenumber))
                    {
                        int value = snapshot.child(no).child("no_of_referral").getValue(Integer.class) + 1;
                        reference.child(no).child("no_of_referral").setValue(value);
                        reference.child(phonenumber).child("used_Referral").setValue("TRUE");

                        Intent intent = new Intent(refer.this, main.class);
                        intent.putExtra("phonenumber",phonenumber);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        have_referral.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        referral.setError("You Can't Use Your Own Referral Code");
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(referral, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                else
                {
                    have_referral.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    referral.setError("No Referral Found");
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                have_referral.setEnabled(true);
                Toast.makeText(refer.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void no_referral(View view)
    {
        Intent intent = new Intent(refer.this, main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("phonenumber",phonenumber);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        hooks();
        phonenumber = getIntent().getStringExtra("phonenumber");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(referral, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hooks() {
        referral = findViewById(R.id.referral);
        progressBar = findViewById(R.id.progressBar);
        have_referral = findViewById(R.id.have_referral);
    }
}