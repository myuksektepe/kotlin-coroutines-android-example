package com.muratlakodla.kotlin_coroutines.retrofit

import com.muratlakodla.kotlin_coroutines.model.TickerModel
import retrofit2.http.GET

interface RetrofitApi {
    @GET("ticker/24hr")
    suspend fun getTickers(): MutableList<TickerModel>
}