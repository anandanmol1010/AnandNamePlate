package com.shop.anandnameplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.shop.anandnameplate.register.NO;

public class BottomSheet extends BottomSheetDialogFragment {
    private Button name_Continue;
    private TextInputEditText hindi_Name,english_Name;
    private String name,activity,iconUrl,rate;
    private FirebaseFirestore firebaseFirestore;

    public BottomSheet(String name, String activity, String iconUrl, String rate) {
        this.name = name;
        this.activity = activity;
        this.iconUrl = iconUrl;
        this.rate = rate;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog,container,false);

        name_Continue = view.findViewById(R.id.name_Continue);
        english_Name = view.findViewById(R.id.english_Name);
        hindi_Name = view.findViewById(R.id.hindi_Name);

        if(name.equals("Cloth Name Plate"))
        {
            hindi_Name.setVisibility(View.GONE);
        }

        name_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                String phonenumber = NO;
                firebaseFirestore = FirebaseFirestore.getInstance();
                Map<String,Object> map = new HashMap<>();
                map.put("icon",iconUrl);
                map.put("name",name);
                map.put("qty","1");
                map.put("rate",rate);

                if (name.equals("Cloth Name Plate"))
                {
                    if(!english_Name.getText().toString().trim().isEmpty())
                    {
                        if (activity.equals("buy"))
                        {
                            Intent intent = new Intent(getContext(), Delivery.class);
                            intent.putExtra("englishName", english_Name.getText().toString());
                            intent.putExtra("product", "Cloth Name Plate");
                            startActivity(intent);
                        }
                        else if(activity.equals("cart"))
                        {
                            final progress_bar progress_bar = new progress_bar(getActivity());
                            progress_bar.startDialog();
                            map.put("englishName",english_Name.getText().toString());

                            firebaseFirestore.collection("Users").document(phonenumber).collection("MY CART").document(name.toUpperCase()).set(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progress_bar.dismissDialog();
                                            Toast.makeText(view.getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progress_bar.dismissDialog();
                                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            dismiss();
                        }
                    }
                    else
                    {
                        Toast.makeText(view.getContext(), "Enter a Name", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (name.equals("OG Dress Name Plate"))
                {
                    if(!english_Name.getText().toString().trim().isEmpty())
                    {
                        if(!hindi_Name.getText().toString().trim().isEmpty())
                        {
                            if(activity.equals("buy"))
                            {
                                Intent intent = new Intent(getContext(), Delivery.class);
                                intent.putExtra("englishName", english_Name.getText().toString());
                                intent.putExtra("hindiName", hindi_Name.getText().toString());
                                intent.putExtra("product", "OG Dress Name Plate");
                                startActivity(intent);
                            }
                            else if(activity.equals("cart"))
                            {
                                final progress_bar progress_bar = new progress_bar(getActivity());
                                progress_bar.startDialog();
                                map.put("englishName",english_Name.getText().toString());
                                map.put("hindiName",hindi_Name.getText().toString());
                                firebaseFirestore.collection("Users").document(phonenumber).collection("MY CART").document(name.toUpperCase()).set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progress_bar.dismissDialog();
                                                Toast.makeText(view.getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        progress_bar.dismissDialog();
                                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dismiss();
                            }
                        }
                        else
                        {
                            Toast.makeText(view.getContext(), "Enter a Name", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(view.getContext(), "Enter a Name", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }
}
