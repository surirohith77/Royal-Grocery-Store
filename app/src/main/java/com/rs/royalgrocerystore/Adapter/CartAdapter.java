package com.rs.royalgrocerystore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.Cart;
import com.rs.royalgrocerystore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MYVIEWHOLDER> {

    List<Cart> list;
    RvListener listener;
    Context context;

    public CartAdapter(List<Cart> list, RvListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cart_rec_design,parent,false);
        return new CartAdapter.MYVIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {

        Cart cart = list.get(position);

        holder.tvPriceQty.setText("\u20B9 "+cart.getPrice()+" X "+cart.getQty());
        holder.tvTotalPrice.setText("\u20B9 "+cart.getTotal());
        holder.tvQty.setText(""+cart.getQty());
        holder.tvProductName.setText(""+cart.getProduct());

        Picasso.get().load(cart.getImage()).into(holder.imageItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MYVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvProductName, tvPriceQty, tvTotalPrice, tvQty;
        ImageView imageItem, ivminus, ivPlus, ivRemoveProduct;

        public MYVIEWHOLDER(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPriceQty = itemView.findViewById(R.id.tvPriceQty);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvQty = itemView.findViewById(R.id.tvQty);

            imageItem = itemView.findViewById(R.id.imageItem);
            ivminus = itemView.findViewById(R.id.ivminus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            ivRemoveProduct = itemView.findViewById(R.id.ivRemoveProduct);

            itemView.setOnClickListener(this);
            ivPlus.setOnClickListener(this);
            ivminus.setOnClickListener(this);
            ivRemoveProduct.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            listener.Rvclick(view,getAdapterPosition());
        }
    }
}
