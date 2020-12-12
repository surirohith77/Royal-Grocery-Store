package com.rs.royalgrocerystore.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.PreviousOrderDetails;
import com.rs.royalgrocerystore.Model.PreviousOrders;
import com.rs.royalgrocerystore.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PreviousOrderDetailsAdapter extends RecyclerView.Adapter<PreviousOrderDetailsAdapter.MYVIEWHOLDER> {

    List<PreviousOrderDetails> list;
    RvListener listener;

    public PreviousOrderDetailsAdapter(List<PreviousOrderDetails> list, RvListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_order_details_rec,parent,false);
        return new MYVIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {

        PreviousOrderDetails previousOrderDetails = list.get(position);

        holder.tvPrice.setText("Price \u20B9 "+previousOrderDetails.getPrice());
        holder.tvQty.setText("Qty "+previousOrderDetails.getQty());
        holder.tvProduct.setText(previousOrderDetails.getProduct());

        Picasso.get().load(previousOrderDetails.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MYVIEWHOLDER extends RecyclerView.ViewHolder {
        TextView tvProduct, tvQty, tvPrice;
        ImageView imageView;

        public MYVIEWHOLDER(@NonNull View itemView) {
            super(itemView);

            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
