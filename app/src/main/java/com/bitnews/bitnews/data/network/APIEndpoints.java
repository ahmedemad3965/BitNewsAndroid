package com.bitnews.bitnews.data.network;

import com.bitnews.bitnews.data.models.AuthToken;
import com.bitnews.bitnews.data.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIEndpoints {

    @FormUrlEncoded
    @POST("users/signup/")
    Call<User> signUp(@Field("first_name") String firstName,
                      @Field("last_name") String lastName,
                      @Field("username") String userName,
                      @Field("password") String password);

    @FormUrlEncoded
    @POST("users/signup/")
    Call<User> sinUpAsGuest(@Field("guest") boolean isGuest);

    @FormUrlEncoded
    @POST("users/token/")
    Call<AuthToken> logIn(@Field("username") String username,
                          @Field("password") String password);

    @FormUrlEncoded
    @POST("users/token/")
    Call<AuthToken> logIn(@Field("username") String username);

    @GET("users/me/")
    Call<User> getCurrentUser();
}
