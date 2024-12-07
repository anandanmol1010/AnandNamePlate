package com.shop.anandnameplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Delivery extends AppCompatActivity {

    private String ProductName,ProductCategory,englishName,hindiName,product,iconUrl,name,rate;
    private FirebaseFirestore firebaseFirestore;
    private ImageView productIcon;
    private TextView productName,productQtyTitle,productQty,productPrice,priceDetails,itemPriceTitle,itemPrice,qtyTitle,qty, extraChargeTitle, extraCharge,totalAmount,ogHindi,ogEnglish,cloth_english;
    private Button minus,plus,redeem,Continue;
    private ProgressBar progressBar;
    private ConstraintLayout outer_constraintLayout;
    private LinearLayout linearLayout;
    private ArrayList<String> details = new ArrayList<>();

    public void Continuebtn(View view)
    {
        int intPrice;
        String strPrice;

        Intent intent = new Intent(Delivery.this, payment_methods.class);
        details.add(0,iconUrl);
        details.add(1,name);

        strPrice = productPrice.getText().toString();
        intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')));
        details.add(2,String.valueOf(intPrice));

        details.add(3,productQty.getText().toString());

        if(extraCharge.getText().toString().equals("NONE"))
        {
            details.add(4, extraCharge.getText().toString());
        }
        else
        {
            strPrice = extraCharge.getText().toString();
            intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')));
            details.add(4,String.valueOf(intPrice));
        }

        if(name.equals("OG Dress Name Plate"))
        {
            details.add(5,ogEnglish.getText().toString());
            details.add(6,ogHindi.getText().toString());
        }
        else if(name.equals("Cloth Name Plate"))
        {
            details.add(5,cloth_english.getText().toString());
        }
        intent.putExtra("activity","Single");
        intent.putStringArrayListExtra("details",details);
        startActivity(intent);
    }

    public void minusBtn(View view)
    {
        if(Integer.parseInt(productQty.getText().toString())>2)
        {
            //***********************Calculating Qunatity**************************
            int Qty = Integer.parseInt(productQty.getText().toString()) - 1;
            productQty.setText(String.valueOf(Qty));
            qty.setText(String.valueOf(Qty));

            //***********************Calculating Amount**************************
            int intPrice = 0;
            String strPrice = itemPrice.getText().toString();
            if(extraCharge.getText().equals("NONE"))
            {
                intPrice = Integer.parseInt(strPrice.substring( strPrice.indexOf(' ')+1,strPrice.indexOf('/')))*Integer.parseInt(productQty.getText().toString());
                productPrice.setText("Rs. "+String.valueOf(intPrice)+"/-");
            }
            else
            {
                intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')))*Integer.parseInt(productQty.getText().toString())+10;
                productPrice.setText("Rs. "+String.valueOf(intPrice-10)+"/-");
            }
            totalAmount.setText("Rs. "+String.valueOf(intPrice)+"/-");
        }
        else if(productQty.getText().equals("2"))
        {
            //***********************Setting Background**************************
            minus.setBackgroundResource(R.drawable.faded_circle_border);
            minus.setTextColor(ColorStateList.valueOf(Color.parseColor("#23000000")));

            //***********************Calculating Qunatity**************************
            int Qty = Integer.parseInt(productQty.getText().toString()) - 1;
            productQty.setText(String.valueOf(Qty));
            qty.setText(String.valueOf(Qty));

            //***********************Calculating Amount**************************
            int intPrice = 0;
            String strPrice = itemPrice.getText().toString();
            if(extraCharge.getText().equals("NONE"))
            {
                intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')))*Integer.parseInt(productQty.getText().toString());
                productPrice.setText("Rs. "+String.valueOf(intPrice)+"/-");
            }
            else
            {
                intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')))*Integer.parseInt(productQty.getText().toString())+10;
                productPrice.setText("Rs. "+String.valueOf(intPrice-10)+"/-");
            }
            totalAmount.setText("Rs. "+String.valueOf(intPrice)+"/-");
        }
    }

    public void plusBtn(View view)
    {
        //***********************Setting Background**************************
        minus.setBackgroundResource(R.drawable.circle_border);
        minus.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));

        //***********************Calculating Qunatity**************************
        int Qty = Integer.parseInt(productQty.getText().toString())+1;
        productQty.setText(String.valueOf(Qty));
        qty.setText(String.valueOf(Qty));

        //***********************Calculating Amount**************************
        int intPrice = 0;
        String strPrice = itemPrice.getText().toString();
        if(extraCharge.getText().equals("NONE"))
        {
            intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')))*Integer.parseInt(productQty.getText().toString());
            productPrice.setText("Rs. "+String.valueOf(intPrice)+"/-");
        }
        else
        {
            intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')))*Integer.parseInt(productQty.getText().toString())+10;
            productPrice.setText("Rs. "+String.valueOf(intPrice-10)+"/-");
        }
        totalAmount.setText("Rs. "+String.valueOf(intPrice)+"/-");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        //************ToolBar******************
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        // *********************Variable Initialization***********************
        productIcon = findViewById(R.id.productIcon);

        ogHindi = findViewById(R.id.ogHindi);
        ogEnglish = findViewById(R.id.ogEnglish);
        cloth_english = findViewById(R.id.cloth_english);
        productName = findViewById(R.id.productName);
        productQtyTitle = findViewById(R.id.productQtyTitle);
        productQty = findViewById(R.id.productQty);
        productPrice = findViewById(R.id.productPrice);
        priceDetails = findViewById(R.id.productDetails);
        itemPriceTitle = findViewById(R.id.itemPriceTitle);
        itemPrice = findViewById(R.id.itemPrice);
        qtyTitle = findViewById(R.id.qtyTitle);
        qty = findViewById(R.id.qty);
        extraChargeTitle = findViewById(R.id.extraChargeTitle);
        extraCharge = findViewById(R.id.extraCharge);
        totalAmount = findViewById(R.id.totalAmount);

        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        redeem = findViewById(R.id.redeem);
        Continue = findViewById(R.id.Continue);

        progressBar = findViewById(R.id.progressBar);

        outer_constraintLayout = findViewById(R.id.outer_constraintLayout);
        linearLayout = findViewById(R.id.linearLayout);

        //***************************Visibility**********************************
        outer_constraintLayout.setVisibility(View.INVISIBLE);
        ogEnglish.setVisibility(View.INVISIBLE);
        ogHindi.setVisibility(View.INVISIBLE);
        cloth_english.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        Continue.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        //********************************Alert Dialog Box*********************************
        AlertDialog.Builder builder = new AlertDialog.Builder(Delivery.this);
        builder.setMessage("Do you want your product URGENTLY.\n" +
                "If Yes, then you have to pay Rs. 10/- extra,\n" +
                "otherwise no extra charges are applied.");
        builder.setTitle("Need Product Urgently");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                extraCharge.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                extraCharge.setText("Rs. 10/-");

                String strPrice = totalAmount.getText().toString();
                int intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')))*Integer.parseInt(productQty.getText().toString())+10;
                totalAmount.setText("Rs. "+String.valueOf(intPrice)+"/-");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(totalAmount.getText().equals(productPrice.getText()))
                            {
                                String strPrice = totalAmount.getText().toString();
                                int intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')))*Integer.parseInt(productQty.getText().toString())+10;
                                totalAmount.setText("Rs. "+String.valueOf(intPrice)+"/-");
                            }
                        }
                    }, 1000);
                }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                extraCharge.setText("NONE");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //*************Getting Data From Previous Activity***************
        englishName = getIntent().getStringExtra("englishName");
        hindiName = getIntent().getStringExtra("hindiName");
        product = getIntent().getStringExtra("product");

        //*********************Finding Product Category and Name*********************
        if(product.equals("Cloth Name Plate"))
        {
            ProductCategory = "Name Plate";
            ProductName = "Cloth Name Plate";
        }

        else if(product.equals("OG Dress Name Plate"))
        {
            ProductCategory = "Name Plate";
            ProductName = "OG Dress Name Plate";
        }

        else if(product.contains("Ribbon"))
        {
            ProductCategory = "Ribbon";
            ProductName = product;
        }

        else if(product.contains("Medal"))
        {
            ProductCategory = "Medal";
            ProductName = product;
        }

        //*******************Product Details*****************
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(ProductCategory).document(ProductName.toUpperCase())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    //***************************Getting Data From Firebase**********************************
                    iconUrl = documentSnapshot.get("icon").toString();
                    name = documentSnapshot.get("name").toString();
                    rate = documentSnapshot.get("rate").toString();

                    //***************************Visibility**********************************
                    progressBar.setVisibility(View.INVISIBLE);
                    outer_constraintLayout.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    Continue.setVisibility(View.VISIBLE);
                    if(ProductName.equals("Cloth Name Plate"))
                    {
                        cloth_english.setVisibility(View.VISIBLE);
                    }
                    else if(ProductName.equals("OG Dress Name Plate"))
                    {
                        ogEnglish.setVisibility(View.VISIBLE);
                        ogHindi.setVisibility(View.VISIBLE);
                    }

                    //***************************Loading**********************************
                    Glide.with(Delivery.this).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.small_placeholder)).into(productIcon);
                    ogEnglish.setText(englishName);
                    ogHindi.setText(hindiName);
                    cloth_english.setText(englishName);
                    productName.setText(name);
                    productPrice.setText(rate);
                    itemPrice.setText(rate);
                    totalAmount.setText(rate);
                    productQty.setText("1");
                    qty.setText("1");
                }
                else
                {
                    Toast.makeText(Delivery.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Delivery.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
