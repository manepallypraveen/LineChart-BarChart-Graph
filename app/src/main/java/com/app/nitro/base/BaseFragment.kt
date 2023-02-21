package  com.app.nitro.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.app.nitro.util.logInfo

open class BaseFragment : Fragment(), PageLoadView {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logInfo("Screen Fragment onViewCreated")
    }

    // Remove implementation after integrating with all screens
    override fun showProgress() {
    }

    // Remove implementation after integrating with all screens
    override fun dismissProgress() {
    }
}