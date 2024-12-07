package com.shop.anandnameplate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class OrderFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private TextView noorder;
    private Button order_now;
    private ProgressBar progressBar;

    private LinearLayoutManager order_layoutManager;
    private RecyclerView order_RecyclerView;
    private order_Adapter order_Adapter;
    private ArrayList<order_Model> order_ModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_order, container, false);

        // *********************Variable Initialization***********************
        order_RecyclerView = view.findViewById(R.id.order_RecyclerView);
        noorder = view.findViewById(R.id.noorder);
        order_now = view.findViewById(R.id.order_now);
        progressBar = view.findViewById(R.id.progressBar);

        //***************************Visibility**********************************
        order_RecyclerView.setVisibility(View.INVISIBLE);
        noorder.setVisibility(View.INVISIBLE);
        order_now.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String phonenumber = NO;

        //*********************************Getting Wishlist Items********************************
        order_ModelList = new ArrayList<order_Model>();

        order_Adapter = new order_Adapter(order_ModelList);
        order_RecyclerView.setAdapter(order_Adapter);

        order_layoutManager = new LinearLayoutManager(getActivity());
        order_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        order_RecyclerView.setLayoutManager(order_layoutManager);
        order_RecyclerView.setHasFixedSize(true);
        order_RecyclerView.setItemViewCacheSize(20);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(phonenumber).collection("MY ORDER").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(task.getResult().isEmpty())
                        {
                            noorder.setVisibility(View.VISIBLE);
                            order_now.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                if(documentSnapshot.get("name").toString().contains("Name Plate"))
                                {
                                    if(documentSnapshot.get("name").toString().equals("Cloth Name Plate"))
                                    {
                                        order_ModelList.add(new order_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),
                                                documentSnapshot.get("qty").toString(),documentSnapshot.get("rate").toString(),documentSnapshot.get("englishName").toString(),
                                                "",documentSnapshot.get("extraCharge").toString(),documentSnapshot.get("orderId").toString(),
                                                documentSnapshot.get("orderedOn").toString(),documentSnapshot.get("deliveredOn").toString()));
                                    }
                                    else if(documentSnapshot.get("name").toString().equals("OG Dress Name Plate"))
                                    {
                                        order_ModelList.add(new order_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),
                                                documentSnapshot.get("qty").toString(),documentSnapshot.get("rate").toString(),documentSnapshot.get("englishName").toString(),
                                                documentSnapshot.get("hindiName").toString(),documentSnapshot.get("extraCharge").toString(),documentSnapshot.get("orderId").toString(),
                                                documentSnapshot.get("orderedOn").toString(),documentSnapshot.get("deliveredOn").toString()));
                                    }
                                }
                                else
                                {
                                    order_ModelList.add(new order_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),
                                            documentSnapshot.get("qty").toString(),documentSnapshot.get("rate").toString(),"","",
                                            documentSnapshot.get("extraCharge").toString(),documentSnapshot.get("orderId").toString(), documentSnapshot.get("orderedOn").toString(),
                                            documentSnapshot.get("deliveredOn").toString()));
                                }
                            }
                            order_RecyclerView.setVisibility(View.VISIBLE);

                            order_Adapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),main.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}