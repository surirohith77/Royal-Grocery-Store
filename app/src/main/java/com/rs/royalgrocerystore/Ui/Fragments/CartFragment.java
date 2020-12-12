package com.rs.royalgrocerystore.Ui.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rs.royalgrocerystore.Adapter.CartAdapter;
import com.rs.royalgrocerystore.Data.ApiClient;
import com.rs.royalgrocerystore.Data.ApiDao;
import com.rs.royalgrocerystore.Internet.NetworkConnection;
import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.Cart;
import com.rs.royalgrocerystore.Model.Products;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Ui.Activities.LoginActivity;
import com.rs.royalgrocerystore.Ui.Activities.MainActivity;
import com.rs.royalgrocerystore.Ui.Activities.OrderPlaceActivity;
import com.rs.royalgrocerystore.Utils.ApplicationRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements RvListener {

    Activity activity;
    View view;

    private static final String TAG = CartFragment.class.getSimpleName();
    private List<Cart> itemList;
    private CartAdapter cartAdapter;
    private TextView tvEmptyMessage;
    private ImageView ivEmptyImage;
    private LinearLayout llEmptyState;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvCart;
    String cid;

    TextView tvTotalAmt, tvDeliveryAmt, tvCartAmt;
    CardView cardTotalAmt;

    Button btnCheckOut;
    Double Final;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cart_fragment,container,false);


        SharedPreferences sharedPref = activity.getSharedPreferences(
                "royal_grocery_store", Context.MODE_PRIVATE);

       cid = sharedPref.getString("id", "");

        initializeView();
        initializeRecyclerViewCart();
        getCartItems();
        getCartTotalPrice();

        return view;
    }


    private void initializeView() {

        tvEmptyMessage = view.findViewById(R.id.list_tv_empty_message);
        ivEmptyImage =  view.findViewById(R.id.list_iv_empty_image);
        llEmptyState =  view.findViewById(R.id.list_ll_empty_state);

        tvTotalAmt = view.findViewById(R.id.tvTotalAmt);
        tvCartAmt =  view.findViewById(R.id.tvCartAmt);
        tvDeliveryAmt =  view.findViewById(R.id.tvDeliveryAmt);
        cardTotalAmt = view.findViewById(R.id.cardTotalAmt);

        btnCheckOut = view.findViewById(R.id.btnCheckOut);



        Animation animation1 =
                AnimationUtils.loadAnimation(activity, R.anim.down_to_up2);
        cardTotalAmt.startAnimation(animation1);

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, OrderPlaceActivity.class);
                intent.putExtra("total",Final);
                startActivity(intent);
            }
        });
        /*swipeRefreshLayout =  view.findViewById(R.id.list_swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorGrey));
        swipeRefreshLayout.setOnRefreshListener(this);*/

    }

    private void initializeRecyclerViewCart() {

        rvCart = view.findViewById(R.id.rvCart);
        rvCart.setHasFixedSize(true);
        rvCart.setLayoutManager(new LinearLayoutManager(activity));
/*
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);

        rvRequest.addItemDecoration(decoration);
*/

        itemList = new ArrayList<>();
        cartAdapter = new CartAdapter(itemList,this);
        rvCart.setAdapter(cartAdapter);

    }

   /* private void getCartItems() {



        itemList.clear();

        if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<Cart>> call = apiService.getCartItems("cartitems",cid);
        call.enqueue(new Callback<List<Cart>>() {
                         @Override
                         public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                             List<Cart> KyclistActivity = response.body();

                             if (KyclistActivity != null) {
                                 itemList.addAll(KyclistActivity);
                               //  Collections.reverse(itemList);
                                 cartAdapter.notifyDataSetChanged();

                                 if (itemList.isEmpty()){
                                     cardTotalAmt.setVisibility(View.GONE);
                                 }

                                 List<Cart> carts = response.body();
                                 for (Cart post : carts) {
                                     String content = "";
                                     content += "ID: " + post.getPrice() + "\n";
                                    // textViewResult.append(content);
                                 }
                                 progressDialog.dismiss();

                                 updateViews("No items added till now", R.drawable.moneky4);
                             } else {
                                 updateViews("Invaild Data", R.drawable.ic_invalid_data);
                             }
                         }


                         @Override
                         public void onFailure(Call<List<Cart>> call, Throwable t) {
                             updateViews("Cart is empty", R.drawable.dog_with_towel);
                             Log.e(TAG, t.toString());
                             progressDialog.dismiss();
                         }
                     }
        );
    }

*/

    private void getCartItems() {


        itemList.clear();

        if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection", R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
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
                        Log.e(TAG, response);

                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            int lengt = jsonArray.length();

                            for (int inde = 0; inde < lengt; inde++) {

                                JSONObject jsonObjec = jsonArray.getJSONObject(inde);

                                Cart item = new Cart();
                                item.setImage(jsonObjec.getString("image"));
                                item.setProduct(jsonObjec.getString("product"));
                                item.setProductid(jsonObjec.getInt("productid"));
                                item.setPrice(jsonObjec.getDouble("price"));
                                item.setTotal(jsonObjec.getDouble("Total"));
                                item.setQty(jsonObjec.getInt("qty"));

                                itemList.add(item);

                                cartAdapter.notifyDataSetChanged();

                                if (itemList.isEmpty()){
                                    cardTotalAmt.setVisibility(View.GONE);
                                }
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
                        Toast.makeText(activity, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "cartitems");
                params.put("customerid", cid);

                return params;
            }
        };

        int socketTimeout = 30000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonRequest.setTag(TAG);
        jsonRequest.setRetryPolicy(policy);

        ApplicationRequest.getInstance(getContext()).addToRequestQueue(jsonRequest);
    }

        private void getCartTotalPrice(){

       // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(activity)) {
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

                        progressDialog.dismiss();
                        Log.e(TAG, response);

                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            int lengt = jsonArray.length();

                            for (int inde = 0; inde < lengt; inde++) {

                                JSONObject jsonObjec = jsonArray.getJSONObject(inde);

                                //  JSONObject jsonObject =   jsonObjec.getJSONObject("order");

                                Double total =  jsonObjec.getDouble("total");
                                int deliverycharge =  jsonObjec.getInt("deliverycharge");
                              Final =  jsonObjec.getDouble("Final");

                                if (total!=null){

                                    tvCartAmt.setText("\u20B9 "+total);
                                    tvDeliveryAmt.setText("\u20B9 "+deliverycharge);
                                    tvTotalAmt.setText("\u20B9 "+Final);
                                    cardTotalAmt.setVisibility(View.VISIBLE);

                                }

                                else {
                                    cardTotalAmt.setVisibility(View.GONE);
                                }

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
                        Toast.makeText(activity, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "carttotal");
                params.put("customerid", cid);

                //  params.put("merchant_id","1002");
                return params;
            }
        };

        int socketTimeout = 30000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonRequest.setTag(TAG);
        jsonRequest.setRetryPolicy(policy);

        ApplicationRequest.getInstance(getContext()).addToRequestQueue(jsonRequest);

    }

    @Override
    public void Rvclick(View view, int Position) {

        Cart cart = itemList.get(Position);

        int productid = cart.getProductid();

        switch (view.getId()){

            case R.id.ivPlus:
                sendIncreaseQtyRequest(productid);
                break;

            case R.id.ivminus:
                sendDecreaseQtyRequest(productid);
                break;

            case R.id.ivRemoveProduct:
                sendRemoveItemRequest(productid);
                break;
        }


    }




    private void updateViews(String message, int drawableID) {

        // Stop refresh animation
        //     swipeRefreshLayout.setRefreshing(false);
        //   swipeRefreshLayout.setRefreshing(false);
        //adapter.notifyDataSetChanged();


        if (itemList.isEmpty()) {


            tvEmptyMessage.setText(message);
            ivEmptyImage.setImageResource(drawableID);
            llEmptyState.setVisibility(View.VISIBLE);
        } else {
            llEmptyState.setVisibility(View.GONE);
        }
    }

    private void sendIncreaseQtyRequest(final int productid) {


        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(activity)) {
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


                            }

                            else {

                                Toast.makeText(activity, "Something went wrong ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(activity, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(activity, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "increment");
                params.put("productid", String.valueOf(productid));
                params.put("customerid", cid);

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

        ApplicationRequest.getInstance(activity).addToRequestQueue(jsonRequest);
    }

    private void sendDecreaseQtyRequest(final int productid) {


        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(activity)) {
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
                            }

                            else {

                                Toast.makeText(activity, "Something went wrong ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(activity, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(activity, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "decrement");
                params.put("productid", String.valueOf(productid));
                params.put("customerid", cid);

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

        ApplicationRequest.getInstance(activity).addToRequestQueue(jsonRequest);

    }

    private void sendRemoveItemRequest(final int productid) {


        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(activity)) {
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


                                onSuccess();
                                Toast.makeText(activity, "Product Removed ", Toast.LENGTH_SHORT).show();
                            }

                            else {

                                Toast.makeText(activity, "Something went wrong ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(activity, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(activity, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "deleteitem");
                params.put("productid", String.valueOf(productid));
                params.put("customerid", cid);

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

        ApplicationRequest.getInstance(activity).addToRequestQueue(jsonRequest);

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


        // calling a funtion from mainactivity from cartfragment
        ((MainActivity)getActivity()).getCartCount();

        getCartItems();
        getCartTotalPrice();

    }





}
