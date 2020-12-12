/*
package com.rs.royalgrocerystore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.Products;
import com.rs.royalgrocerystore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MYVIEWHOLDER> {


    List<Products> list;
    RvListener listener;
    Context context;

    public ProductAdapter(List<Products> list, RvListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.product_design,parent,false);
        return new MYVIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {

        Products products = list.get(position);

        holder.tvMRP.setText("MRP \u20B9 "+products.getMRP());
        holder.tvRGSPrice.setText("RGS Price \u20B9 "+products.getRgsprice());
        holder.tvSave.setText("Save \u20B9 "+products.getSave());
        holder.tvProduct.setText(""+products.getProduct());

        Picasso.get().load(products.getImage()).into(holder.ivItem);

      if (products.getCartqy()!=0){

          holder.relativeQty.setVisibility(View.VISIBLE);
          holder.btnAddtocart.setVisibility(View.GONE);
      }

      holder.btnAddtocart.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              sendAddToCart();
          }
      });
    }

    private void sendAddToCart() {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MYVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvSave, tvRGSPrice, tvMRP, tvProduct;
        ImageView ivItem, ivminus, ivPlus;
        Button btnAddtocart;
        RelativeLayout  relativeQty;


        public MYVIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            tvMRP = itemView.findViewById(R.id.tvMRP);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvSave = itemView.findViewById(R.id.tvSave);
            tvRGSPrice = itemView.findViewById(R.id.tvRGSPrice);
            ivItem = itemView.findViewById(R.id.ivItem);

            btnAddtocart = itemView.findViewById(R.id.btnAddtocart);
            relativeQty = itemView.findViewById(R.id.relativeQty);

            ivminus = itemView.findViewById(R.id.ivminus);
            ivPlus = itemView.findViewById(R.id.ivPlus);

            itemView.setOnClickListener(this);
            btnAddtocart.setOnClickListener(this);
            ivPlus.setOnClickListener(this);
            ivminus.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.Rvclick(view,getAdapterPosition());
        }
    }
}
*/
