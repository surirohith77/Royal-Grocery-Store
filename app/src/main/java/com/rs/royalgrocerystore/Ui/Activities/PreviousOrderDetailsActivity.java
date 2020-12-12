package com.rs.royalgrocerystore.Ui.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rs.royalgrocerystore.Adapter.PreviousOrderDetailsAdapter;
import com.rs.royalgrocerystore.Data.ApiClient;
import com.rs.royalgrocerystore.Data.ApiDao;
import com.rs.royalgrocerystore.Internet.NetworkConnection;
import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.PreviousOrderDetails;
import com.rs.royalgrocerystore.Model.Products;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Utils.ApplicationRequest;

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

public class PreviousOrderDetailsActivity extends AppCompatActivity implements RvListener {

    private List<PreviousOrderDetails> previousOrderDetailsList;
    private PreviousOrderDetailsAdapter previousOrderDetailsAdapter;
    RecyclerView rvPreviousOrderDetails;
    private TextView tvEmptyMessage;
    private ImageView ivEmptyImage;
    private LinearLayout llEmptyState;

    String customerid;
    int orderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_order_details);


        SharedPreferences sharedPref = getSharedPreferences(
                "royal_grocery_store", Context.MODE_PRIVATE);

        customerid = sharedPref.getString("id", "");

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){
            orderid =   bundle.getInt("orderid");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Previous Order Details");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initializeView();
        initializeRvPreviousOrderDetails();
        getPreviousOrderDetails();
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


    private void initializeRvPreviousOrderDetails() {

        rvPreviousOrderDetails = findViewById(R.id.list_rec_view);
        rvPreviousOrderDetails.setHasFixedSize(true);
     //   rvPreviousOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager manager = new GridLayoutManager(this,2);
        rvPreviousOrderDetails.setLayoutManager(manager);
/*
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);

        rvRequest.addItemDecoration(decoration);
*/

        previousOrderDetailsList = new ArrayList<>();
        previousOrderDetailsAdapter = new PreviousOrderDetailsAdapter(previousOrderDetailsList,this);
        rvPreviousOrderDetails.setAdapter(previousOrderDetailsAdapter);

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

   /* private void getPreviousOrderDetails() {


        previousOrderDetailsList.clear();

        if (!NetworkConnection.isConnected(this)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<PreviousOrderDetails>> call = apiService.getPreviousOrderDetails("orderitems", String.valueOf(orderid));
        call.enqueue(new Callback<List<PreviousOrderDetails>>() {
                         @Override
                         public void onResponse(Call<List<PreviousOrderDetails>> call, Response<List<PreviousOrderDetails>> response) {
                             List<PreviousOrderDetails> KyclistActivity = response.body();

                             if (KyclistActivity != null) {
                                 previousOrderDetailsList.addAll(KyclistActivity);
                              //   Collections.reverse(previousOrderDetailsList);
                                 previousOrderDetailsAdapter.notifyDataSetChanged();
                                 progressDialog.dismiss();

                                 updateViews("No refers till Now", R.drawable.moneky4);
                             } else {
                                 updateViews("Invaild Data", R.drawable.ic_invalid_data);
                             }
                         }


                         @Override
                         public void onFailure(Call<List<PreviousOrderDetails>> call, Throwable t) {
                             updateViews(getString(R.string.error_connection_request_failed), R.drawable.ic_connection_break);
                             //   Log.e(TAG, t.toString());
                             progressDialog.dismiss();
                         }
                     }
        );
    }
*/

    private void getPreviousOrderDetails() {


        previousOrderDetailsList.clear();

        if (!NetworkConnection.isConnected(this)) {
            updateViews("No internet connection", R.drawable.dog6);
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
                    //    Log.e(TAG, response);

                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            int lengt = jsonArray.length();

                            for (int inde = 0; inde < lengt; inde++) {

                                JSONObject jsonObjec = jsonArray.getJSONObject(inde);

                                PreviousOrderDetails item = new PreviousOrderDetails();
                                item.setImage(jsonObjec.getString("image"));
                                item.setProduct(jsonObjec.getString("product"));
                                item.setQty(jsonObjec.getInt("qty"));
                                item.setPrice(jsonObjec.getDouble("price"));

                                previousOrderDetailsList.add(item);

                                Collections.reverse(previousOrderDetailsList);

                                previousOrderDetailsAdapter.notifyDataSetChanged();
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
                        Toast.makeText(PreviousOrderDetailsActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "orderitems");
                params.put("orderid",  String.valueOf(orderid));

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

    private void updateViews(String message, int drawableID) {

        // Stop refresh animation
        //     swipeRefreshLayout.setRefreshing(false);
        //   swipeRefreshLayout.setRefreshing(false);
        //adapter.notifyDataSetChanged();


        if (previousOrderDetailsList.isEmpty()) {


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
}