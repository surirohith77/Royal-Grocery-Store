package com.rs.royalgrocerystore.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Utils.ApplicationRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatEditText etName, etEmail, etMobile, etPassword;
    String name, email, mobile, password;
    Button btnRegister;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize() {

        etName = findViewById(R.id.etNamee);
        etEmail = findViewById(R.id.etEmaill);
        etMobile = findViewById(R.id.etMobilee);
        etPassword = findViewById(R.id.etPasswordd);

        tvLogin = findViewById(R.id.tvLogin);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnRegister:
               getviews();
             //   startActivity(new Intent(this,OtpVerifyActivity.class));
                break;

            case R.id.tvLogin:
               startActivity(new Intent(this,LoginActivity.class));
                //   startActivity(new Intent(this,OtpVerifyActivity.class));
                break;
        }
    }

    private void getviews() {

        name = Objects.requireNonNull(etName.getText()).toString().trim();
        email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        mobile = Objects.requireNonNull(etMobile.getText()).toString().trim();
        password = Objects.requireNonNull(etPassword.getText()).toString().trim();

        if (name.isEmpty()){

            etName.requestFocus();
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()){

            etEmail.requestFocus();
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mobile.isEmpty()){

            etMobile.requestFocus();
            Toast.makeText(this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()){

            etPassword.requestFocus();
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        sendHttpsRequest();
    }


    private void sendHttpsRequest() {

        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

      /*  if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }
*/

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
                           String otpR = object.getString("otp");

                                etName.setText("");
                                etMobile.setText("");
                                etEmail.setText("");
                                etPassword.setText("");

                                Toast.makeText(RegisterActivity.this, "OTP Sent to "+emailR, Toast.LENGTH_SHORT).show();

                                onSuccess(nameR,mobileR,emailR,otpR);
                            }

                       else {

                           Toast.makeText(RegisterActivity.this, "Already registered with "+email, Toast.LENGTH_SHORT).show();
                       }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(RegisterActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(RegisterActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "OTP");
                params.put("name", name);
                params.put("mobile", mobile);
                params.put("email", email);
                //params.put("password", password);

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

        ApplicationRequest.getInstance(RegisterActivity.this).addToRequestQueue(jsonRequest);
    }

    private void onSuccess(String nameR, String mobileR, String emailR, String otpR) {

        Intent intent = new Intent(this,OtpVerifyActivity.class);
        intent.putExtra("nameR",nameR);
        intent.putExtra("mobileR",mobileR);
        intent.putExtra("emailR",emailR);
        intent.putExtra("otpR",otpR);
        intent.putExtra("password",password);
        startActivity(intent);

    }

}