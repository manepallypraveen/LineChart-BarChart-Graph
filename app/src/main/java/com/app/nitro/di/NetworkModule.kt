package com.app.nitro.data.di.module

import com.app.nitro.di.ApiService
import com.app.nitro.di.PostsRepositoryImp
import com.app.nitro.domain.repository.PostsRepository
import com.app.nitro.domain.usecase.usecase.*
import com.app.nitro.util.AppConstants.Companion.SENSORS_BASE_URL
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 30L

val NetworkModule = module {

    single { createService(get()) }

    single { createRetrofit(get(), SENSORS_BASE_URL) }

    single { createOkHttpClient() }

    single { MoshiConverterFactory.create() }

    single { Moshi.Builder().build() }

}

fun createRetrofit(okHttpClient: OkHttpClient, sensorUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(SENSORS_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create()).build()
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

    return OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
}


fun createService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun createPostRepository(apiService: ApiService): PostsRepository {
    return PostsRepositoryImp(apiService)
}


//sensors analytics  list
fun createGraphDataUseCase(postsRepository: PostsRepository): GraphDataUseCase {
    return GraphDataUseCase(postsRepository)
}

