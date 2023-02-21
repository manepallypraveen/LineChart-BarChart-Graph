package com.app.nitro.di

import com.app.nitro.domain.model.*
import com.app.nitro.domain.usecase.models.*
import com.app.nitro.util.API
import retrofit2.http.*


interface ApiService {

    @GET(API.getSensorAnalyticsGraph + "/{deviceEui}/{key}")
    suspend fun getSensorAnalyticsGraph(
        @Path("deviceEui") deviceEui: String,
        @Path("key") key: String,
        @Header("Authorization") token: String?
    ): GraphDataModel

}



