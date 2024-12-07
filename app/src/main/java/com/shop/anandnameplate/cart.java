package com.shop.anandnameplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.shop.anandnameplate.register.NO;

public class cart extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private static Button shopNow;
    private static Button Continue;
    private static TextView emptyCart;
    private static TextView add_item;
    private static LinearLayout linearLayout;
    private static TextView totalAmount;
    private long amount;
    private String strprice,phonenumber;
    private int intprice;

    private LinearLayoutManager cart_layoutManager;
    private RecyclerView cart_RecyclerView;
    private cart_Adapter cart_Adapter;
    private ArrayList<cart_Model> cart_ModelList;
    private ArrayList<String> details,iconUrl,name,qty,rate,englishName,hindiName;

    public void shopNowbtn(View view)
    {
        Intent intent =new Intent(cart.this,main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void Continuebtn(View view)
    {
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(phonenumber).collection("MY CART").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty())
                        {
                            Toast.makeText(cart.this, "Cart is Empty", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            details = new ArrayList<String>();
                            iconUrl = new ArrayList<String>();
                            name = new ArrayList<String>();
                            qty = new ArrayList<String>();
                            rate = new ArrayList<String>();
                            englishName = new ArrayList<String>();
                            hindiName = new ArrayList<String>();

                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                if(documentSnapshot.get("name").toString().contains("Name Plate"))
                                {
                                    if(documentSnapshot.get("name").toString().equals("Cloth Name Plate"))
                                    {
                                        englishName.add(englishName.size(),documentSnapshot.get("englishName").toString());
                                        hindiName.add(hindiName.size()," ");
                                    }
                                    else if(documentSnapshot.get("name").toString().equals("OG Dress Name Plate"))
                                    {
                                        englishName.add(englishName.size(),documentSnapshot.get("englishName").toString());
                                        hindiName.add(hindiName.size(),documentSnapshot.get("hindiName").toString());
                                    }
                                }
                                else
                                {
                                    englishName.add(englishName.size()," ");
                                    hindiName.add(hindiName.size()," ");
                                }
                                iconUrl.add(iconUrl.size(),documentSnapshot.get("icon").toString());
                                name.add(name.size(),documentSnapshot.get("name").toString());
                                qty.add(qty.size(),documentSnapshot.get("qty").toString());
                                rate.add(rate.size(),documentSnapshot.get("rate").toString());
                            }
                            String Icon = "", Name = "", Qty = "", Rate = "", EnglishName = "", HindiName = "";
                            for(int a=0;a<iconUrl.size();a++)
                            {
                                Icon = Icon + iconUrl.get(a)+"==";
                                Name = Name + name.get(a)+"==";
                                Qty = Qty + qty.get(a)+"==";
                                Rate = Rate + rate.get(a)+"==";
                                EnglishName = EnglishName + englishName.get(a)+"==";
                                HindiName = HindiName + hindiName.get(a)+"==";
                            }
                            details.add(0,Icon);
                            details.add(1,Name);
                            details.add(2,Qty);
                            details.add(3,Rate);
                            details.add(4,EnglishName);
                            details.add(5,HindiName);

                            int intPrice;
                            String strPrice;

                            strPrice = totalAmount.getText().toString();
                            intPrice = Integer.parseInt(strPrice.substring(strPrice.indexOf(' ')+1,strPrice.indexOf('/')));
                            details.add(6,String.valueOf(intPrice));

                            Intent intent = new Intent(cart.this,payment_methods.class);
                            intent.putExtra("activity","Cart");
                            intent.putStringArrayListExtra("details",details);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(cart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //************ToolBar******************
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // *********************Variable Initialization***********************
        emptyCart = findViewById(R.id.emptyCart);
        shopNow = findViewById(R.id.shopNow);
        progressBar = findViewById(R.id.progressBar);
        add_item = findViewById(R.id.add_item);
        cart_RecyclerView = findViewById(R.id.wishlist_RecyclerView);
        linearLayout = findViewById(R.id.linearLayout);
        Continue = findViewById(R.id.Continue);
        totalAmount = findViewById(R.id.totalAmount);

        //***************************Visibility**********************************
        emptyCart.setVisibility(View.INVISIBLE);
        shopNow.setVisibility(View.INVISIBLE);
        add_item.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        Continue.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        phonenumber = NO;

        //*********************************Getting Cart Items********************************
        cart_ModelList = new ArrayList<cart_Model>();

        cart_Adapter = new cart_Adapter(cart_ModelList,"Cart",cart.this);
        cart_RecyclerView.setAdapter(cart_Adapter);

        cart_layoutManager = new LinearLayoutManager(cart.this);
        cart_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cart_RecyclerView.setLayoutManager(cart_layoutManager);
        cart_RecyclerView.setHasFixedSize(true);
        cart_RecyclerView.setItemViewCacheSize(20);
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(phonenumber).collection("MY CART").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(task.getResult().isEmpty())
                        {
                            emptyCart.setVisibility(View.VISIBLE);
                            shopNow.setVisibility(View.VISIBLE);
                            add_item.setVisibility(View.VISIBLE);
                        }

                        else
                        {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                strprice = documentSnapshot.get("rate").toString();
                                intprice = Integer.parseInt(strprice.substring(strprice.indexOf(' ')+1,strprice.indexOf('/')));
                                amount = amount + intprice;

                                if(documentSnapshot.get("name").toString().contains("Name Plate"))
                                {
                                    if(documentSnapshot.get("name").toString().equals("Cloth Name Plate"))
                                    {
                                        cart_ModelList.add(new cart_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),
                                                documentSnapshot.get("qty").toString(),documentSnapshot.get("rate").toString(),
                                                documentSnapshot.get("englishName").toString(),""));
                                    }
                                    else if(documentSnapshot.get("name").toString().equals("OG Dress Name Plate"))
                                    {
                                        cart_ModelList.add(new cart_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),
                                                documentSnapshot.get("qty").toString(),documentSnapshot.get("rate").toString(),
                                                documentSnapshot.get("englishName").toString(),documentSnapshot.get("hindiName").toString()));
                                    }
                                }
                                else
                                {
                                    cart_ModelList.add(new cart_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),
                                            documentSnapshot.get("qty").toString(),documentSnapshot.get("rate").toString(),"",""));
                                }
                                //********************Visibility**********************
                                cart_RecyclerView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                linearLayout.setVisibility(View.VISIBLE);
                                Continue.setVisibility(View.VISIBLE);

                            }
                            totalAmount.setText("Rs. "+String.valueOf(amount)+"/-");
                            cart_Adapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(cart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void visi()
    {
        Continue.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        emptyCart.setVisibility(View.VISIBLE);
        add_item.setVisibility(View.VISIBLE);
        shopNow.setVisibility(View.VISIBLE);
    }

    public static void calAmt(String rate,String QTY,Context context)
    {
        //************************Calculating Amount************************
        String strprice;
        long intprice;
        long amount = 0;
        strprice = totalAmount.getText().toString();
        intprice = Integer.parseInt(strprice.substring(strprice.indexOf(' ')+1,strprice.indexOf('/')));
        amount = amount + intprice;

        strprice = rate;
        intprice = Integer.parseInt(strprice.substring(strprice.indexOf(' ')+1,strprice.indexOf('/')));
        amount = amount - intprice;

        totalAmount.setText("Rs. "+String.valueOf(amount)+"/-");
    }

    public static void qty(String rate,String QTY,Context context,String todo)
    {
        //************************Calculating Amount************************
        String strprice;
        long intprice;
        long amount = 0;
        strprice = totalAmount.getText().toString();
        intprice = Integer.parseInt(strprice.substring(strprice.indexOf(' ')+1,strprice.indexOf('/')));
        amount = amount + intprice;

        strprice = rate;
        intprice = Integer.parseInt(strprice.substring(strprice.indexOf(' ')+1,strprice.indexOf('/')))/Integer.parseInt(QTY);
        if(todo.equals("Plus"))
        {
            amount = amount + intprice;
        }
        else if(todo.equals("Minus"))
        {
            amount = amount - intprice;
        }

        totalAmount.setText("Rs. "+String.valueOf(amount)+"/-");
    }
}