package com.lost_found_it.services;


import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.CategoryDataModel;
import com.lost_found_it.model.HomeDataModel;
import com.lost_found_it.model.MessagesDataModel;
import com.lost_found_it.model.RoomDataModel;
import com.lost_found_it.model.SingleAd;
import com.lost_found_it.model.SettingDataModel;
import com.lost_found_it.model.SingleAdModel;
import com.lost_found_it.model.SingleMessageModel;
import com.lost_found_it.model.StatusResponse;
import com.lost_found_it.model.UserModel;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Single<Response<StatusResponse>> deleteAd(@Header("Authorization") String Authorization,
                                              @Field("country") String country,
                                              @Field("ad_id") String ad_id);

    @FormUrlEncoded
    @POST("api/auth/login")
    Single<Response<UserModel>> login(@Field("country") String country,
                                      @Field("phone_code") String phone_code,
                                      @Field("phone") String phone
    );

    @Multipart
    @POST("api/auth/register")
    Single<Response<UserModel>> signUp(@Part("country") RequestBody country,
                                       @Part("first_name") RequestBody first_name,
                                       @Part("last_name") RequestBody last_name,
                                       @Part("email") RequestBody email,
                                       @Part("phone_code") RequestBody phone_code,
                                       @Part("phone") RequestBody phone,
                                       @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/auth/updateProfile")
    Single<Response<UserModel>> updateProfile(@Header("Authorization") String Authorization,
                                              @Part("country") RequestBody country,
                                              @Part("first_name") RequestBody first_name,
                                              @Part("last_name") RequestBody last_name,
                                              @Part("email") RequestBody email,
                                              @Part("phone_code") RequestBody phone_code,
                                              @Part("phone") RequestBody phone,
                                              @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("api/auth/insertToken")
    Single<Response<StatusResponse>> updateFireBaseToken(@Header("Authorization") String Authorization,
                                                         @Field("country") String country,
                                                         @Field("token") String token,
                                                         @Field("type") String type

    );

    @FormUrlEncoded
    @POST("api/setting/contactUs")
    Single<Response<StatusResponse>> contactUs(@Field("country") String country,
                                               @Field("name") String name,
                                               @Field("email") String email,
                                               @Field("phone") String phone,
                                               @Field("text") String text);

    @GET("api/setting/index")
    Single<Response<SettingDataModel>> getSettings(@Query("country") String country);

    @Multipart
    @POST("api/profile/addAd")
    Single<Response<SingleAd>> addAds(@Header("Authorization") String Authorization,
                                      @Part("country") RequestBody country,
                                      @Part("title") RequestBody title,
                                      @Part("description") RequestBody description,
                                      @Part("category_id") RequestBody category_id,
                                      @Part("sub_category_id") RequestBody sub_category_id,
                                      @Part("address") RequestBody address,
                                      @Part("latitude") RequestBody latitude,
                                      @Part("longitude") RequestBody longitude,
                                      @Part("whatsapp") RequestBody whatsapp,
                                      @Part("phone_code") RequestBody phone_code,
                                      @Part("phone") RequestBody phone,
                                      @Part("type") RequestBody type,
                                      @Part("place_type") RequestBody place_type,
                                      @Part List<MultipartBody.Part> images
    );

    @Multipart
    @POST("api/profile/updateAd")
    Single<Response<SingleAd>> updateAds(@Header("Authorization") String Authorization,
                                         @Part("country") RequestBody country,
                                         @Part("ad_id") RequestBody ad_id,
                                         @Part("title") RequestBody title,
                                         @Part("description") RequestBody description,
                                         @Part("category_id") RequestBody category_id,
                                         @Part("sub_category_id") RequestBody sub_category_id,
                                         @Part("address") RequestBody address,
                                         @Part("latitude") RequestBody latitude,
                                         @Part("longitude") RequestBody longitude,
                                         @Part("whatsapp") RequestBody whatsapp,
                                         @Part("phone_code") RequestBody phone_code,
                                         @Part("phone") RequestBody phone,
                                         @Part("type") RequestBody type,
                                         @Part("place_type") RequestBody place_type,
                                         @Part List<MultipartBody.Part> images
    );

    @FormUrlEncoded
    @POST("api/auth/logout")
    Single<Response<StatusResponse>> logout(@Header("Authorization") String Authorization,
                                            @Field("country") String country,
                                            @Field("token") String token);

    @GET("api/home/follow-delete-Ad")
    Single<Response<StatusResponse>> followUnFollow(@Header("Authorization") String Authorization,
                                                    @Query("country") String country,
                                                    @Query("ad_id") String ad_id
    );

    @GET("api/chat/myRooms")
    Single<Response<RoomDataModel>> getRoom(@Header("Authorization") String Authorization,
                                            @Query("country") String country);

    @GET("api/chat/index")
    Single<Response<MessagesDataModel>> getChatMessages(@Header("Authorization") String Authorization,
                                                        @Query("country") String country,
                                                        @Query("ad_id") String ad_id,
                                                        @Query("room_id") String room_id);

    @Multipart
    @POST("api/chat/sendMessage")
    Single<Response<SingleMessageModel>> sendMessages(@Header("Authorization") String Authorization,
                                                      @Part("country") RequestBody country,
                                                      @Part("ad_id") RequestBody ad_id,
                                                      @Part("room_id") RequestBody room_id,
                                                      @Part("type") RequestBody type,
                                                      @Part("message") RequestBody message,
                                                      @Part MultipartBody.Part image
    );
}