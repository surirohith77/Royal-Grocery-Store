package com.rs.royalgrocerystore.Data;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

      /*  OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();*/
      OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

      /*  URL url = new URL("https://wikipedia.org");
        URLConnection urlConnection = url.openConnection();
        InputStream in = urlConnection.getInputStream();
        copyInputStreamToOutputStream(in, System.out);*/

        if (retrofit == null){
            retrofit = new Retrofit.Builder()

                    .baseUrl("https://www.royalgrocerystore.com/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
