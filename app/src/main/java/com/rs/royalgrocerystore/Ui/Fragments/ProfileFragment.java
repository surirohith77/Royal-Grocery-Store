package com.rs.royalgrocerystore.Ui.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rs.royalgrocerystore.Adapter.PreviousAdaper;
import com.rs.royalgrocerystore.Data.ApiClient;
import com.rs.royalgrocerystore.Data.ApiDao;
import com.rs.royalgrocerystore.Internet.NetworkConnection;
import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.PreviousOrders;
import com.rs.royalgrocerystore.Model.Products;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Ui.Activities.CategoryWiseActivity;
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

public class ProfileFragment extends Fragment implements RvListener {

    Activity activity;
    View view;
    String customerid, name , email, mobile;
    TextView tvMobile, tvName, tvEmail;

    private List<PreviousOrders> previousOrdersList;
    private PreviousAdaper previousAdaper;
    RecyclerView rvPreviousOrders;
    private TextView tvEmptyMessage;
    private ImageView ivEmptyImage;
    private LinearLayout llEmptyState;

    private static final String TAG = CartFragment.class.getSimpleName();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment,container,false);

        SharedPreferences sharedPref = activity.getSharedPreferences(
                "royal_grocery_store", Context.MODE_PRIVATE);

        customerid = sharedPref.getString("id", "");
        name = sharedPref.getString("name", "");
        mobile = sharedPref.getString("mobile", "");
        email = sharedPref.getString("email", "");


        initialize();
        initializeView();
        initializeRecyclerViewPreviousORders();
        getPreviousOrders();

        return view;
    }

    private void initialize() {


        tvName  = view.findViewById(R.id.tvName);
        tvMobile  = view.findViewById(R.id.tvMobile);
        tvEmail  = view.findViewById(R.id.tvemail);

        tvName.setText(name);
        tvMobile.setText(mobile);
        tvEmail.setText(email);

    }


    private void initializeView() {

        tvEmptyMessage =view.findViewById(R.id.list_tv_empty_message);
        ivEmptyImage =  view.findViewById(R.id.list_iv_empty_image);
        llEmptyState =  view.findViewById(R.id.list_ll_empty_state);

        /*swipeRefreshLayout =  view.findViewById(R.id.list_swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorGrey));
        swipeRefreshLayout.setOnRefreshListener(this);*/

    }


    private void initializeRecyclerViewPreviousORders() {

        rvPreviousOrders = view.findViewById(R.id.list_rec_view);
        rvPreviousOrders.setHasFixedSize(true);
        rvPreviousOrders.setLayoutManager(new LinearLayoutManager(activity));
/*
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);

        rvRequest.addItemDecoration(decoration);
*/

        previousOrdersList = new ArrayList<>();
        previousAdaper = new PreviousAdaper(previousOrdersList,this);
        rvPreviousOrders.setAdapter(previousAdaper);

    }

    @Override
    public void Rvclick(View view, int Position) {

    }

   /* private void getPreviousOrders() {


        previousOrdersList.clear();

        if (!NetworkConnection.isConnected(activity)) {
            //updateViews("No internet connection",R.drawable.dog6);
            Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();
            updateViews("No Internet Connection", R.drawable.moneky4);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<PreviousOrders>> call = apiService.getPreviousOrders("orderlist",customerid);
        call.enqueue(new Callback<List<PreviousOrders>>() {
                         @Override
                         public void onResponse(Call<List<PreviousOrders>> call, Response<List<PreviousOrders>> response) {
                             List<PreviousOrders> KyclistActivity = response.body();

                             if (KyclistActivity != null) {
                                 previousOrdersList.addAll(KyclistActivity);
                              //   Collections.reverse(previousOrdersList);
                                 previousAdaper.notifyDataSetChanged();
                                 progressDialog.dismiss();

                                 //updateViews("No refers till Now", R.drawable.moneky4);
                             } else {
                                // updateViews("Invaild Data", R.drawable.ic_invalid_data);
                             }
                         }


                         @Override
                         public void onFailure(Call<List<PreviousOrders>> call, Throwable t) {
                            // updateViews(getString(R.string.error_connection_request_failed), R.drawable.ic_connection_break);
                             //   Log.e(TAG, t.toString());
                             progressDialog.dismiss();
                         }
                     }
        );
    }
*/

   private void getPreviousOrders(){

       previousOrdersList.clear();

       if (!NetworkConnection.isConnected(activity)) {
           //updateViews("No internet connection",R.drawable.dog6);
           Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();
           updateViews("No Internet Connection", R.drawable.moneky4);
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

                               PreviousOrders item = new PreviousOrders();
                               item.setOrderid(jsonObjec.getInt("orderid"));
                               item.setDate(jsonObjec.getString("date"));

                               previousOrdersList.add(item);

                              // Collections.reverse(previousOrdersList);


                             //  setRVVegetableSize(previousOrdersList.size());

                               previousAdaper.notifyDataSetChanged();
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
               params.put("request", "orderlist");
               //params.put("category", "Vegetables");
               params.put("customerid", customerid);

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
    private void updateViews(String message, int drawableID) {

        // Stop refresh animation
        //     swipeRefreshLayout.setRefreshing(false);
        //   swipeRefreshLayout.setRefreshing(false);
        //adapter.notifyDataSetChanged();


        if (previousOrdersList.isEmpty()) {


            tvEmptyMessage.setText(message);
            ivEmptyImage.setImageResource(drawableID);
            llEmptyState.setVisibility(View.VISIBLE);
        } else {
            llEmptyState.setVisibility(View.GONE);
        }
    }

}
