package com.rs.royalgrocerystore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rs.royalgrocerystore.Model.ImageSlider;
import com.rs.royalgrocerystore.Model.Products;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Ui.Activities.CategoryWiseActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SmarateistSliderAdapter extends SliderViewAdapter<SmarateistSliderAdapter.MYVIEWHOLDER> {

    private Context context;
    private List<Products> mSliderItems = new ArrayList<>();

    public SmarateistSliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<Products> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Products sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }





    @Override
    public MYVIEWHOLDER onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item_for_bottom, null);
        return new MYVIEWHOLDER(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {
        final Products sliderItem = mSliderItems.get(position);

      //  holder.textViewDescription.setText(sliderItem.getDescription());
        //holder.textViewDescription.setTextSize(16);
      //  holder.textViewDescription.setTextColor(Color.WHITE);
        Glide.with(holder.itemView)
                .load(sliderItem.getImage())
                .fitCenter()
                .into(holder.imageViewBackground);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(context, " " + sliderItem.getCategory(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, CategoryWiseActivity.class);
                intent.putExtra("request",sliderItem.getCategory());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getCount() {
        return  mSliderItems.size();
    }

    class MYVIEWHOLDER extends SliderViewAdapter.ViewHolder  {
        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;
        public MYVIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
