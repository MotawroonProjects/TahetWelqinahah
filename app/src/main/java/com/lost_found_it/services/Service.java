package com.lost_found_it.services;


import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.CategoryDataModel;
import com.lost_found_it.model.HomeDataModel;
import com.lost_found_it.model.SingleAdModel;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("api/home/index")
    Single<Response<HomeDataModel>> getHomeData(@Query("country") String country);

    @GET("api/home/getAds")
    Single<Response<AdsDataModel>> getAds(@Query("country") String country,
                                          @Query("category_id") String category_id,
                                          @Query("sub_category_id") String sub_category_id,
                                          @Query("type") String type,
                                          @Query("place_type") String place_type,
                                          @Query("search") String search
    );

    @GET("api/home/getCategories")
    Single<Response<CategoryDataModel>> getCategoryWithSubCategory(@Query("country") String country
    );

    @GET("api/home/getOneAd")
    Single<Response<SingleAdModel>> getAdDetails(@Query("country") String country,
                                              @Query("ad_id") String ad_id,
                                              @Query("phone_key") String phone_key
    );

}