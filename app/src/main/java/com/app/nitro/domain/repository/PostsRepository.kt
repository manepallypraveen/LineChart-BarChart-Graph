package com.app.nitro.domain.repository

import com.app.nitro.domain.usecase.models.*

interface PostsRepository {

    suspend fun getSensorAnalyticsGraph(
        deviceEui: String?,
        key: String?,
        token: String?
    ): GraphDataModel

}