package com.app.nitro.domain.usecase.base

import com.app.nitro.domain.model.ApiError

interface UseCaseResponse<Type> {

    fun onSuccess(result: Type)

    fun onError(apiError: ApiError?)
}

