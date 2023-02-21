package com.app.nitro.di

import com.app.nitro.di.ApiService
import com.app.nitro.domain.repository.PostsRepository
import com.app.nitro.domain.usecase.models.*

class PostsRepositoryImp(private val apiService: ApiService) : PostsRepository {


    override suspend fun getSensorAnalyticsGraph(
        deviceEui: String?,
        key: String?,
        token: String?
    ): GraphDataModel {
        return apiService.getSensorAnalyticsGraph(deviceEui!!, key!!, token)
    }



}


