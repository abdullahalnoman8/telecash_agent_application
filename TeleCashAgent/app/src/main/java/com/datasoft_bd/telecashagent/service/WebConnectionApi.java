package com.datasoft_bd.telecashagent.service;

import com.datasoft.mfs.commons.model.Transactions;
import com.datasoft_bd.telecashagent.model.UserDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Masum on 28-Jul-2019
 */
public interface WebConnectionApi {
    String AUTH_URL = "http://192.168.1.126:8085/";
    String BASE_API_URL = "http://192.168.1.126:8083/";


//    @Query() use when need to send null parameter value

    @GET("userAccount/currentBalance")
    Call<Double> getCurrentBalance(@Query("id") int id);

    @GET("userAccount/dsrList")
    Call<List<UserDetail>> getDistributorList(@Query("id") int id);

    @POST("transactions/sendRequest")
    Call<String> sendRequest(@Body Transactions transactions);

    @POST("oauth/token")
    @FormUrlEncoded
    Call<Object> accessToken(@Field("grant_type") String grantType, @Field("username") String username,
                             @Field("password") String password, @Header("Authorization") String authorization);
}


