package com.app.nitro

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.app.nitro.data.di.module.AppModule
import com.app.nitro.data.di.module.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    private var mInstance: App? = null

    override fun onCreate() {
        super.onCreate()

        mInstance = this

        MultiDex.install(this)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(AppModule, NetworkModule))
        }

    }

    fun getInstance(): Context? {
        return mInstance
    }

}
