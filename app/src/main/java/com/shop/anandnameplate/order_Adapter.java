package com.shop.anandnameplate;

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

public class order_Adapter extends RecyclerView.Adapter<order_Adapter.ViewHolder>{

    private List<order_Model> order_ModelList;

    public order_Adapter(List<order_Model> order_ModelList) {
        this.order_ModelList = order_ModelList;
    }

    @NonNull
    @Override
    public order_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String icon = order_ModelList.get(position).getIcon();
        String name = order_ModelList.get(position).getName();
        String qty = order_ModelList.get(position).getQty();
        String rate = order_ModelList.get(position).getRate();
        String englishName = order_ModelList.get(position).getEnglishName();
        String hindiName = order_ModelList.get(position).getHindiName();
        String orderId = order_ModelList.get(position).getOrderId();
        String extraPrice = order_ModelList.get(position).getExtraCharge();
        String orderOn = order_ModelList.get(position).getOrderedOn();
        String deliverOn = order_ModelList.get(position).getDeliveredOn();


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
        holder.product_Rate.setText(rate+" ");
        holder.orderid.setText(orderId);
        holder.extraPrice.setText("+ "+extraPrice);
        holder.orderOn.setText(orderOn);
        holder.deliverOn.setText(deliverOn);
    }

    @Override
    public int getItemCount() {
        return order_ModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView product_Icon;
        private TextView product_Name,product_Qty,product_Rate,ogEnglish,ogHindi,clothEnglish,orderid,extraPrice,orderOn,deliverOn;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            orderid = itemView.findViewById(R.id.orderid);
            product_Icon = itemView.findViewById(R.id.productIcon);
            product_Name = itemView.findViewById(R.id.productName);
            product_Qty = itemView.findViewById(R.id.productQty);
            product_Rate = itemView.findViewById(R.id.productPrice);
            ogEnglish = itemView.findViewById(R.id.ogEnglish);
            ogHindi = itemView.findViewById(R.id.ogHindi);
            clothEnglish = itemView.findViewById(R.id.cloth_english);
            extraPrice = itemView.findViewById(R.id.extraPrice);
            orderOn = itemView.findViewById(R.id.orderOn);
            deliverOn = itemView.findViewById(R.id.deliverOn);
        }

        private  void productIcon(String iconUrl)
        {
            Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.small_placeholder)).into(product_Icon);
        }
    }
  }