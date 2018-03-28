package com.apptriangle.pos.Login.restInterface;


import com.apptriangle.pos.Login.response.LoginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface LoginService {


    @GET("values/login")
    Call<LoginResponse> login(@Query("userId") String userId, @Query("pswd") String pswd);
}