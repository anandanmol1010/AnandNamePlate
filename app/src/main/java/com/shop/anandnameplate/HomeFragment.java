package com.shop.anandnameplate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView namePlate_RecyclerView,ribbon_RecyclerView,medal_RecyclerView;

    private SwipeRefreshLayout swipeLayout;

    private TextView name,rib,med;
    private ProgressBar progressBar;
    private Button ribbon_viewAll,medal_viewAll;

    private product_Adapter namePlate_Adapter;
    private product_Adapter ribbon_Adapter;
    private product_Adapter medal_Adapter;

    private List<product_Model> namePlate_ModelList;
    private List<product_Model> ribbon_ModelList;
    private List<product_Model> medal_ModelList;

    private FirebaseFirestore namePlate_firebaseFirestore,ribbon_firebaseFirestore,medal_firebaseFirestore;

    LinearLayoutManager namePlate_layoutManager,ribbon_layoutManager,medal_layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        // *********************************Variable Initialization******************************
        swipeLayout = view.findViewById(R.id.swipeLayout);
        namePlate_RecyclerView = view.findViewById(R.id.namePlate_recyclerview);
        ribbon_RecyclerView = view.findViewById(R.id.ribbon_recyclerview);
        medal_RecyclerView = view.findViewById(R.id.medal_recyclerview);
        name = view.findViewById(R.id.Name_Plates);
        rib = view.findViewById(R.id.Ribbons);
        med = view.findViewById(R.id.Medals);
        progressBar = view.findViewById(R.id.progressBar);
        ribbon_viewAll = view.findViewById(R.id.ribbon_viewAll);
        medal_viewAll = view.findViewById(R.id.medal_viewAll);

        //********************Visibility**********************
        name.setVisibility(View.INVISIBLE);
        namePlate_RecyclerView.setVisibility(View.INVISIBLE);
        rib.setVisibility(View.INVISIBLE);
        ribbon_RecyclerView.setVisibility(View.INVISIBLE);
        med.setVisibility(View.INVISIBLE);
        medal_RecyclerView.setVisibility(View.INVISIBLE);
        ribbon_viewAll.setVisibility(View.INVISIBLE);
        medal_viewAll.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // ***********************NAME PLATE***********************
        namePlate_ModelList = new ArrayList<product_Model>();

        namePlate_Adapter = new product_Adapter(namePlate_ModelList);
        namePlate_RecyclerView.setAdapter(namePlate_Adapter);

        namePlate_layoutManager = new LinearLayoutManager(getActivity());
        namePlate_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        namePlate_RecyclerView.setLayoutManager(namePlate_layoutManager);
        namePlate_RecyclerView.setHasFixedSize(true);
        namePlate_RecyclerView.setItemViewCacheSize(20);


        namePlate_firebaseFirestore = FirebaseFirestore.getInstance();
        namePlate_firebaseFirestore.collection("Name Plate").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                namePlate_ModelList.add(new product_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),documentSnapshot.get("rate").toString()));

                                //********************Visibility**********************
                                name.setVisibility(View.VISIBLE);
                                namePlate_RecyclerView.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            namePlate_Adapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // ***********************RIBBON***********************
        ribbon_ModelList = new ArrayList<product_Model>();

        ribbon_Adapter = new product_Adapter(ribbon_ModelList);
        ribbon_RecyclerView.setAdapter(ribbon_Adapter);

        ribbon_layoutManager = new LinearLayoutManager(getActivity());
        ribbon_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ribbon_RecyclerView.setLayoutManager(ribbon_layoutManager);
        ribbon_RecyclerView.setHasFixedSize(true);
        ribbon_RecyclerView.setItemViewCacheSize(20);

        ribbon_firebaseFirestore = FirebaseFirestore.getInstance();
        ribbon_firebaseFirestore.collection("Ribbon").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                ribbon_ModelList.add(new product_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),documentSnapshot.get("rate").toString()));

                                //********************Visibility**********************
                                rib.setVisibility(View.VISIBLE);
                                ribbon_RecyclerView.setVisibility(View.VISIBLE);
                                ribbon_viewAll.setVisibility(View.VISIBLE);
                            }
                            ribbon_Adapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // ***********************MEDAL***********************
        medal_ModelList = new ArrayList<product_Model>();

        medal_Adapter = new product_Adapter(medal_ModelList);
        medal_RecyclerView.setAdapter(medal_Adapter);

        medal_layoutManager = new LinearLayoutManager(getActivity());
        medal_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        medal_RecyclerView.setLayoutManager(medal_layoutManager);
        medal_RecyclerView.setHasFixedSize(true);
        medal_RecyclerView.setItemViewCacheSize(20);

        medal_firebaseFirestore = FirebaseFirestore.getInstance();
        medal_firebaseFirestore.collection("Medal").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                medal_ModelList.add(new product_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),documentSnapshot.get("rate").toString()));

                                //********************Visibility**********************
                                med.setVisibility(View.VISIBLE);
                                medal_RecyclerView.setVisibility(View.VISIBLE);
                                medal_viewAll.setVisibility(View.VISIBLE);
                            }
                            medal_Adapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);

                name.setVisibility(View.INVISIBLE);
                namePlate_RecyclerView.setVisibility(View.INVISIBLE);
                rib.setVisibility(View.INVISIBLE);
                ribbon_RecyclerView.setVisibility(View.INVISIBLE);
                med.setVisibility(View.INVISIBLE);
                medal_RecyclerView.setVisibility(View.INVISIBLE);
                ribbon_viewAll.setVisibility(View.INVISIBLE);
                medal_viewAll.setVisibility(View.INVISIBLE);

                namePlate_ModelList.clear();
                ribbon_ModelList.clear();
                medal_ModelList.clear();

                // ***********************NAME PLATE***********************
                namePlate_ModelList = new ArrayList<product_Model>();

                namePlate_Adapter = new product_Adapter(namePlate_ModelList);
                namePlate_RecyclerView.setAdapter(namePlate_Adapter);

                namePlate_layoutManager = new LinearLayoutManager(getActivity());
                namePlate_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                namePlate_RecyclerView.setLayoutManager(namePlate_layoutManager);
                namePlate_RecyclerView.setHasFixedSize(true);
                namePlate_RecyclerView.setItemViewCacheSize(20);

                namePlate_firebaseFirestore = FirebaseFirestore.getInstance();
                namePlate_firebaseFirestore.collection("Name Plate").orderBy("index").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                                    {
                                        namePlate_ModelList.add(new product_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),documentSnapshot.get("rate").toString()));
                                        name.setVisibility(View.VISIBLE);
                                        namePlate_RecyclerView.setVisibility(View.VISIBLE);
                                    }
                                    namePlate_Adapter.notifyDataSetChanged();
                                }
                                else
                                    Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // ***********************RIBBON***********************
                ribbon_ModelList = new ArrayList<product_Model>();

                ribbon_Adapter = new product_Adapter(ribbon_ModelList);
                ribbon_RecyclerView.setAdapter(ribbon_Adapter);

                ribbon_layoutManager = new LinearLayoutManager(getActivity());
                ribbon_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                ribbon_RecyclerView.setLayoutManager(ribbon_layoutManager);
                ribbon_RecyclerView.setHasFixedSize(true);
                ribbon_RecyclerView.setItemViewCacheSize(20);

                ribbon_firebaseFirestore = FirebaseFirestore.getInstance();
                ribbon_firebaseFirestore.collection("Ribbon").orderBy("index").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                                    {
                                        ribbon_ModelList.add(new product_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),documentSnapshot.get("rate").toString()));
                                        rib.setVisibility(View.VISIBLE);
                                        ribbon_RecyclerView.setVisibility(View.VISIBLE);
                                        ribbon_viewAll.setVisibility(View.VISIBLE);
                                    }
                                    ribbon_Adapter.notifyDataSetChanged();
                                }
                                else
                                    Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // ***********************MEDAL***********************
                medal_ModelList = new ArrayList<product_Model>();

                medal_Adapter = new product_Adapter(medal_ModelList);
                medal_RecyclerView.setAdapter(medal_Adapter);

                medal_layoutManager = new LinearLayoutManager(getActivity());
                medal_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                medal_RecyclerView.setLayoutManager(medal_layoutManager);
                medal_RecyclerView.setHasFixedSize(true);
                medal_RecyclerView.setItemViewCacheSize(20);
                
                medal_firebaseFirestore = FirebaseFirestore.getInstance();
                medal_firebaseFirestore.collection("Medal").orderBy("index").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                                    {
                                        medal_ModelList.add(new product_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString(),documentSnapshot.get("rate").toString()));
                                        med.setVisibility(View.VISIBLE);
                                        medal_RecyclerView.setVisibility(View.VISIBLE);
                                        medal_viewAll.setVisibility(View.VISIBLE);
                                    }
                                    medal_Adapter.notifyDataSetChanged();
                                }
                                else
                                    Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                swipeLayout.setRefreshing(false);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ribbon_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),viewAll.class);
                intent.putExtra("Category","Ribbon");
                startActivity(intent);
            }
        });

        medal_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),viewAll.class);
                intent.putExtra("Category","Medal");
                startActivity(intent);
            }
        });
        return view;
    }
}