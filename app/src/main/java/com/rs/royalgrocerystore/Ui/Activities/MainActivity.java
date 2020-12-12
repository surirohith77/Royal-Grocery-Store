package com.rs.royalgrocerystore.Ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rs.royalgrocerystore.Internet.NetworkConnection;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Ui.Fragments.CartFragment;
import com.rs.royalgrocerystore.Ui.Fragments.HomeFragment;
import com.rs.royalgrocerystore.Ui.Fragments.ProfileFragment;
import com.rs.royalgrocerystore.Utils.ApplicationRequest;
import com.rs.royalgrocerystore.Utils.BottomNavigationViewBehavior;
import com.rs.royalgrocerystore.Utils.ErrorSnackBar;
import com.rs.royalgrocerystore.sharedPreference.shared_preference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager = getSupportFragmentManager();
    String cid;
    Toolbar toolbar;
    TextView tvBadge;

    private long backPressed;
    private static final long TIME_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences(
                "royal_grocery_store", Context.MODE_PRIVATE);

        cid = sharedPref.getString("id", "");


        Toolbar();

        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.frameLayout);


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        bottomNavigationView.setOnNavigationItemSelectedListener(naviCustoListView);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                    new HomeFragment()).commitAllowingStateLoss();
        }

        BottomNavigationMenuView bottomNavigationMenuView =


                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.cart_count_layout, bottomNavigationMenuView, false);
        tvBadge = badge.findViewById(R.id.notification_badge);

    //    tvBadge.setText("3");
        getCartCount();
        itemView.addView(badge);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }




    private BottomNavigationView.OnNavigationItemSelectedListener naviCustoListView =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        Fragment selectedFragment = null;


                        switch (menuItem.getItemId()){



                            case R.id.home:

                                selectedFragment = new HomeFragment();
                                fragmentManager.beginTransaction().replace(R.id.frameLayout, selectedFragment).commitAllowingStateLoss();

                                break;

                            case R.id.cart:

                                selectedFragment = new CartFragment();
                                fragmentManager.beginTransaction().replace(R.id.frameLayout, selectedFragment).commitAllowingStateLoss();

                                break;

                            case R.id.profile:

                                selectedFragment = new ProfileFragment();
                                fragmentManager.beginTransaction().replace(R.id.frameLayout, selectedFragment).commitAllowingStateLoss();

                             //   startActivity(new Intent(MainActivity3.this, AdvertiseActivity.class));

                                break;




                        }

                        return true;
                    }
                };


    private void Toolbar(){

        toolbar = findViewById(R.id.toolbar);
        final ImageView ic_search = findViewById(R.id.ic_search);
      //toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        TextView tvTool1 = findViewById(R.id.tvTool1);
        tvTool1.setText(R.string.app_name);
        /*final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar();*/
        /*.setDisplayHomeAsUpEnabled(true);*/

        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(MainActivity.this, SearchviewActivity.class));
            }
        });
    }

    public void getCartCount() {


        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(this)) {
            //  updateViews("No internet connection",R.drawable.dog6);
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
     //   String url = "http://kga.bterp.in/Mobileservices.aspx?";
        String url = getString(R.string.url);

        //Again creating the string request
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //   itemList.clear();
                        progressDialog.dismiss();
                        //Log.e(TAG, response);

                        try {

                            JSONObject object = new JSONObject(response);

                            boolean error =      object.getBoolean("error");

                            if (!error) {

                            String count =   object.getString("count");
                                tvBadge.setText(count);
                                //tvBadge.setVisibility(View.VISIBLE);

                                if (count.equals("0")){
                                    tvBadge.setVisibility(View.GONE);
                                }
                                else {
                                    tvBadge.setVisibility(View.VISIBLE);
                                }

                              //  Toast.makeText(MainActivity.this, "Login Success "+emailR, Toast.LENGTH_SHORT).show();
                            }

                            else {

                                tvBadge.setVisibility(View.GONE);
                               // Toast.makeText(MainActivity.this, "Incorrect Email or Password ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(MainActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(MainActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "cartcount");
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

        ApplicationRequest.getInstance(this).addToRequestQueue(jsonRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);

        return true;
    }


    public void Logout(MenuItem item) {
        shared_preference sp = new shared_preference(this);
        sp.WriteLoginStatus(false);
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {

        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
          //  ErrorSnackBar.onBackExit(this, bottomNavigationView);
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCartCount();
    }
}