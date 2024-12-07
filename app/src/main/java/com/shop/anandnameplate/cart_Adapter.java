package com.shop.anandnameplate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shop.anandnameplate.register.NO;

public class cart_Adapter extends RecyclerView.Adapter<cart_Adapter.ViewHolder>{

    private List<cart_Model> cart_ModelList;
    private String activity;
    private Activity Activity;

    public cart_Adapter(List<cart_Model> cart_ModelList, String activity, android.app.Activity activity1) {
        this.cart_ModelList = cart_ModelList;
        this.activity = activity;
        Activity = activity1;
    }

    @NonNull
    @Override
    public cart_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String icon = cart_ModelList.get(position).getIcon();
        String name = cart_ModelList.get(position).getName();
        String qty = cart_ModelList.get(position).getQty();
        String rate = cart_ModelList.get(position).getRate();
        String englishName = cart_ModelList.get(position).getEnglishName();
        String hindiName = cart_ModelList.get(position).getHindiName();

            if (name.equals("OG Dress Name Plate"))
            {
                holder.ogEnglish.setText(englishName);
                holder.ogHindi.setText(hindiName);
            }
            else if (name.equals("Cloth Name Plate"))
            {
                holder.clothEnglish.setText(englishName);
            }
            holder.productIcon(icon);
            holder.product_Name.setText(name);
            holder.product_Qty.setText(qty);
            holder.product_Rate.setText(rate);

            if(!holder.product_Qty.getText().toString().equals("1"))
            {
                holder.minus.setBackgroundResource(R.drawable.circle_border);
                holder.minus.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            }
    }

    @Override
    public int getItemCount() {
        return cart_ModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView product_Icon;
        private LinearLayout item_remove;
        private Button plus,minus;
        private TextView product_Name,product_Qty,product_Rate,ogEnglish,ogHindi,clothEnglish;
        private long price;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            product_Icon = itemView.findViewById(R.id.productIcon);
            product_Name = itemView.findViewById(R.id.productName);
            product_Qty = itemView.findViewById(R.id.productQty);
            product_Rate = itemView.findViewById(R.id.productPrice);
            ogEnglish = itemView.findViewById(R.id.ogEnglish);
            ogHindi = itemView.findViewById(R.id.ogHindi);
            clothEnglish = itemView.findViewById(R.id.cloth_english);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            item_remove = itemView.findViewById(R.id.item_remove);

            item_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove();
                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    plusbtn();
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    minusbtn();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prClick();
                }
            });
        }

        private  void productIcon(String iconUrl)
        {
            Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.small_placeholder)).into(product_Icon);
        }

        public void remove()
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("Do you want to delete product from Cart.");
                    builder.setTitle("Delete Product");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final progress_bar progress_bar = new progress_bar(Activity);
                            progress_bar.startDialog();

                            String phonenumber = NO;
                            final String rate = product_Rate.getText().toString();
                            final String QTY = product_Qty.getText().toString();

                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            firebaseFirestore.collection("Users").document(phonenumber).collection("MY CART").document(product_Name.getText().toString().toUpperCase())
                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        //************************Removing Product************************
                                        int position = getAdapterPosition();
                                        cart_ModelList.remove(position);
                                        notifyItemRemoved(position);

                                        //************************Calculating Amount************************
                                        if(activity.equals("Cart"))
                                        {
                                            cart.calAmt(rate,QTY,itemView.getContext());
                                        }
                                        else if(activity.equals("CartFragment"))
                                        {
                                            CartFragment.calAmt(rate,QTY,itemView.getContext());
                                        }

                                        Toast.makeText(itemView.getContext(), "Removed from Cart", Toast.LENGTH_SHORT).show();

                                        if(cart_ModelList.size() == 0)
                                        {
                                            if(activity.equals("Cart"))
                                            {
                                                cart.visi();
                                            }
                                            else if(activity.equals("CartFragment"))
                                            {
                                                CartFragment.visi();
                                            }
                                        }
                                        progress_bar.dismissDialog();
                                    }
                                    else
                                    {
                                        Toast.makeText(itemView.getContext(), "Can't able to remove from Cart", Toast.LENGTH_SHORT).show();
                                        progress_bar.dismissDialog();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progress_bar.dismissDialog();
                                }
                            });
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String a= "";
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
        }

        public void plusbtn()
        {
            final progress_bar progress_bar = new progress_bar(Activity);
            progress_bar.startDialog();

            String strprice = product_Rate.getText().toString();
            long intprice = Integer.parseInt(strprice.substring(strprice.indexOf(' ')+1,strprice.indexOf('/')));
            price = (intprice * (Integer.parseInt(product_Qty.getText().toString())+1))/Integer.parseInt(product_Qty.getText().toString());

            Map<String,Object> map = new HashMap<>();
            map.put("qty",String.valueOf(Integer.parseInt(product_Qty.getText().toString())+1));
            map.put("rate","Rs. "+String.valueOf(price)+"/-");

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(NO).collection("MY CART").document(product_Name.getText().toString().toUpperCase())
                    .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //***********************Setting Background**************************
                    minus.setBackgroundResource(R.drawable.circle_border);
                    minus.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));

                    if(task.isSuccessful())
                    {
                        //***********************Calculating Amount and Quantity***********************
                        if(activity.equals("CartFragment"))
                        {
                            CartFragment.qty(product_Rate.getText().toString(),product_Qty.getText().toString(),itemView.getContext(),"Plus");
                        }
                        else if(activity.equals("Cart"))
                        {
                            cart.qty(product_Rate.getText().toString(),product_Qty.getText().toString(),itemView.getContext(),"Plus");
                        }

                        product_Rate.setText("Rs. "+String.valueOf(price)+"/-");
                        product_Qty.setText(String .valueOf(Integer.parseInt(product_Qty.getText().toString())+1));

                        progress_bar.dismissDialog();
                    }
                    else
                    {
                        progress_bar.dismissDialog();
                        Toast.makeText(itemView.getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void minusbtn()
        {
            if(Integer.parseInt(product_Qty.getText().toString())>2)
            {
                final progress_bar progress_bar = new progress_bar(Activity);
                progress_bar.startDialog();

                String strprice = product_Rate.getText().toString();
                long intprice = Integer.parseInt(strprice.substring(strprice.indexOf(' ')+1,strprice.indexOf('/')));
                price = (intprice * (Integer.parseInt(product_Qty.getText().toString())-1))/Integer.parseInt(product_Qty.getText().toString());

                Map<String,Object> map = new HashMap<>();
                map.put("qty",String.valueOf(Integer.parseInt(product_Qty.getText().toString())-1));
                map.put("rate","Rs. "+String.valueOf(price)+"/-");

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Users").document(NO).collection("MY CART").document(product_Name.getText().toString().toUpperCase())
                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            //***********************Calculating Amount and Quantity***********************
                            if(activity.equals("CartFragment"))
                            {
                                CartFragment.qty(product_Rate.getText().toString(),product_Qty.getText().toString(),itemView.getContext(),"Minus");
                            }
                            else if(activity.equals("Cart"))
                            {
                                cart.qty(product_Rate.getText().toString(),product_Qty.getText().toString(),itemView.getContext(),"Minus");
                            }

                            product_Rate.setText("Rs. "+String.valueOf(price)+"/-");
                            product_Qty.setText(String .valueOf(Integer.parseInt(product_Qty.getText().toString())-1));

                            progress_bar.dismissDialog();
                        }
                        else
                        {
                            progress_bar.dismissDialog();
                            Toast.makeText(itemView.getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }



            if(product_Qty.getText().equals("2"))
            {
                final progress_bar progress_bar = new progress_bar(Activity);
                progress_bar.startDialog();

                String strprice = product_Rate.getText().toString();
                long intprice = Integer.parseInt(strprice.substring(strprice.indexOf(' ')+1,strprice.indexOf('/')));
                price = (intprice * (Integer.parseInt(product_Qty.getText().toString())-1))/Integer.parseInt(product_Qty.getText().toString());

                Map<String,Object> map = new HashMap<>();
                map.put("qty",String.valueOf(Integer.parseInt(product_Qty.getText().toString())-1));
                map.put("rate","Rs. "+String.valueOf(price)+"/-");

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Users").document(NO).collection("MY CART").document(product_Name.getText().toString().toUpperCase())
                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //***********************Setting Background**************************
                        minus.setBackgroundResource(R.drawable.faded_circle_border);
                        minus.setTextColor(ColorStateList.valueOf(Color.parseColor("#23000000")));

                        if(task.isSuccessful())
                        {
                            //***********************Calculating Amount and Quantity***********************
                            if(activity.equals("CartFragment"))
                            {
                                CartFragment.qty(product_Rate.getText().toString(),product_Qty.getText().toString(),itemView.getContext(),"Minus");
                            }
                            else if(activity.equals("Cart"))
                            {
                                cart.qty(product_Rate.getText().toString(),product_Qty.getText().toString(),itemView.getContext(),"Minus");
                            }

                            product_Rate.setText("Rs. "+String.valueOf(price)+"/-");
                            product_Qty.setText(String .valueOf(Integer.parseInt(product_Qty.getText().toString())-1));

                            progress_bar.dismissDialog();
                        }
                        else
                        {
                            progress_bar.dismissDialog();
                            Toast.makeText(itemView.getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        public void prClick()
        {
            String prCat = "";
            if(product_Name.getText().toString().contains("Name Plate"))
            {
                prCat = "Name Plate";
            }
            else if(product_Name.getText().toString().contains("Ribbon"))
            {
                prCat = "Ribbon";
            }

            else if(product_Name.getText().toString().contains("Medal"))
            {
                prCat = "Medal";
            }

            Intent intent = new Intent(itemView.getContext(),ProductDetails.class);
            intent.putExtra("productName",product_Name.getText().toString());
            intent.putExtra("productCategory",prCat);
            itemView.getContext().startActivity(intent);
        }
    }
  }