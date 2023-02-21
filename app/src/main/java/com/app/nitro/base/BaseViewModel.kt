package  com.app.nitro.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    var pageLoadView: PageLoadView? = null
        private set

    fun setupView(pageLoadView: PageLoadView) {
        this.pageLoadView = pageLoadView
    }
}