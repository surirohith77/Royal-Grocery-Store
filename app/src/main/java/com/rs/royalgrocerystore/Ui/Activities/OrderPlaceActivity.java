package com.rs.royalgrocerystore.Ui.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rs.royalgrocerystore.Internet.NetworkConnection;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Utils.ApplicationRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderPlaceActivity extends AppCompatActivity {

    AppCompatEditText etAddresss, etMandall, etDistrictt, etStatee;
    Button btnContinue;
   String address, mandal, district, state;
    String cid;
    Double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){

            total =  bundle.getDouble("total");
        }

        SharedPreferences sharedPref = getSharedPreferences(
                "royal_grocery_store", Context.MODE_PRIVATE);

        cid = sharedPref.getString("id", "");

            initalize();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Total Bill : \u20B9" +total);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initalize() {

        etAddresss = findViewById(R.id.etAddresss);
        etMandall = findViewById(R.id.etMandall);
        etDistrictt = findViewById(R.id.etDistrictt);
        etStatee = findViewById(R.id.etStatee);

        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



      getViews();
              //  sendhttprequest();

            }
        });

    }

    private void getViews() {

        address =      Objects.requireNonNull(etAddresss.getText()).toString().trim();
        mandal =      Objects.requireNonNull(etMandall.getText()).toString().trim();
        district =      Objects.requireNonNull(etDistrictt.getText()).toString().trim();
        state =      Objects.requireNonNull(etStatee.getText()).toString().trim();

        if (address.isEmpty()){
            Toast.makeText(this, "Please enter address", Toast.LENGTH_LONG).show();
            etAddresss.requestFocus();
            return;
        }


        if (mandal.isEmpty()){
            Toast.makeText(this, "Please enter mandal", Toast.LENGTH_LONG).show();
            etMandall.requestFocus();
            return;
        }


        if (district.isEmpty()){
            Toast.makeText(this, "Please enter district", Toast.LENGTH_LONG).show();
            etDistrictt.requestFocus();
            return;
        }


        if (state.isEmpty()){
            Toast.makeText(this, "Please enter state", Toast.LENGTH_LONG).show();
            etStatee.requestFocus();
            return;
        }


        if (!state.toLowerCase().equals("telangana")){
            Toast.makeText(this, "Currently only available in Telangana", Toast.LENGTH_LONG).show();
            etStatee.requestFocus();
            return;
        }

        String fullAddress = address + " " + mandal + " " + district + " " + state;
        Intent intent = new Intent(OrderPlaceActivity.this,SelectPaymentActivity.class);
        intent.putExtra("address",fullAddress);
        intent.putExtra("total",total);
        startActivity(intent);



    }

    private void sendhttprequest() {


        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(this)) {
            //updateViews("No internet connection",R.drawable.dog6);
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

                                // Toast.makeText(activity, "Login Success ", Toast.LENGTH_SHORT).show();
                                Toast.makeText(OrderPlaceActivity.this, "Order Placed Successfully ", Toast.LENGTH_SHORT).show();
                            }

                            else {

                                Toast.makeText(OrderPlaceActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(OrderPlaceActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(OrderPlaceActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "placeorder");
                params.put("address", address);
                params.put("customerid", cid);
                params.put("cashmode", "cod");

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