package com.jldemos.roomdb_demo

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class BoredApiService {


    data class BoredResult(
        @SerializedName("activity")
        val description: String = "",
        @SerializedName("accessibility")
        val accessibility: Double = 0.0,
        @SerializedName("type")
        val type: String = "",
        @SerializedName("participants")
        val participants: Int = 0,
//        @SerializedName("price") Invalid api response, some are int and some are double causing crashes
//        val price: Double = 0.0,
        @SerializedName("link")
        val link: String,
        @SerializedName("key")
        val key: String = ""
    )

    interface ApiService {

        //Get Random result
        @GET("activity/")
        fun pullAnotherSuggestion(): Observable<BoredResult>

        @GET("activity/type")
        fun pullByType(
            @Query(value = "type") type: String = "recreational"
        ) : Observable<BoredResult>

        companion object {
            private const val baseUrl = "http://www.boredapi.com/api/"

            fun create(): ApiService {
                val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build()
                return retrofit.create(ApiService::class.java)
            }
        }
    }
}