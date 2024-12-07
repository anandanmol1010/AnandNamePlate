package com.shop.anandnameplate;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class product_Adapter extends RecyclerView.Adapter<product_Adapter.ViewHolder>{

    private List<product_Model> product_ModelList;

    public product_Adapter(List<product_Model> product_ModelList) {
        this.product_ModelList = product_ModelList;
    }

    @NonNull
    @Override
    public product_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String icon = product_ModelList.get(position).getProduct_IconLink();
        String name = product_ModelList.get(position).getProduct_Name();
        String rate = product_ModelList.get(position).getProduct_Rate();

        if(name.equals("OG Dress Name Plate"))
        {
            holder.product_icon_ogh_text.setVisibility(View.VISIBLE);
            holder.product_icon_oge_text.setVisibility(View.VISIBLE);
        }
        else if(name.equals("Cloth Name Plate"))
            holder.product_icon_cloth_text.setVisibility(View.VISIBLE);

        holder.setnamePlate_Name(name);
        holder.setnamePlate_Icon(icon);
        holder.setnamePlate_Rate(rate);
    }

    @Override
    public int getItemCount() {
        return product_ModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView product_Icon;
        private TextView product_Name;
        private TextView product_Rate;
        private TextView product_icon_ogh_text;
        private TextView product_icon_oge_text;
        private TextView product_icon_cloth_text;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            product_Icon = itemView.findViewById(R.id.product_icon);
            product_Name = itemView.findViewById(R.id.product_name);
            product_Rate = itemView.findViewById(R.id.product_rate);
            product_icon_ogh_text = itemView.findViewById(R.id.product_icon_ogh_text);
            product_icon_oge_text = itemView.findViewById(R.id.product_icon_oge_text);
            product_icon_cloth_text = itemView.findViewById(R.id.product_icon_cloth_text);

            product_icon_ogh_text.setVisibility(View.INVISIBLE);
            product_icon_oge_text.setVisibility(View.INVISIBLE);
            product_icon_cloth_text.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(),ProductDetails.class);
                    intent.putExtra("productName", product_Name.getText().toString());

                    if(product_Name.getText().toString().contains("Name Plate"))
                        intent.putExtra("productCategory","Name Plate");

                    else if(product_Name.getText().toString().contains("Ribbon"))
                        intent.putExtra("productCategory","Ribbon");

                    else if(product_Name.getText().toString().contains("Medal"))
                        intent.putExtra("productCategory","Medal");

                    itemView.getContext().startActivity(intent);
                }
            });
        }

        private  void setnamePlate_Icon(String iconUrl)
        {
            Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.small_placeholder)).into(product_Icon);
        }

        private void setnamePlate_Name(String name)
        {
            product_Name.setText(name);
        }

        private void setnamePlate_Rate(String rate)
        {
            product_Rate.setText(rate);
        }
    }
}
