package com.rs.royalgrocerystore.Ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rs.royalgrocerystore.Data.ApiClient;
import com.rs.royalgrocerystore.Data.ApiDao;
import com.rs.royalgrocerystore.Internet.NetworkConnection;
import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.Products;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Ui.Fragments.HomeFragment;
import com.rs.royalgrocerystore.Utils.ApplicationRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryWiseActivity extends AppCompatActivity implements RvListener{

    String request;

    private List<Products> groceryList, vegetableList, bannerList;
    private CategoryAdapter categoryAdapter, vegetableAdapter;
    RecyclerView rvGrocery;
    private TextView tvEmptyMessage;
    private ImageView ivEmptyImage;
    private LinearLayout llEmptyState;

    String customerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise);

        SharedPreferences sharedPref = getSharedPreferences(
                "royal_grocery_store", Context.MODE_PRIVATE);

        customerid = sharedPref.getString("id", "");

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){
            request =   bundle.getString("request");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(request);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initializeView();
        initializeRecyclerViewGrocery();
        getGroceries();

    }


 /*   private void Toolbar(){

        toolbar = findViewById(R.id.toolbar);
        final ImageView ic_search = findViewById(R.id.ic_search);
        //toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        TextView tvTool1 = findViewById(R.id.tvTool1);
        tvTool1.setText(R.string.app_name);
        *//*final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar();*//*
        *//*.setDisplayHomeAsUpEnabled(true);*//*

        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(CategoryWiseActivity.this, SearchviewActivity.class));
            }
        });
    }
*/

    private void initializeView() {

        tvEmptyMessage =findViewById(R.id.list_tv_empty_message);
        ivEmptyImage =  findViewById(R.id.list_iv_empty_image);
        llEmptyState =  findViewById(R.id.list_ll_empty_state);

        /*swipeRefreshLayout =  view.findViewById(R.id.list_swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorGrey));
        swipeRefreshLayout.setOnRefreshListener(this);*/

    }


    private void initializeRecyclerViewGrocery() {

        rvGrocery = findViewById(R.id.list_rec_view);
        rvGrocery.setHasFixedSize(true);
        rvGrocery.setLayoutManager(new LinearLayoutManager(this));
/*
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);

        rvRequest.addItemDecoration(decoration);
*/

        groceryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(groceryList,this);
        rvGrocery.setAdapter(categoryAdapter);

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
/*

    private void getGroceries() {


        groceryList.clear();

        if (!NetworkConnection.isConnected(this)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<Products>> call = apiService.getGrocery("Products",request,customerid);
        call.enqueue(new Callback<List<Products>>() {
                         @Override
                         public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                             List<Products> KyclistActivity = response.body();

                             if (KyclistActivity != null) {
                                 groceryList.addAll(KyclistActivity);
                                 Collections.reverse(groceryList);
                                 categoryAdapter.notifyDataSetChanged();
                                 progressDialog.dismiss();

                                 updateViews("No refers till Now", R.drawable.moneky4);
                             } else {
                                 updateViews("Invaild Data", R.drawable.ic_invalid_data);
                             }
                         }


                         @Override
                         public void onFailure(Call<List<Products>> call, Throwable t) {
                            // updateViews(getString(R.string.error_connection_request_failed), R.drawable.ic_connection_break);
                             updateViews("No products added till now", R.drawable.ic_connection_break);
                          //   Log.e(TAG, t.toString());
                             progressDialog.dismiss();
                         }
                     }
        );
    }
*/

    private void getGroceries() {

        groceryList.clear();

        if (!NetworkConnection.isConnected(this)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();
        String url = getString(R.string.url);

        //Again creating the string request
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //itemList.clear();
                        progressDialog.dismiss();
                        //Log.e(TAG, response);

                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            int lengt = jsonArray.length();

                            for (int inde = 0; inde < lengt; inde++) {

                                JSONObject jsonObjec = jsonArray.getJSONObject(inde);

                                Products item = new Products();
                                item.setImage(jsonObjec.getString("image"));
                                item.setProduct(jsonObjec.getString("product"));
                                item.setMRP(jsonObjec.getString("MRP"));
                                item.setRgsprice(jsonObjec.getString("rgsprice"));
                                item.setSave(jsonObjec.getDouble("save"));
                                item.setCartqy(jsonObjec.getInt("cartqy"));
                                item.setCategory(jsonObjec.getString("category"));
                                item.setId(jsonObjec.getInt("id"));

                                groceryList.add(item);

                                Collections.reverse(groceryList);
                                categoryAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            // Toast.makeText(activity, "Invalid resonse from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(CategoryWiseActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "Products");
                params.put("category", request);
                params.put("customerid", customerid);

                return params;
            }
        };

        int socketTimeout = 30000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        //jsonRequest.setTag(TAG);
        jsonRequest.setRetryPolicy(policy);

        ApplicationRequest.getInstance(this).addToRequestQueue(jsonRequest);


    }


    private void updateViews(String message, int drawableID) {

        // Stop refresh animation
        //     swipeRefreshLayout.setRefreshing(false);
        //   swipeRefreshLayout.setRefreshing(false);
        //adapter.notifyDataSetChanged();


        if (groceryList.isEmpty()) {


            tvEmptyMessage.setText(message);
            ivEmptyImage.setImageResource(drawableID);
            llEmptyState.setVisibility(View.VISIBLE);
        } else {
            llEmptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.srh_list, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                categoryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                categoryAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }


    @Override
    public void Rvclick(View view, int Position) {

    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MYVIEWHOLDER> implements Filterable {

        List<Products> list;
        RvListener listener;
        Context context;
        List<Products> mFilteredList;

        public CategoryAdapter(List<Products> list, RvListener listener) {
            this.list = list;
            this.listener = listener;
            this.mFilteredList = list;
        }

        @NonNull
        @Override
        public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.product_design,parent,false);
            return new MYVIEWHOLDER(view,context);
        }

        @Override
        public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {

            final Products products = mFilteredList.get(position);

            holder.tvMRP.setText("MRP \u20B9 "+products.getMRP());
            holder.tvRGSPrice.setText("RGS Price \u20B9 "+products.getRgsprice());
            holder.tvSave.setText("Save \u20B9 "+products.getSave());
            holder.tvProduct.setText(""+products.getProduct());
            holder.tvQty.setText(""+products.getCartqy());
            Picasso.get().load(products.getImage()).into(holder.ivItem);



            if (products.getCartqy()!=0){

                holder.relativeQty.setVisibility(View.VISIBLE);
                holder.btnAddtocart.setVisibility(View.GONE);

            }

            else {

                holder.relativeQty.setVisibility(View.GONE);
                holder.btnAddtocart.setVisibility(View.VISIBLE);

            }

            holder.btnAddtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sendAddToCart(products.getId());
                }
            });

            holder.ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sendAddToCart(products.getId());
                }
            });

            holder.ivminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (products.getId() != 0) {

                        sendDecreaseQtyRequest(products.getId());

                    }
                }
            });

            holder.bind(products);
        }

        @Override
        public int getItemCount() {
            return mFilteredList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        mFilteredList = list;
                    } else {
                        List<Products> filteredList = new ArrayList<>();
                        for (Products row : list) {


                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getProduct().toLowerCase().contains(charString.toLowerCase()) || row.getCategory().toLowerCase().contains(charSequence) ){
                                filteredList.add(row);
                            }
                        }

                        mFilteredList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilteredList = (ArrayList<Products>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class MYVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView tvSave, tvRGSPrice, tvMRP, tvProduct, tvQty;
            ImageView ivItem, ivminus, ivPlus;
            Button btnAddtocart;
            RelativeLayout relativeQty;
            Context context;
            Products dpojo;

            public MYVIEWHOLDER(@NonNull View itemView, Context context) {
                super(itemView);
                tvMRP = itemView.findViewById(R.id.tvMRP);
                tvProduct = itemView.findViewById(R.id.tvProduct);
                tvSave = itemView.findViewById(R.id.tvSave);
                tvRGSPrice = itemView.findViewById(R.id.tvRGSPrice);
                tvQty = itemView.findViewById(R.id.tvQty);
                ivItem = itemView.findViewById(R.id.ivItem);

                btnAddtocart = itemView.findViewById(R.id.btnAddtocart);
                relativeQty = itemView.findViewById(R.id.relativeQty);

                ivminus = itemView.findViewById(R.id.ivminus);
                ivPlus = itemView.findViewById(R.id.ivPlus);

                itemView.setOnClickListener(this);
                btnAddtocart.setOnClickListener(this);
                ivPlus.setOnClickListener(this);
                ivminus.setOnClickListener(this);
                this.context = context;
            }

            @Override
            public void onClick(View view) {
                listener.Rvclick(view,getAdapterPosition());
            }

            public void bind(final Products products) {

                //interview_list dpojo = list.get(i);

               tvMRP.setText("MRP \u20B9 "+products.getMRP());
                tvRGSPrice.setText("RGS Price \u20B9 "+products.getRgsprice());
              tvSave.setText("Save \u20B9 "+products.getSave());
                tvProduct.setText(""+products.getProduct());
              tvQty.setText(""+products.getCartqy());
                Picasso.get().load(products.getImage()).into(ivItem);


                if (products.getCartqy()!=0){

                   relativeQty.setVisibility(View.VISIBLE);
                    btnAddtocart.setVisibility(View.GONE);

                }

                else {

                    relativeQty.setVisibility(View.GONE);
                    btnAddtocart.setVisibility(View.VISIBLE);

                }

               btnAddtocart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sendAddToCart(products.getId());
                    }
                });

              ivPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sendAddToCart(products.getId());
                    }
                });

               ivminus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (products.getId() != 0) {

                            sendDecreaseQtyRequest(products.getId());

                        }
                    }
                });

                dpojo = products; //<-- keep a reference to the current item
            }
        }


    }

    private void sendAddToCart(final int id) {


        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(this)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }
       // String url = "http://kga.bterp.in/Mobileservices.aspx?";
        String url = getString(R.string.url);

        //Again creating the string request
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //   itemList.clear();
                        progressDialog.dismiss();
                        //Log.e(TAG, response);

                        try {

                            JSONObject object = new JSONObject(response);

                            boolean error =      object.getBoolean("error");

                            if (!error) {



                                // Toast.makeText(activity, "Login Success ", Toast.LENGTH_SHORT).show();

                                onSuccess();

                                Toast.makeText(CategoryWiseActivity.this, "Product Added to cart", Toast.LENGTH_SHORT).show();


                            }

                            else {

                                Toast.makeText(CategoryWiseActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(CategoryWiseActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(CategoryWiseActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "increment");
                params.put("productid", String.valueOf(id));
                params.put("customerid", customerid);

                return params;
            }
        };

        int socketTimeout = 30000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        //    jsonRequest.setTag(TAG);
        jsonRequest.setRetryPolicy(policy);

        ApplicationRequest.getInstance(this).addToRequestQueue(jsonRequest);

    }


    private void sendDecreaseQtyRequest(final int id) {


        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(this)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }
      //  String url = "http://kga.bterp.in/Mobileservices.aspx?";
        String url = getString(R.string.url);

        //Again creating the string request
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //   itemList.clear();
                        progressDialog.dismiss();
                        //Log.e(TAG, response);

                        try {

                            JSONObject object = new JSONObject(response);

                            boolean error =      object.getBoolean("error");

                            if (!error) {

                                // Toast.makeText(activity, "Login Success ", Toast.LENGTH_SHORT).show();
                                onSuccess();
                                Toast.makeText(CategoryWiseActivity.this, "Product reduced from cart", Toast.LENGTH_SHORT).show();
                            }

                            else {

                                Toast.makeText(CategoryWiseActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(CategoryWiseActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(CategoryWiseActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "decrement");
                params.put("productid", String.valueOf(id));
                params.put("customerid", customerid);

                return params;
            }
        };

        int socketTimeout = 30000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        //    jsonRequest.setTag(TAG);
        jsonRequest.setRetryPolicy(policy);

        ApplicationRequest.getInstance(this).addToRequestQueue(jsonRequest);

    }

    private void onSuccess() {


        // Delay screen
      /*  long postDelayTime = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               getCartItems();
            }
        }, postDelayTime);*/

    /*    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                getCartItems();
            }
        }, 500);*/

        getGroceries();

    }

}