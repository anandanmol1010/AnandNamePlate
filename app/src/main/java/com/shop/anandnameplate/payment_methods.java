package com.shop.anandnameplate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class payment_methods extends AppCompatActivity {

    ArrayList<String> details = new ArrayList<>();
    private TextView itemPrice, extraCharge,totalAmount;
    private ProgressBar progressBar;
    private String activity;

    public void paytm(View view)
    {
        Intent intent = new Intent(payment_methods.this,pay.class);
        intent.putExtra("activity",activity);
        intent.putStringArrayListExtra("details",details);
        intent.putExtra("paymentMethod","Paytm");
        startActivity(intent);
    }

    public void googlepay(View view)
    {
        Intent intent = new Intent(payment_methods.this,pay.class);
        intent.putExtra("activity",activity);
        intent.putStringArrayListExtra("details",details);
        intent.putExtra("paymentMethod","Google Pay");
        startActivity(intent);
    }

    public void phonepe(View view)
    {
        Intent intent = new Intent(payment_methods.this,pay.class);
        intent.putExtra("activity",activity);
        intent.putStringArrayListExtra("details",details);
        intent.putExtra("paymentMethod","PhonePe");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

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
        progressBar = findViewById(R.id.progressBar);

        //*************Getting Data From Previous Activity***************
        activity = getIntent().getStringExtra("activity");
        details = getIntent().getStringArrayListExtra("details");

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
            AlertDialog.Builder builder = new AlertDialog.Builder(payment_methods.this);
            builder.setMessage("Do you want these products URGENTLY.\n" +
                    "If Yes, then you have to pay Rs. 10/- extra per product,\n" +
                    "otherwise no extra charges are applied.");
            builder.setTitle("Need Product Urgently");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String[] arr = details.get(1).split("==");

                    itemPrice.setText("Rs. "+details.get(6)+"/-");
                    extraCharge.setText("Rs. "+(arr.length)*10+"/-");
                    extraCharge.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                    totalAmount.setText("Rs. "+(Integer.parseInt(details.get(6))+((arr.length)*10))+"/-");

                    details.add(7,String.valueOf((arr.length)*10));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    itemPrice.setText("Rs. "+details.get(6)+"/-");
                    extraCharge.setText("NONE");
                    totalAmount.setText("Rs. "+details.get(6)+"/-");

                    details.add(7,"NONE");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}