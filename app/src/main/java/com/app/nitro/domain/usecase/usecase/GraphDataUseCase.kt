package com.app.nitro.domain.usecase.usecase

import com.app.nitro.domain.repository.PostsRepository
import com.app.nitro.domain.usecase.models.*


class GraphDataUseCase constructor(
    private val postsRepository: PostsRepository
) : GraphDataUseCaseMain<GraphDataModel, String>() {
    override suspend fun getSensorAnalyticsGraph(
        deviceEui:String?,key: String?,token:String?
    ): GraphDataModel {
        return postsRepository.getSensorAnalyticsGraph(deviceEui,key,token)
    }
}

