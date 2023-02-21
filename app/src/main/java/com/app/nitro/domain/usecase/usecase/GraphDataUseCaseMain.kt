package com.app.nitro.domain.usecase.usecase

import com.app.nitro.domain.exception.traceErrorException
import com.app.nitro.domain.usecase.base.UseCaseResponse
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

abstract class GraphDataUseCaseMain<Type, in Params>() where Type : Any {

    abstract suspend fun getSensorAnalyticsGraph(dui: String? = null,params: String? = null,token: String?=null): Type
    fun getSensorAnalyticsGraph(
        scope: CoroutineScope,
        dui:String?=null , params: String? = null,token:String?=null,
        onResult: UseCaseResponse<Type>?
    ) {
        scope.launch {
            try {
                val result = getSensorAnalyticsGraph(dui,params,token)
                onResult?.onSuccess(result)
            } catch (e: CancellationException) {
                e.printStackTrace()
                onResult?.onError(traceErrorException(e))
            } catch (e: Exception) {
                e.printStackTrace()
                onResult?.onError(traceErrorException(e))
            }
        }
    }

}