package com.rs.royalgrocerystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.PreviousOrders;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Ui.Activities.PreviousOrderDetailsActivity;

import java.util.List;

public class PreviousAdaper extends RecyclerView.Adapter<PreviousAdaper.MYVIEHWOLDER> {

    List<PreviousOrders> list;
    RvListener listener;
    Context context;

    public PreviousAdaper(List<PreviousOrders> list, RvListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MYVIEHWOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.previous_orders_rec,parent,false);
        return new MYVIEHWOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEHWOLDER holder, int position) {

        final PreviousOrders previousOrders = list.get(position);

        holder.tvDate.setText("Ordered On "+previousOrders.getDate());
        holder.tvORderid.setText("Order Id "+previousOrders.getOrderid());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PreviousOrderDetailsActivity.class);
                intent.putExtra("orderid",previousOrders.getOrderid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MYVIEHWOLDER extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvORderid, tvDate;
        CardView cardView;


        public MYVIEHWOLDER(@NonNull View itemView) {
            super(itemView);

            tvORderid = itemView.findViewById(R.id.tvOrderid);
            tvDate = itemView.findViewById(R.id.tvDate);
            cardView = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(this);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.Rvclick(view,getAdapterPosition());
        }
    }
}
