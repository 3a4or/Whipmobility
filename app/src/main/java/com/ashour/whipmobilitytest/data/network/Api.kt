package com.ashour.whipmobilitytest.data.network

import com.ashour.whipmobilitytest.data.entitities.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("mock?")
    suspend fun getChartsData(@Query("scope") scope: String): Response<BaseResponse>
}