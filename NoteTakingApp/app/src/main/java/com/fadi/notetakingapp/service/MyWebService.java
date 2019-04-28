package com.fadi.notetakingapp.service;

import com.fadi.notetakingapp.model.DataItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface MyWebService {

// retrofil uses HttpOK in the background but it reduces the code significantly
    String BASE_URL = "http://560057.youcanlearnit.net/";
    String REST_URL = "services/json/itemsfeed.php";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //
    @GET(REST_URL)
    Call<DataItem[]> dataItems();

}
