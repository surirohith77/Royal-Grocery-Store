package com.rs.royalgrocerystore.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rs.royalgrocerystore.Internet.NetworkConnection;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Utils.ApplicationRequest;
import com.rs.royalgrocerystore.sharedPreference.shared_preference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatEditText etEmail, etPassword;
    String email, password;
    Button btnLogin;
    shared_preference sp;
    TextView tvForgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = new shared_preference(this);

        if (sp.readloginstatus()){

                startActivity(new Intent(this, MainActivity.class));
                finish();

        }


            initalize();
    }

    private void initalize() {

        etEmail = findViewById(R.id.etEmaill);
        etPassword = findViewById(R.id.etPasswordd);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

    }

    public void GotoRegister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnLogin:
                getviews();
                break;

            case R.id.tvForgotPassword:
              // startActivity(new Intent(this,ForgotPassswordActivity.class));
                break;
        }
    }

    private void getviews() {

        email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        password = Objects.requireNonNull(etPassword.getText()).toString().trim();

        if (email.isEmpty()){

            etEmail.requestFocus();
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()){

            etPassword.requestFocus();
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        sendHttpsRequest();

    }

    private void sendHttpsRequest() {

        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(this)) {
          //  updateViews("No internet connection",R.drawable.dog6);
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
      //  String url = "http://kga.bterp.in/Mobileservices.aspx?";
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


                                String nameR = object.getString("name");
                                String mobileR = object.getString("mobile");
                                String emailR = object.getString("email");
                                String idR = object.getString("id");

                                etEmail.setText("");
                                etPassword.setText("");

                                Toast.makeText(LoginActivity.this, "Login Success "+emailR, Toast.LENGTH_SHORT).show();

                                onSuccess(nameR,mobileR,emailR,idR);
                            }

                            else {

                                Toast.makeText(LoginActivity.this, "Incorrect Email or Password ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(LoginActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(LoginActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "login");
                params.put("mobile", email);
                params.put("password", password);

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

    private void onSuccess(String nameR, String mobileR, String emailR, String idR) {

        sp.WriteLoginStatus(true);

        setpreference(nameR,mobileR,emailR,idR);

       // startActivity(new Intent(this,MainActivity.class));
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void setpreference(String nameR, String mobileR, String emailR, String idR) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                "royal_grocery_store", Context.MODE_PRIVATE);

        sharedPref.edit().putString("name", nameR).apply();
        sharedPref.edit().putString("id", idR).apply();
        sharedPref.edit().putString("mobile", mobileR).apply();
        sharedPref.edit().putString("email", emailR).apply();

    }


}