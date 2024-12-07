package com.shop.anandnameplate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static com.shop.anandnameplate.register.NO;

public class ProductDetails extends AppCompatActivity {

    private boolean ADDED_TO_WISHLIST = false;
    private String productName,productCategory;
    private String iconUrl,name,rate,des,phonenumber;
    private FirebaseFirestore firebaseFirestore;
    private TextView productDetails_icon_text,productDetails_icon_ogh_text,productDetails_icon_oge_text,productDetails_Name,productDetails_Rate,productDetails_Des,description;
    private ImageView productDetails_Icon;
    private FloatingActionButton wishlistbtn;
    private Button buybtn;
    private ProgressBar progressBar;
    private View imageDivider,desDivider;
    private LinearLayout cartbtn;

    public void wishlist(View view)
    {
        if(ADDED_TO_WISHLIST)
        {
            final progress_bar progress_bar = new progress_bar(ProductDetails.this);
            progress_bar.startDialog();
            ADDED_TO_WISHLIST = false;
            wishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(phonenumber).collection("MY WISHLIST").document(name.toUpperCase())
                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        progress_bar.dismissDialog();
                        Toast.makeText(ProductDetails.this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progress_bar.dismissDialog();
                        Toast.makeText(ProductDetails.this, "Can't able to remove from Wishlist", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress_bar.dismissDialog();
                    Toast.makeText(ProductDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        else
        {
            final progress_bar progress_bar = new progress_bar(ProductDetails.this);
            progress_bar.startDialog();
            ADDED_TO_WISHLIST = true;
            wishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.colourPrimary));

            Map<String,Object> map = new HashMap<>();
            map.put("icon",iconUrl);
            map.put("name",name);
            map.put("rate",rate);

            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(phonenumber).collection("MY WISHLIST").document(name.toUpperCase()).set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    progress_bar.dismissDialog();
                                    Toast.makeText(ProductDetails.this, "Added to Wishist", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    progress_bar.dismissDialog();
                                    Toast.makeText(ProductDetails.this, "Can't able to add to Wishlist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress_bar.dismissDialog();
                        Toast.makeText(ProductDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    public void buy(View view)
    {
        if(productDetails_Name.getText().toString().contains("Name Plate"))
        {
            BottomSheet bottomSheet = new BottomSheet(name,"buy",iconUrl,rate);
            bottomSheet.show(getSupportFragmentManager(), "TAG");
        }
        else
        {
            Intent intent = new Intent(ProductDetails.this,Delivery.class);
            intent.putExtra("product",productDetails_Name.getText().toString());
            startActivity(intent);
        }
    }

    public void cart(View view)
    {
        firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String,Object> map = new HashMap<>();
        map.put("icon",iconUrl);
        map.put("name",name);
        map.put("qty","1");
        map.put("rate",rate);

        if(productDetails_Name.getText().toString().contains("Name Plate"))
        {
            BottomSheet bottomSheet = new BottomSheet(name,"cart",iconUrl,rate);
            bottomSheet.show(getSupportFragmentManager(), "TAG");
        }
        else
        {
            final progress_bar progress_bar = new progress_bar(ProductDetails.this);
            progress_bar.startDialog();
            firebaseFirestore.collection("Users").document(phonenumber).collection("MY CART").document(name.toUpperCase()).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progress_bar.dismissDialog();
                            Toast.makeText(ProductDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress_bar.dismissDialog();
                    Toast.makeText(ProductDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //************ToolBar******************
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // *********************Variable Initialization***********************
        productDetails_icon_text = findViewById(R.id.productDetails_icon_cloth_text);
        productDetails_icon_ogh_text = findViewById(R.id.productDetails_icon_ogh_text);
        productDetails_icon_oge_text = findViewById(R.id.productDetails_icon_oge_text);
        productDetails_Icon = findViewById(R.id.productDetails_icon);
        productDetails_Name = findViewById(R.id.productDetails_name);
        productDetails_Rate = findViewById(R.id.productDetails_rate);
        productDetails_Des= findViewById(R.id.productDetails_des);
        cartbtn = findViewById(R.id.cartbtn);
        buybtn = findViewById(R.id.Continue);
        wishlistbtn = findViewById(R.id.add_to_wishlist);
        progressBar = findViewById(R.id.progressBar);
        imageDivider = findViewById(R.id.imageDivider);
        desDivider = findViewById(R.id.desDivider);
        description = findViewById(R.id.description);

        //***************************Visibility**********************************
        productDetails_icon_text.setVisibility(View.INVISIBLE);
        productDetails_icon_ogh_text.setVisibility(View.INVISIBLE);
        productDetails_icon_oge_text.setVisibility(View.INVISIBLE);
        productDetails_Icon.setVisibility(View.INVISIBLE);
        productDetails_Name.setVisibility(View.INVISIBLE);
        productDetails_Rate.setVisibility(View.INVISIBLE);
        productDetails_Des.setVisibility(View.INVISIBLE);
        cartbtn.setVisibility(View.INVISIBLE);
        buybtn.setVisibility(View.INVISIBLE);
        imageDivider.setVisibility(View.INVISIBLE);
        desDivider.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        //*************Getting Data From Previous Activity***************
        productName = getIntent().getStringExtra("productName");
        productCategory = getIntent().getStringExtra("productCategory");

        getSupportActionBar().setTitle(productName);
        phonenumber = NO;

        firebaseFirestore = FirebaseFirestore.getInstance();
        //*******************************Checking item in Wishlist**********************************
        firebaseFirestore.collection("Users").document(phonenumber).collection("MY WISHLIST").document(productName.toUpperCase())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists())
                {
                    ADDED_TO_WISHLIST = true;
                    wishlistbtn.setSupportImageTintList(getResources().getColorStateList(R.color.colourPrimary));
                }
                else
                {
                    ADDED_TO_WISHLIST = false;
                    wishlistbtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //*******************Product Details*****************
        firebaseFirestore.collection(productCategory).document(productName.toUpperCase())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    //***************************Getting Data From Firebase**********************************
                    iconUrl = documentSnapshot.get("icon").toString();

                    name = documentSnapshot.get("name").toString();
                    rate = documentSnapshot.get("rate").toString();
                    des = documentSnapshot.get("des").toString();

                    //***************************Visibility**********************************
                    progressBar.setVisibility(View.INVISIBLE);

                    if(name.equals("Cloth Name Plate"))
                        productDetails_icon_text.setVisibility(View.VISIBLE);

                    else if(name.equals("OG Dress Name Plate"))
                    {
                        productDetails_icon_ogh_text.setVisibility(View.VISIBLE);
                        productDetails_icon_oge_text.setVisibility(View.VISIBLE);
                    }
                    productDetails_Icon.setVisibility(View.VISIBLE);
                    productDetails_Name.setVisibility(View.VISIBLE);
                    productDetails_Rate.setVisibility(View.VISIBLE);
                    productDetails_Des.setVisibility(View.VISIBLE);
                    cartbtn.setVisibility(View.VISIBLE);
                    buybtn.setVisibility(View.VISIBLE);
                    wishlistbtn.setVisibility(View.VISIBLE);
                    imageDivider.setVisibility(View.VISIBLE);
                    desDivider.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);

                    //***************************Loading**********************************
                    Glide.with(ProductDetails.this).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.small_placeholder)).into(productDetails_Icon);
                    productDetails_Name.setText(name);
                    productDetails_Rate.setText(rate);
                    productDetails_Des.setText(des);
                }
                else
                {
                    Toast.makeText(ProductDetails.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.cart)
        {
            Intent intent = new Intent(ProductDetails.this,cart.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}