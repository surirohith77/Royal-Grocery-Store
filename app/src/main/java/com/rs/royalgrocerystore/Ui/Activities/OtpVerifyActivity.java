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

public class OtpVerifyActivity extends AppCompatActivity {

    String nameR, mobileR, emailR, otpR, password;
    TextView tvVerifyEmailDesc;
    AppCompatEditText etOtpp;
    Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){

            nameR =   bundle.getString("nameR");
            mobileR =   bundle.getString("mobileR");
            emailR =   bundle.getString("emailR");
            otpR =   bundle.getString("otpR");
            password =   bundle.getString("password");

        }

        initialize();
    }

    private void initialize() {

        tvVerifyEmailDesc = findViewById(R.id.tvVerifyEmailDesc);
        etOtpp = findViewById(R.id.etOtpp);
        btnVerify = findViewById(R.id.btnVerify);

        tvVerifyEmailDesc.setText("Please enter the 4 digit verification code we sent to "+emailR);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getviews();
            }
        });



    }

    private void getviews() {

        String otp = Objects.requireNonNull(etOtpp.getText()).toString().trim();

        if (!otp.equals(otpR)){

            etOtpp.requestFocus();
            Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        sendHttpsRequest();
    }


    private void sendHttpsRequest() {

        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(OtpVerifyActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

      /*  if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }
*/

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

                              /*  String nameR = object.getString("name");
                                String mobileR = object.getString("mobile");
                                String emailR = object.getString("email");
                                String otpR = object.getString("otp");*/

                                Toast.makeText(OtpVerifyActivity.this, "You are registered successfully with "+emailR, Toast.LENGTH_SHORT).show();

                              startActivity(new Intent(OtpVerifyActivity.this,LoginActivity.class));
                              finish();
                               // onSuccess(nameR,mobileR,emailR,otpR);
                            }

                            else {

                                Toast.makeText(OtpVerifyActivity.this, "All ready registered", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // onFailed(2);
                            Toast.makeText(OtpVerifyActivity.this, "Invalid response from the server", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  onFailed(3);
                        Toast.makeText(OtpVerifyActivity.this, "Problem there in server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put("request", "Registration");
                params.put("name", nameR);
                params.put("mobile", mobileR);
                params.put("email", emailR);
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
}