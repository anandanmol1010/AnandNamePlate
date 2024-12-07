package com.shop.anandnameplate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

public class WishlistFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private static TextView nowishlist;
    private static Button addnow;
    private ProgressBar progressBar;

    private LinearLayoutManager wishlist_layoutManager;
    private RecyclerView wishlist_RecyclerView;
    private wishlist_Adapter wishlist_Adapter;
    private ArrayList<wishlist_Model> wishlist_ModelList;


   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

       // *********************Variable Initialization***********************
       wishlist_RecyclerView = view.findViewById(R.id.wishlist_RecyclerView);
       nowishlist = view.findViewById(R.id.nowishlist);
       addnow = view.findViewById(R.id.addnow);
       progressBar = view.findViewById(R.id.progressBar);

       //***************************Visibility**********************************
       wishlist_RecyclerView.setVisibility(View.INVISIBLE);
       nowishlist.setVisibility(View.INVISIBLE);
       addnow.setVisibility(View.INVISIBLE);
       progressBar.setVisibility(View.VISIBLE);

       String phonenumber = NO;

       //*********************************Getting Wishlist Items********************************
       wishlist_ModelList = new ArrayList<wishlist_Model>();

       wishlist_Adapter = new wishlist_Adapter(wishlist_ModelList,getActivity());
       wishlist_RecyclerView.setAdapter(wishlist_Adapter);

       wishlist_layoutManager = new LinearLayoutManager(getActivity());
       wishlist_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       wishlist_RecyclerView.setLayoutManager(wishlist_layoutManager);
       wishlist_RecyclerView.setHasFixedSize(true);
       wishlist_RecyclerView.setItemViewCacheSize(20);

       firebaseFirestore = FirebaseFirestore.getInstance();
       firebaseFirestore.collection("Users").document(phonenumber).collection("MY WISHLIST").get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       progressBar.setVisibility(View.INVISIBLE);
                       if(task.getResult().isEmpty())
                       {
                           nowishlist.setVisibility(View.VISIBLE);
                           addnow.setVisibility(View.VISIBLE);
                       }
                       else
                       {
                           for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                           {
                               wishlist_ModelList.add(new wishlist_Model(documentSnapshot.get("icon").toString(),documentSnapshot.get("name").toString()
                                       ,documentSnapshot.get("rate").toString()));
                           }
                           wishlist_RecyclerView.setVisibility(View.VISIBLE);

                           wishlist_Adapter.notifyDataSetChanged();
                       }
                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });

       addnow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(view.getContext(),main.class);
               getActivity().startActivity(intent);
           }
       });

       return view;
    }

    public static void visi()
    {
        nowishlist.setVisibility(View.VISIBLE);
        addnow.setVisibility(View.VISIBLE);
    }
}