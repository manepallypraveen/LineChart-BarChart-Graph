package com.app.nitro.data.di.module

import com.app.nitro.domain.usecase.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {

    single { createPostRepository(get()) }


    viewModel { GraphDataViewModel(get()) }
    single { createGraphDataUseCase(get()) }




}
