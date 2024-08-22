package com.aman.tomtomimplementation.retro

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.concurrent.TimeUnit

class RetrofitClass {
    lateinit var  apiInterface: ApiInterface
    private lateinit var retrofit: Retrofit

    fun getApiClient(): ApiInterface {
        if (!this::retrofit.isInitialized) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.tomtom.com/search/2/")
                .client(
                    OkHttpClient().newBuilder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(httpLoggingInterceptor)
                        .build()
                )
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }
        apiInterface =  retrofit.create(ApiInterface::class.java)
        return apiInterface
    }
}


interface ApiInterface {

    @GET("nearbySearch/.json")
    fun getRestaurants(@QueryMap map: HashMap<String, Any>): Call<PlacesApiResponse>
}