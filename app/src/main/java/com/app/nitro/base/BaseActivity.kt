package com.app.nitro.base

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.app.nitro.R
import com.app.nitro.util.logInfo

open class BaseActivity : AppCompatActivity(), PageLoadView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor()
        logInfo("Screen Activity OnCreate")
    }

    fun setStatusBarColor() {
        this.window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            statusBarColor = resources.getColor(getStatusBarColorId())
        }
    }

    open fun getStatusBarColorId(): Int {
        return R.color.white
    }

    override fun onResume() {
        super.onResume()
        logInfo("Screen Activity OnResume")
    }

    // Remove implementation after integrating with all screens
    override fun showProgress() {
    }

    // Remove implementation after integrating with all screens
    override fun dismissProgress() {
    }
}

