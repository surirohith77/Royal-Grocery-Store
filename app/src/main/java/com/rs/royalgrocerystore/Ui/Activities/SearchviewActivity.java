package com.rs.royalgrocerystore.Ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.rs.royalgrocerystore.Utils.ApplicationRequest;
import com.rs.royalgrocerystore.Utils.AutoSuggestAdapter;
import com.squareup.picasso.Picasso;

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

public class SearchviewActivity extends AppCompatActivity implements RvListener {

    EditText etSearch;

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private List<String> stringList;

    String request;

    private List<Products> groceryList, vegetableList, bannerList;
    private SearchviewAdapter searchviewAdapter;
    RecyclerView rvGrocery;
    private TextView tvEmptyMessage;
    private ImageView ivEmptyImage;
    private LinearLayout llEmptyState;

    String customerid;

    String    serached;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview);


        SharedPreferences sharedPref = getSharedPreferences(
                "royal_grocery_store", Context.MODE_PRIVATE);

        customerid = sharedPref.getString("id", "");


        initToolbar();
        initializeView();
        initializeRecyclerViewGrocery();
        //getGroceries(serached);
      //  autocomplete();
    }

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
        searchviewAdapter = new SearchviewAdapter(groceryList,this);
        rvGrocery.setAdapter(searchviewAdapter);

    }


    private void initToolbar() {
        etSearch = findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here

                // yourEditText...

                serached = String.valueOf(s);
                getGroceries(serached);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }





    private void getGroceries(String serached) {


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

        Call<List<Products>> call = apiService.getSearchItems("search",serached);
        call.enqueue(new Callback<List<Products>>() {
                         @Override
                         public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                             List<Products> KyclistActivity = response.body();

                             if (KyclistActivity != null) {
                                 groceryList.addAll(KyclistActivity);
                                 Collections.reverse(groceryList);
                                 searchviewAdapter.notifyDataSetChanged();
                                 progressDialog.dismiss();

                                 updateViews("Not searched till now", R.drawable.moneky4);
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
    public void Rvclick(View view, int Position) {

    }

    class SearchviewAdapter extends RecyclerView.Adapter<SearchviewAdapter.MYVIEWEHOLDER> {

        List<Products> list;
        RvListener listener;
        Context context;

        public SearchviewAdapter(List<Products> list, RvListener listener) {
            this.list = list;
            this.listener = listener;
        }

        @NonNull
        @Override
        public MYVIEWEHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                context = parent.getContext();
                View view = LayoutInflater.from(context).inflate(R.layout.product_design,parent,false);
                return new MYVIEWEHOLDER(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MYVIEWEHOLDER holder, int position) {


            final Products products = list.get(position);

            holder.tvMRP.setText("MRP \u20B9 "+products.getMRP());
            holder.tvRGSPrice.setText("RGS Price \u20B9 "+products.getRgsprice());
            holder.tvSave.setText("Save \u20B9 "+products.getSave());
            holder.tvProduct.setText(""+products.getProduct());
          //  holder.tvQty.setText(""+products.getCartqy());
            Picasso.get().load(products.getImage()).into(holder.ivItem);

            /*if (products.getCartqy()!=0){

                holder.relativeQty.setVisibility(View.VISIBLE);
                holder.btnAddtocart.setVisibility(View.GONE);

            }

            else {

                holder.relativeQty.setVisibility(View.GONE);
                holder.btnAddtocart.setVisibility(View.VISIBLE);

            }
*/
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

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MYVIEWEHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvSave, tvRGSPrice, tvMRP, tvProduct, tvQty;
            ImageView ivItem, ivminus, ivPlus;
            Button btnAddtocart;
            RelativeLayout relativeQty;

            public MYVIEWEHOLDER(@NonNull View itemView) {
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

            }

            @Override
            public void onClick(View v) {
                listener.Rvclick(v,getAdapterPosition());
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
                                Toast.makeText(SearchviewActivity.this, "Product added to Cart", Toast.LENGTH_SHORT).show();

                            }

                            else {

                                Toast.makeText(SearchviewActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(SearchviewActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(SearchviewActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
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
     //   String url = "http://kga.bterp.in/Mobileservices.aspx?";
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


                                onSuccess();
                                Toast.makeText(SearchviewActivity.this, "Product reduced to Cart", Toast.LENGTH_SHORT).show();
                            }

                            else {

                                Toast.makeText(SearchviewActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(SearchviewActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(SearchviewActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
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

        getGroceries(serached);

    }

}