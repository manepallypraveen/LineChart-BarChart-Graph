package com.app.nitro.domain.usecase.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.nitro.domain.model.ApiError
import com.app.nitro.domain.usecase.base.UseCaseResponse
import com.app.nitro.domain.usecase.models.*
import com.app.nitro.domain.usecase.usecase.GraphDataUseCase
import kotlinx.coroutines.cancel

class GraphDataViewModel constructor(private val graphDataUseCase: GraphDataUseCase) :
    ViewModel() {

    val graphDataModel = MutableLiveData<GraphDataModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun getSensorAnalyticsGraph(deviceEui: String?,key: String?,token: String?) {

        showProgressbar.value = true
        graphDataUseCase.getSensorAnalyticsGraph(
            viewModelScope,deviceEui, key,token,
            object : UseCaseResponse<GraphDataModel> {
                override fun onSuccess(result: GraphDataModel) {
                    Log.i(TAG, "result: $result")
                    graphDataModel.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            },
        )
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    companion object {
        private val TAG = GraphDataViewModel::class.java.name
    }

}
