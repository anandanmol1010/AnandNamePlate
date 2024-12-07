package com.shop.anandnameplate;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import static com.shop.anandnameplate.register.NO;

public class wishlist_Adapter extends RecyclerView.Adapter<wishlist_Adapter.ViewHolder> {

    private List<wishlist_Model> wishlist_ModelList;
    private Activity Activity;

    public wishlist_Adapter(List<wishlist_Model> wishlist_ModelList, android.app.Activity activity) {
        this.wishlist_ModelList = wishlist_ModelList;
        Activity = activity;
    }

    @NonNull
    @Override
    public wishlist_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String icon = wishlist_ModelList.get(position).getIcon();
        String name = wishlist_ModelList.get(position).getName();
        String rate = wishlist_ModelList.get(position).getRate();

        if(name.equals("OG Dress Name Plate"))
        {
            holder.product_icon_ogh_text.setVisibility(View.VISIBLE);
            holder.product_icon_oge_text.setVisibility(View.VISIBLE);
        }
        else if(name.equals("Cloth Name Plate"))
            holder.product_icon_cloth_text.setVisibility(View.VISIBLE);

        holder.productIcon(icon);
        holder.product_Name.setText(name);
        holder.product_Rate.setText(rate);
    }

    @Override
    public int getItemCount() {
        return wishlist_ModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView product_Icon;
        private LinearLayout item_remove;
        private TextView product_Name, product_Rate,product_icon_cloth_text,product_icon_ogh_text,product_icon_oge_text;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            product_Icon = itemView.findViewById(R.id.productIcon);
            product_Name = itemView.findViewById(R.id.productName);
            product_Rate = itemView.findViewById(R.id.productPrice);
            item_remove = itemView.findViewById(R.id.item_remove);
            product_icon_cloth_text = itemView.findViewById(R.id.product_icon_cloth_text);
            product_icon_ogh_text = itemView.findViewById(R.id.product_icon_ogh_text);
            product_icon_oge_text = itemView.findViewById(R.id.product_icon_oge_text);

            product_icon_cloth_text.setVisibility(View.INVISIBLE);
            product_icon_ogh_text.setVisibility(View.INVISIBLE);
            product_icon_oge_text.setVisibility(View.INVISIBLE);

            item_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prClick();
                }
            });
        }

        private void productIcon(String iconUrl) {
            Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.small_placeholder)).into(product_Icon);
        }

        public void remove()
        {
            final progress_bar progress_bar = new progress_bar(Activity);
            progress_bar.startDialog();

            String phonenumber = NO;
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(phonenumber).collection("MY WISHLIST").document(product_Name.getText().toString().toUpperCase())
                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        int position = getAdapterPosition();
                        wishlist_ModelList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(itemView.getContext(), "Removed from Wishlist", Toast.LENGTH_SHORT).show();

                        if(wishlist_ModelList.size() == 0)
                        {
                            WishlistFragment.visi();
                        }
                        progress_bar.dismissDialog();
                    }
                    else
                    {
                        progress_bar.dismissDialog();
                        Toast.makeText(itemView.getContext(), "Can't able to remove from Wishlist", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress_bar.dismissDialog();
                    Toast.makeText(itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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