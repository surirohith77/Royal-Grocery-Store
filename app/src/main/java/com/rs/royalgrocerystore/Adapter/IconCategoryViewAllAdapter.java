package com.rs.royalgrocerystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.CategoryIcon;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Ui.Activities.CategoryWiseActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IconCategoryViewAllAdapter extends RecyclerView.Adapter<IconCategoryViewAllAdapter.MYVIEWHOLDER> {

    List<CategoryIcon> list;
    RvListener listener;
    Context context;

    public IconCategoryViewAllAdapter(List<CategoryIcon> list, RvListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_categories_viewall_design, parent, false);
        return new MYVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {

        final CategoryIcon categoryIcon = list.get(position);
        Picasso.get().load(categoryIcon.getImage()).into(holder.ivICon);
        holder.tvTitle.setText(categoryIcon.getTitle());

        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    context.startActivity(new Intent(context, IconSubCategoryActivity.class));
                Intent intent = new Intent(context, CategoryWiseActivity.class);
                intent.putExtra("request",categoryIcon.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MYVIEWHOLDER extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivICon;
        RelativeLayout relative;

        public MYVIEWHOLDER(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvFeatures);
            ivICon = itemView.findViewById(R.id.ivCheckMark);
            relative = itemView.findViewById(R.id.relative);
        }
    }
}
