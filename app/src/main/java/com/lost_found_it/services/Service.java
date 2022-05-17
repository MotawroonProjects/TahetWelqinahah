package com.lost_found_it.services;


import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.CategoryDataModel;
import com.lost_found_it.model.HomeDataModel;
import com.lost_found_it.model.SingleAdModel;
import com.lost_found_it.model.StatusResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("api/profile/myAds")
    Single<Response<AdsDataModel>> getMyAds(@Header("Authorization") String Authorization,
                                            @Query("country") String country);

    @GET("api/home/getAds")
    Single<Response<AdsDataModel>> search(@Query("country") String country,
                                          @Query("search") String search
    );

    @FormUrlEncoded
    @POST("api/profile/deleteAd")
    Single<Response<StatusResponse>> deleteAdd(@Header("Authorization") String Authorization,
                                               @Query("country") String country,
                                               @Field("ad_id") String ad_id);

}