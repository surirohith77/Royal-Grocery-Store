package com.rs.royalgrocerystore.Ui.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.rs.royalgrocerystore.Adapter.CategoriesIconAdapter;
import com.rs.royalgrocerystore.Adapter.IconCategoryViewAllAdapter;
import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.CategoryIcon;
import com.rs.royalgrocerystore.R;

import java.util.ArrayList;

public class IconCategoryViewAllActivity extends AppCompatActivity implements RvListener {


    RecyclerView rvCategoryIcon;
    IconCategoryViewAllAdapter iconCategoryViewAllAdapter;
    ArrayList categoryIconList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_category_view_all);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Categories");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        intializeCategoryIconRecyclerview();
        initializeRvCategories();

    }


    private void intializeCategoryIconRecyclerview() {

        rvCategoryIcon = findViewById(R.id.rvCategoryIcon);
        rvCategoryIcon.setHasFixedSize(true);
        //rvFeatures.setLayoutManager(new LinearLayoutManager(activity));
        rvCategoryIcon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // GridLayoutManager manager = new GridLayoutManager(activity,2);
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rvCategoryIcon.setLayoutManager(layoutManager);

    }



    private void initializeRvCategories() {


        categoryIconList = new ArrayList();
        categoryIconList.add(new CategoryIcon("Grocery",R.drawable.grocery));
        categoryIconList.add(new CategoryIcon("Vegetables",R.drawable.vegetable));
        categoryIconList.add(new CategoryIcon("Non Veg",R.drawable.non_veg));
        categoryIconList.add(new CategoryIcon("Soft Drinks",R.drawable.soft_drink2));




     /*   iconFoods.add(new iconslider(R.drawable.cake,"Great Offers"));
        iconFoods.add(new iconslider(R.drawable.food1,"Express Delivery"));
        iconFoods.add(new iconslider(R.drawable.food2,"Rohith Suri"));*/
        iconCategoryViewAllAdapter = new IconCategoryViewAllAdapter(categoryIconList,this);
        rvCategoryIcon.setAdapter(iconCategoryViewAllAdapter);

    }


    @Override
    public void Rvclick(View view, int Position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}