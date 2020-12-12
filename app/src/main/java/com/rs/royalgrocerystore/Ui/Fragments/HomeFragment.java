package com.rs.royalgrocerystore.Ui.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rs.royalgrocerystore.Adapter.CategoriesIconAdapter;
import com.rs.royalgrocerystore.Adapter.SmarateistSliderAdapter;
import com.rs.royalgrocerystore.Data.ApiClient;
import com.rs.royalgrocerystore.Data.ApiDao;
import com.rs.royalgrocerystore.Internet.NetworkConnection;
import com.rs.royalgrocerystore.Listener.RvListener;
import com.rs.royalgrocerystore.Model.CategoryIcon;
import com.rs.royalgrocerystore.Model.Products;
import com.rs.royalgrocerystore.R;
import com.rs.royalgrocerystore.Ui.Activities.CategoryWiseActivity;
import com.rs.royalgrocerystore.Ui.Activities.IconCategoryViewAllActivity;
import com.rs.royalgrocerystore.Ui.Activities.MainActivity;
import com.rs.royalgrocerystore.Utils.ApplicationRequest;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements RvListener, SwipeRefreshLayout.OnRefreshListener{

    Activity activity;
    View view;

    SliderView sliderView;
    private SmarateistSliderAdapter smarateistSliderAdapter;
    ArrayList sliderLIst;

    RecyclerView rvCategoryIcon;
    CategoriesIconAdapter categoriesIconAdapter;
    ArrayList categoryIconList;

    TextView tvViewAll;

    private static final String TAG = HomeFragment.class.getSimpleName();
    private List<Products> groceryList, vegetableList, bannerList, nonVegList, softDrinkList;
    private ProductAdapter groceryAdapter, vegetableAdapter;
    private ProductAdapter2 nonVegAdapter, softdrinkAdapter;

    private TextView tvEmptyMessage;
    private ImageView ivEmptyImage;
    private LinearLayout llEmptyState;

    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView rvGrocery, rvVegetables, rvNonVeg, rv_softdrinks;
    String customerid;

    TextView tvViewAllGroceries, tvViewAllNonveg, tvViewAllVegetable, tvViewAllSoftDrink;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment,container,false);


        SharedPreferences sharedPref = activity.getSharedPreferences(
                "royal_grocery_store", Context.MODE_PRIVATE);

        customerid = sharedPref.getString("id", "");

        initializeView();
        intializeSLiderData();


        intializeCategoryIconRecyclerview();
        initializeRvCategories();

        initializeRecyclerViewGrocery();
        getGroceries();

        initializeRecyclerViewVegetables();
  getVegetables();

        initializeRecyclerViewNonVeg();
    getNonVeg();

        initializeRecyclerViewSoftDrinks();
       getsoftDrinks();
        return view;
    }

    private void intializeSLiderData() {

        sliderView = view.findViewById(R.id.imageSlider);

        smarateistSliderAdapter = new SmarateistSliderAdapter(activity);
        sliderView.setSliderAdapter(smarateistSliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
       // sliderLIst = new ArrayList();

        bannerList = new ArrayList<>();

        getBanners();

       /* sliderLIst.add(new ImageSlider("grocery1",R.drawable.grocery1));
        sliderLIst.add(new ImageSlider("grocery2",R.drawable.grocery2));
        sliderLIst.add(new ImageSlider("grocery3",R.drawable.grocery3));
        sliderLIst.add(new ImageSlider("grocery4",R.drawable.grocery6));*/

        smarateistSliderAdapter.renewItems(bannerList);
    }


    private void intializeCategoryIconRecyclerview() {

        rvCategoryIcon = view.findViewById(R.id.rvCategoryIcon);
        rvCategoryIcon.setHasFixedSize(true);
        //rvFeatures.setLayoutManager(new LinearLayoutManager(activity));
        rvCategoryIcon.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        // GridLayoutManager manager = new GridLayoutManager(activity,2);
        GridLayoutManager layoutManager =
                new GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false);
        rvCategoryIcon.setLayoutManager(layoutManager);

    }

    private void initializeRvCategories() {


        categoryIconList = new ArrayList();

        categoryIconList.add(new CategoryIcon("Grocery",R.drawable.grocery));
        categoryIconList.add(new CategoryIcon("Vegetables",R.drawable.vegetable));
        categoryIconList.add(new CategoryIcon("Non Veg",R.drawable.non_veg));
        categoryIconList.add(new CategoryIcon("Soft Drinks",R.drawable.soft_drink2));




     /*   iconFoods.add(new iconslider(R.drawable.cake,"Great Offers"));
        iconFoods.add(new iconslider(R.drawable.food1,"Express Delivery"));
        iconFoods.add(new iconslider(R.drawable.food2,"Rohith Suri"));*/
        categoriesIconAdapter = new CategoriesIconAdapter(categoryIconList,this);
        rvCategoryIcon.setAdapter(categoriesIconAdapter);


    }

    private void initializeView() {

        tvViewAll = view.findViewById(R.id.tvViewAll);
        tvViewAllGroceries = view.findViewById(R.id.tvViewAllGroceries);
        tvViewAllNonveg = view.findViewById(R.id.tvViewAllNonveg);
        tvViewAllVegetable = view.findViewById(R.id.tvViewAllVegetable);
        tvViewAllSoftDrink = view.findViewById(R.id.tvViewAllSoftDrink);

        tvViewAll.setText(Html.fromHtml("<u>View All</u></font>"));
        tvViewAllGroceries.setText(Html.fromHtml("<u>View All</u></font>"));
        tvViewAllNonveg.setText(Html.fromHtml("<u>View All</u></font>"));
        tvViewAllVegetable.setText(Html.fromHtml("<u>View All</u></font>"));
        tvViewAllSoftDrink.setText(Html.fromHtml("<u>View All</u></font>"));



        tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, IconCategoryViewAllActivity.class);
                startActivity(intent);
            }
        });


        tvViewAllGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, CategoryWiseActivity.class);
                intent.putExtra("request","Grocery");
               startActivity(intent);
            }
        });

        tvViewAllNonveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, CategoryWiseActivity.class);
                intent.putExtra("request","Non Veg");
                startActivity(intent);
            }
        });

        tvViewAllVegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, CategoryWiseActivity.class);
                intent.putExtra("request","Vegetables");
                startActivity(intent);
            }
        });

        tvViewAllSoftDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, CategoryWiseActivity.class);
                intent.putExtra("request","Soft Drinks");
                startActivity(intent);
            }
        });





        tvEmptyMessage = view.findViewById(R.id.list_tv_empty_message);
        ivEmptyImage =  view.findViewById(R.id.list_iv_empty_image);
        llEmptyState =  view.findViewById(R.id.list_ll_empty_state);

        /*swipeRefreshLayout =  view.findViewById(R.id.list_swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorGrey));
        swipeRefreshLayout.setOnRefreshListener(this);*/

    }


    private void initializeRecyclerViewGrocery() {

        rvGrocery = view.findViewById(R.id.rv_groceries);
        rvGrocery.setHasFixedSize(true);
        rvGrocery.setLayoutManager(new LinearLayoutManager(activity));
/*
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);

        rvRequest.addItemDecoration(decoration);
*/

        groceryList = new ArrayList<>();
  /*  groceryAdapter = new ProductAdapter(groceryList,this);
        rvGrocery.setAdapter(groceryAdapter);*/


    }


    private void initializeRecyclerViewVegetables() {

        rvVegetables = view.findViewById(R.id.rv_vegetables);
        rvVegetables.setHasFixedSize(true);
        rvVegetables.setLayoutManager(new LinearLayoutManager(activity));
/*
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);

        rvRequest.addItemDecoration(decoration);
*/

        vegetableList = new ArrayList<>();
    /* vegetableAdapter = new ProductAdapter(vegetableList,this);
        rvVegetables.setAdapter(vegetableAdapter);*/


    }

    private void initializeRecyclerViewNonVeg() {

        rvNonVeg = view.findViewById(R.id.rv_nonveg);
        rvNonVeg.setHasFixedSize(true);
       // rvNonVeg.setLayoutManager(new LinearLayoutManager(activity));
        GridLayoutManager manager = new GridLayoutManager(activity,2);
        rvNonVeg.setLayoutManager(manager);
/*
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);

        rvRequest.addItemDecoration(decoration);
*/

        nonVegList = new ArrayList<>();
 /*    nonVegAdapter = new ProductAdapter2(nonVegList,this);
        rvNonVeg.setAdapter(nonVegAdapter);
*/

    }

    private void initializeRecyclerViewSoftDrinks() {

        rv_softdrinks = view.findViewById(R.id.rv_softdrinks);
        rv_softdrinks.setHasFixedSize(true);
        // rvNonVeg.setLayoutManager(new LinearLayoutManager(activity));
        GridLayoutManager manager = new GridLayoutManager(activity,2);
        rv_softdrinks.setLayoutManager(manager);
/*
        rvRequest.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(
                activity, LinearLayoutManager.VERTICAL);

        rvRequest.addItemDecoration(decoration);
*/

        softDrinkList = new ArrayList<>();
 /*    nonVegAdapter = new ProductAdapter2(nonVegList,this);
        rvNonVeg.setAdapter(nonVegAdapter);
*/

    }

  /*  private void getBanners() {


        bannerList.clear();

        if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<Products>> call = apiService.getBanners("Banner");
        call.enqueue(new Callback<List<Products>>() {
                         @Override
                         public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                             List<Products> KyclistActivity = response.body();

                             if (KyclistActivity != null) {
                                 bannerList.addAll(KyclistActivity);
                                // Collections.reverse(bannerList);
                             //    groceryAdapter.notifyDataSetChanged();
                                 progressDialog.dismiss();

                               //  updateViews("No refers till Now", R.drawable.moneky4);
                             } else {
                              //   updateViews("Invaild Data", R.drawable.ic_invalid_data);
                             }
                         }


                         @Override
                         public void onFailure(Call<List<Products>> call, Throwable t) {
                             updateViews(getString(R.string.error_connection_request_failed), R.drawable.ic_connection_break);
                             Log.e(TAG, t.toString());
                             progressDialog.dismiss();
                         }
                     }
        );
    }
*/


  private void getBanners(){

      final ProgressDialog progressDialog = new ProgressDialog(activity);
      progressDialog.setMessage("Loading...");
      progressDialog.show();

      if (!NetworkConnection.isConnected(activity)) {
          //updateViews("No internet connection",R.drawable.dog6);
          Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();
          progressDialog.dismiss();
          return;
      }

   //   String url = "http://kga.bterp.in/Mobileservices.aspx?";

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

                              Products item = new Products();
                              item.setImage(jsonObjec.getString("image"));
                              item.setCategory(jsonObjec.getString("category"));

                              bannerList.add(item);
                                smarateistSliderAdapter.notifyDataSetChanged();

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
              params.put("request", "Banner");

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

    /*private void getGroceries() {


        groceryList.clear();

        if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<Products>> call = apiService.getGrocery("Products","Grocery",customerid);
        call.enqueue(new Callback<List<Products>>() {
                         @Override
                         public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                             List<Products> KyclistActivity = response.body();

                             Context context;

                             if (KyclistActivity != null) {

                                 groceryList.addAll(KyclistActivity);

                                 Collections.reverse(groceryList);


                                     setRVGrocerySIze(groceryList.size());

                                 groceryAdapter.notifyDataSetChanged();
                                 progressDialog.dismiss();

                                 updateViews("No refers till Now", R.drawable.moneky4);
                             } else {
                                 updateViews("Invaild Data", R.drawable.ic_invalid_data);
                             }
                         }


                         @Override
                         public void onFailure(Call<List<Products>> call, Throwable t) {
                             updateViews(getString(R.string.error_connection_request_failed), R.drawable.ic_connection_break);
                             Log.e(TAG, t.toString());
                             progressDialog.dismiss();
                         }
                     }
        );
    }*/

    private void getGroceries() {


        groceryList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

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


                                setRVGrocerySIze(groceryList.size());

                                groceryAdapter.notifyDataSetChanged();
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
                params.put("request", "Products");
                params.put("category", "Grocery");
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

    private void setRVGrocerySIze(int size) {

      /*  Map<String, List<Products>> myMap = new ConcurrentHashMap<>();
        myMap.put("groceryList", groceryList);

     // try {

          Iterator<String> it1 = myMap.keySet().iterator();
          while (it1.hasNext()) {
*/
              if (size > 6) {
                  groceryAdapter = new ProductAdapter(groceryList.subList(0, 6), this);

              } else {
                  groceryAdapter = new ProductAdapter(groceryList, this);
              }
        //  }

          rvGrocery.setAdapter(groceryAdapter);
/*
      }catch (ConcurrentModificationException cse){
          cse.printStackTrace();
      }*/
    }


/*
    private void getVegetables() {


        vegetableList.clear();

        if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<Products>> call = apiService.getVegetables("Products","Vegetables",customerid);
        call.enqueue(new Callback<List<Products>>() {
                         @Override
                         public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                             List<Products> KyclistActivity = response.body();

                             if (KyclistActivity != null) {
                                 vegetableList.addAll(KyclistActivity);
                                 Collections.reverse(vegetableList);
                                // setRVVegetableSize();

                                     setRVVegetableSize(vegetableList.size());


                                 vegetableAdapter.notifyDataSetChanged();
                                 progressDialog.dismiss();

                                // updateViews("No refers till Now", R.drawable.moneky4);
                             } else {
                               //  updateViews("Invaild Data", R.drawable.ic_invalid_data);
                             }
                         }


                         @Override
                         public void onFailure(Call<List<Products>> call, Throwable t) {
                           //  updateViews(getString(R.string.error_connection_request_failed), R.drawable.ic_connection_break);
                             Log.e(TAG, t.toString());
                             progressDialog.dismiss();
                         }
                     }
        );
    }

*/


private void getVegetables(){


    vegetableList.clear();

    final ProgressDialog progressDialog = new ProgressDialog(activity);
    progressDialog.setMessage("Loading...");
    progressDialog.show();

    if (!NetworkConnection.isConnected(activity)) {
        updateViews("No internet connection",R.drawable.dog6);
        return;
    }

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

                            Products item = new Products();
                            item.setImage(jsonObjec.getString("image"));
                            item.setProduct(jsonObjec.getString("product"));
                            item.setMRP(jsonObjec.getString("MRP"));
                            item.setRgsprice(jsonObjec.getString("rgsprice"));
                            item.setSave(jsonObjec.getDouble("save"));
                            item.setCartqy(jsonObjec.getInt("cartqy"));
                            item.setCategory(jsonObjec.getString("category"));
                            item.setId(jsonObjec.getInt("id"));

                            vegetableList.add(item);

                            Collections.reverse(vegetableList);


                            setRVVegetableSize(vegetableList.size());

                            vegetableAdapter.notifyDataSetChanged();
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
            params.put("request", "Products");
            params.put("category", "Vegetables");
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


    private void setRVVegetableSize(int size) {

      try {
      if (size>6) {

          vegetableAdapter = new ProductAdapter(vegetableList.subList(0, 6), this);

      }
      else {
          vegetableAdapter = new ProductAdapter(vegetableList,this);
      }
        rvVegetables.setAdapter(vegetableAdapter);


    }catch (ConcurrentModificationException cse){
        cse.printStackTrace();
    }
    }


 /*   private void getNonVeg() {


        nonVegList.clear();

        if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<Products>> call = apiService.getGrocery("Products","Non Veg",customerid);
        call.enqueue(new Callback<List<Products>>() {
                         @Override
                         public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                             List<Products> KyclistActivity = response.body();

                             if (KyclistActivity != null) {

                                 nonVegList.addAll(KyclistActivity);
                                 Collections.reverse(nonVegList);
                               setRvNonVegSize(nonVegList.size());
                                 nonVegAdapter.notifyDataSetChanged();
                                 progressDialog.dismiss();

                                 updateViews("No refers till Now", R.drawable.moneky4);
                             } else {
                                 updateViews("Invaild Data", R.drawable.ic_invalid_data);
                             }
                         }


                         @Override
                         public void onFailure(Call<List<Products>> call, Throwable t) {
                             updateViews(getString(R.string.error_connection_request_failed), R.drawable.ic_connection_break);
                             Log.e(TAG, t.toString());
                             progressDialog.dismiss();
                         }
                     }
        );
    }*/

 private void getNonVeg(){

     nonVegList.clear();

     if (!NetworkConnection.isConnected(activity)) {
         updateViews("No internet connection",R.drawable.dog6);
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

                             Products item = new Products();
                             item.setImage(jsonObjec.getString("image"));
                             item.setProduct(jsonObjec.getString("product"));
                             item.setMRP(jsonObjec.getString("MRP"));
                             item.setRgsprice(jsonObjec.getString("rgsprice"));
                             item.setSave(jsonObjec.getDouble("save"));
                             item.setCartqy(jsonObjec.getInt("cartqy"));
                             item.setCategory(jsonObjec.getString("category"));
                             item.setId(jsonObjec.getInt("id"));

                             nonVegList.add(item);
                             Collections.reverse(nonVegList);
                             setRvNonVegSize(nonVegList.size());
                             nonVegAdapter.notifyDataSetChanged();
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
             params.put("request", "Products");
             params.put("category", "Non Veg");
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

    private void setRvNonVegSize(int size) {

      try {

        if (size>6) {

            nonVegAdapter = new ProductAdapter2(nonVegList.subList(0, 6), this);
        }else {

            nonVegAdapter = new ProductAdapter2(nonVegList,this);
        }
        rvNonVeg.setAdapter(nonVegAdapter);

    }catch (ConcurrentModificationException cse){
        cse.printStackTrace();
    }
    }



    /*private void getsoftDrinks() {


        softDrinkList.clear();

        if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait........");
        progressDialog.show();

        ApiDao apiService =
                ApiClient.getClient().create(ApiDao.class);

        Call<List<Products>> call = apiService.getGrocery("Products","Soft Drinks",customerid);
        call.enqueue(new Callback<List<Products>>() {
                         @Override
                         public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                             List<Products> KyclistActivity = response.body();

                             if (KyclistActivity != null) {
                                 softDrinkList.addAll(KyclistActivity);
                                 Collections.reverse(softDrinkList);
                                 setRvSoftDrinkSize(softDrinkList.size());
                                 softdrinkAdapter.notifyDataSetChanged();
                                 progressDialog.dismiss();

                                 updateViews("No refers till Now", R.drawable.moneky4);
                             } else {
                                 updateViews("No items added till now", R.drawable.ic_invalid_data);
                             }
                         }


                         @Override
                         public void onFailure(Call<List<Products>> call, Throwable t) {
                             updateViews(getString(R.string.error_connection_request_failed), R.drawable.ic_connection_break);
                             Log.e(TAG, t.toString());
                             progressDialog.dismiss();
                         }
                     }
        );
    }
*/

    private void getsoftDrinks() {


        softDrinkList.clear();

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

                                Products item = new Products();
                                item.setImage(jsonObjec.getString("image"));
                                item.setProduct(jsonObjec.getString("product"));
                                item.setMRP(jsonObjec.getString("MRP"));
                                item.setRgsprice(jsonObjec.getString("rgsprice"));
                                item.setSave(jsonObjec.getDouble("save"));
                                item.setCartqy(jsonObjec.getInt("cartqy"));
                                item.setCategory(jsonObjec.getString("category"));
                                item.setId(jsonObjec.getInt("id"));

                                softDrinkList.add(item);
                                Collections.reverse(softDrinkList);
                                setRvSoftDrinkSize(softDrinkList.size());
                                softdrinkAdapter.notifyDataSetChanged();
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
                params.put("request", "Products");
                params.put("category", "Soft Drinks");
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

    private void setRvSoftDrinkSize(int size) {

      try {
        if (size>6) {

            softdrinkAdapter = new ProductAdapter2(softDrinkList.subList(0, 6), this);
        }else {

            softdrinkAdapter = new ProductAdapter2(softDrinkList,this);
        }
        rv_softdrinks.setAdapter(softdrinkAdapter);

    }catch (ConcurrentModificationException cse){
        cse.printStackTrace();
    }
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

    @Override
    public void onRefresh() {
        groceryList.clear();
        //getleadslist();
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MYVIEWHOLDER> {


        List<Products> list;
        RvListener listener;
        Context context;

        public ProductAdapter(List<Products> list, RvListener listener) {
            this.list = list;
            this.listener = listener;
        }

        @NonNull
        @Override
        public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.product_design,parent,false);
            return new MYVIEWHOLDER(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {

            final Products products = list.get(position);

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
        }



        @Override
        public int getItemCount() {

            try {
                return list.size();
            }

            catch(ConcurrentModificationException cse ){
                cse.printStackTrace();
            }

            return list.size();
        }

        class MYVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView tvSave, tvRGSPrice, tvMRP, tvProduct, tvQty;
            ImageView ivItem, ivminus, ivPlus;
            Button btnAddtocart;
            RelativeLayout relativeQty;


            public MYVIEWHOLDER(@NonNull View itemView) {
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
            public void onClick(View view) {
                listener.Rvclick(view,getAdapterPosition());
            }
        }
    }

    private void sendAddToCart(final int id) {


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

        ApplicationRequest.getInstance(activity).addToRequestQueue(jsonRequest);

    }


    private void sendDecreaseQtyRequest(final int id) {


        // itemList.clear();

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if (!NetworkConnection.isConnected(activity)) {
            updateViews("No internet connection",R.drawable.dog6);
            return;
        }
 //       String url = "http://kga.bterp.in/Mobileservices.aspx?";
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

    // calling a funtion from mainactivity from Homefragment
        ((MainActivity)getActivity()).getCartCount();

        getGroceries();
        getVegetables();
        getNonVeg();
        getsoftDrinks();
    }


    // 2nd adapter  starts here

    class ProductAdapter2 extends RecyclerView.Adapter<ProductAdapter2.MYVIEWHOLDER>{

        List<Products> list;
        RvListener listener;
        Context context;

        public ProductAdapter2(List<Products> list, RvListener listener) {
            this.list = list;
            this.listener = listener;
        }

        @NonNull
        @Override
        public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.product_design_rec2,parent,false);
            return new MYVIEWHOLDER(view);

        }

        @Override
        public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {

            final Products products = list.get(position);

            holder.tvMRP.setText("MRP \u20B9 "+products.getMRP());
            holder.tvRGSPrice.setText("RGS Price \u20B9 "+products.getRgsprice());
           // holder.tvSave.setText("Save \u20B9 "+products.getSave());
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


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MYVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvSave, tvRGSPrice, tvMRP, tvProduct, tvQty;
            ImageView ivItem, ivminus, ivPlus;
            Button btnAddtocart;
            RelativeLayout relativeQty;
          public MYVIEWHOLDER(@NonNull View itemView) {
              super(itemView);

              tvMRP = itemView.findViewById(R.id.tvMRP);
              tvProduct = itemView.findViewById(R.id.tvProduct);
            //  tvSave = itemView.findViewById(R.id.tvSave);
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
            public void onClick(View view) {
                listener.Rvclick(view,getAdapterPosition());
            }
        }
    }

   /* @Override
    public void onResume() {
        super.onResume();

        intializeSLiderData();


        getGroceries();
        getVegetables();
        getNonVeg();
        getsoftDrinks();

        ((MainActivity)getActivity()).getCartCount();
    }*/

    
}
