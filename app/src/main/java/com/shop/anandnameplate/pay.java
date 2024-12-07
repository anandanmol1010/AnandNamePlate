package com.shop.anandnameplate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.shop.anandnameplate.main.Username;
import static com.shop.anandnameplate.main.no;

public class pay extends AppCompatActivity {

    public static final String GPAY_PACKAGE = "com.google.android.apps.nbu.paisa.user";
    public static final String PHONEPE_PACKAGE = "com.phonepe.app";
    public static final String PAYTM_PACKAGE = "net.one97.paytm";
    private String UserName = "Ashok Anand",upiId = "ashokanand726@oksbi",note = "",status;
    private ArrayList<String> details;
    String[] iconUrl,name,qty,rate,englishName,hindiName;

    private SimpleDateFormat dateFormat;
    private FirebaseFirestore firebaseFirestore;
    private ImageView payment_image;
    private String paymentMethod,activity;
    private TextView itemPrice, extraCharge,totalAmount,payment_method;
    private Uri uri;

    public void pay(View view)
    {
        if(!isNetworkAvailable(getApplicationContext()))
            Toast.makeText(this, "Internet Not Found", Toast.LENGTH_SHORT).show();
        else
        {
            note = Username + "("+ no + ")";

            if(activity.equals("Single"))
            {
                if(details.get(4).equals("NONE"))
                    uri = getUpiPaymentUri(UserName,upiId,note, String.valueOf(Integer.parseInt(details.get(2))));
                else
                    uri = getUpiPaymentUri(UserName,upiId,note, String.valueOf(Integer.parseInt(details.get(2))+Integer.parseInt(details.get(4))));
            }

            else if(activity.equals("Cart"))
            {
                if(details.get(7).equals("NONE"))
                    uri = getUpiPaymentUri(UserName,upiId,note, String.valueOf(Integer.parseInt(details.get(6))));
                else
                    uri = getUpiPaymentUri(UserName,upiId,note, String.valueOf(Integer.parseInt(details.get(6))+Integer.parseInt(details.get(7))));
            }

            if(paymentMethod.equals("Google Pay"))
                paywith(GPAY_PACKAGE);
            else if(paymentMethod.equals("PhonePe"))
                paywith(PHONEPE_PACKAGE);
            else if(paymentMethod.equals("Paytm"))
                paywith(PAYTM_PACKAGE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        //************ToolBar******************
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");

        // *********************Variable Initialization***********************
        itemPrice = findViewById(R.id.itemPrice);
        extraCharge = findViewById(R.id.extraCharge);
        totalAmount = findViewById(R.id.totalAmount);
        payment_method = findViewById(R.id.payment_method);
        payment_image = findViewById(R.id.payment_image);

        //*************Getting Data From Previous Activity***************
        activity = getIntent().getStringExtra("activity");
        details = getIntent().getStringArrayListExtra("details");
        paymentMethod = getIntent().getStringExtra("paymentMethod");

        if(paymentMethod.equals("Paytm"))
            payment_image.setImageResource(R.drawable.paytm);
        else if(paymentMethod.equals("Google Pay"))
            payment_image.setImageResource(R.drawable.googlepay);
        else if(paymentMethod.equals("PhonePe"))
            payment_image.setImageResource(R.drawable.phonepe);

        payment_method.setText(paymentMethod);

        if(activity.equals("Single"))
        {
            itemPrice.setText("Rs. "+details.get(2)+"/-");
            if(details.get(4).equals("NONE"))
            {
                extraCharge.setText(details.get(4));
                totalAmount.setText("Rs. "+details.get(2)+"/-");
            }
            else
            {
                extraCharge.setText("Rs. "+details.get(4)+"/-");
                extraCharge.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                totalAmount.setText("Rs. "+(Integer.parseInt(details.get(2))+Integer.parseInt(details.get(4)))+"/-");
            }
        }

        else if(activity.equals("Cart"))
        {
            itemPrice.setText("Rs. "+details.get(6)+"/-");
            if(details.get(7).equals("NONE"))
            {
                extraCharge.setText(details.get(7));
                totalAmount.setText("Rs. "+details.get(6)+"/-");
            }
            else
            {
                extraCharge.setText("Rs. "+details.get(7)+"/-");
                extraCharge.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                totalAmount.setText("Rs. "+(Integer.parseInt(details.get(6))+Integer.parseInt(details.get(7)))+"/-");
            }
        }
    }

    public boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private static Uri getUpiPaymentUri(String name,String upiID,String note,String amount)
    {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa",upiID)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",note)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();
    }

    private void paywith(String packageName)
    {
        if(isAppInstalled(this,packageName))
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(packageName);
            startActivityForResult(intent,0);
        }

        else
        {
            Toast.makeText(this, paymentMethod+" is not installed.Please install and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isAppInstalled(Context context,String packageName)
    {
        try
        {
            context.getPackageManager().getApplicationInfo(packageName,0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(paymentMethod.equals("Google Pay") || paymentMethod.equals("Paytm"))
        {
            if(data != null)
            {
                status = data.getStringExtra("Status").toLowerCase();
            }

            if(RESULT_OK == resultCode && status.equals("success"))
            {
                Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
            }

            else
            {
                Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
            }
        }

        else if(paymentMethod.equals("PhonePe"))
        {
            switch (requestCode) {
                case 0:
                    if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                        if (data != null) {
                            String response = data.getStringExtra("response");
                            response = response.substring(response.indexOf('S'),'&'-1);
                            String result = response.substring(response.indexOf('='+1)).toLowerCase();

                            if(result.equals("success"))
                            {
                                Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        if(activity.equals("Single"))
        {
            dateFormat = new SimpleDateFormat("dd MMM, yyyy");
            String date = dateFormat.format(Calendar.getInstance().getTime());
            date = date.substring(0,date.indexOf("2020")+4);

            HashMap map = new HashMap();
            map.put("icon",details.get(0));
            map.put("name",details.get(1));
            map.put("rate","Rs. "+details.get(2)+"/-");
            map.put("qty",details.get(3));
            map.put("extraCharge",(details.get(4).equals("NONE")) ? details.get(4) : "Rs. "+details.get(4)+"/-");
            map.put("orderId","Anmol");
            map.put("orderedOn",date);
            map.put("deliveredOn","Not Delivered Yet");


            if(details.get(1).equals("OG Dress Name Plate"))
            {
                map.put("englishName",details.get(5));
                map.put("hindiName",details.get(6));
            }
            else if(details.get(1).equals("Cloth Name Plate"))
            {
                map.put("englishName",details.get(5));
            }

            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(no).collection("MY ORDER").document(details.get(1).toUpperCase()).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(pay.this, "Order Place Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(pay.this,main.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(pay.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else if(activity.equals("Cart"))
        {
            iconUrl = details.get(0).split("==");
            name = details.get(1).split("==");
            qty = details.get(2).split("==");
            rate = details.get(3).split("==");
            englishName = details.get(4).split("==");
            hindiName = details.get(5).split("==");

            for(int i=0;i<iconUrl.length;i++)
            {
                HashMap map = new HashMap();
                map.put("icon",iconUrl[i]);
                map.put("name",name[i]);
                map.put("rate",rate[i]);
                map.put("qty",qty[i]);
                map.put("extraCharge",(details.get(7).equals("NONE")) ? details.get(7) : "Rs. "+(Integer.parseInt(details.get(7))/iconUrl.length)+"/-");
                map.put("orderId",i);
                map.put("orderedOn","Today "+i);
                map.put("deliveredOn","Not Delivered Yet");


                if(name[i].equals("OG Dress Name Plate"))
                {
                    map.put("englishName",englishName[i]);
                    map.put("hindiName",hindiName[i]);
                }
                else if(name[i].equals("Cloth Name Plate"))
                {
                    map.put("englishName",englishName[i]);
                }

                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Users").document(no).collection("MY ORDER").document(name[i].toUpperCase()).set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {

                                }
                                else
                                {
                                    Toast.makeText(pay.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            Toast.makeText(pay.this, "Order Place Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(pay.this,main.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}