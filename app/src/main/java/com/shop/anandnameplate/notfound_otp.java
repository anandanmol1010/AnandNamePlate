package com.shop.anandnameplate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class notfound_otp extends AppCompatActivity {

    private String verificationcode, code;
    private FirebaseAuth mAuth;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    private TextInputEditText otp;
    private Button btncontinue;
    private ProgressBar progressBar;
    private InputMethodManager imm;
    String phonenumber;

    public void signin(View view) {
        code = otp.getText().toString().trim();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        if ((code.isEmpty() || code.length() < 6)) {
            otp.setError("Enter a Valid OTP...");
            otp.requestFocus();
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(otp, InputMethodManager.SHOW_IMPLICIT);
            return;
        } else {
            btncontinue.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            verify(code);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notfound_otp);
        hooks();

        mAuth = FirebaseAuth.getInstance();

        phonenumber = getIntent().getStringExtra("phonenumber");
        progressBar.setVisibility(View.VISIBLE);
        sendVerificationCode(phonenumber);
    }

    private void hooks() {
        otp = findViewById(R.id.otp);
        btncontinue = findViewById(R.id.btncontinue);
        progressBar = findViewById(R.id.progressBar);
    }

    private void verify(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationcode, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            rootnode = FirebaseDatabase.getInstance();
                            reference = rootnode.getReference("Users");

                            UserDataClass userClass = new UserDataClass(0, generate_string(6),phonenumber,"FALSE","","","",UID);

                            reference.child(phonenumber).setValue(userClass);

                            Intent intent = new Intent(notfound_otp.this, user_filling_details.class);
                            intent.putExtra("phonenumber", phonenumber);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        else
                        {
                            btncontinue.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            otp.setError("Enter Correct OTP");
                            otp.requestFocus();
                            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(otp, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }

                });
    }

    private void sendVerificationCode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationcode = s;
            progressBar.setVisibility(View.GONE);
            Toast.makeText(notfound_otp.this, "Code Sent...", Toast.LENGTH_SHORT).show();
            otp.requestFocus();
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(otp, InputMethodManager.SHOW_IMPLICIT);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            otp.setText(code);

            if (code != null) {
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                progressBar.setVisibility(View.VISIBLE);
                verify(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(notfound_otp.this, e.getMessage() + " Please Retry!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(notfound_otp.this, register.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    };

    private String generate_string(int l) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < l; i++) {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
            return stringBuilder.toString();
    }
}