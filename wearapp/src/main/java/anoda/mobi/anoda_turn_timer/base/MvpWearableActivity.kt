package anoda.mobi.anoda_turn_timer.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.arellomobile.mvp.MvpDelegate

@SuppressLint("Registered")
open class MvpWearableActivity : WearableActivity() {

    private var mMvpDelegate: MvpDelegate<out MvpWearableActivity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMvpDelegate().onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        getMvpDelegate().onAttach()
    }

    override fun onResume() {
        super.onResume()

        getMvpDelegate().onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        getMvpDelegate().onSaveInstanceState(outState)
        getMvpDelegate().onDetach()
    }

    override fun onStop() {
        super.onStop()

        getMvpDelegate().onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()

        getMvpDelegate().onDestroyView()

        if (isFinishing) {
            getMvpDelegate().onDestroy()
        }
    }

    /**
     * @return The [MvpDelegate] being used by this Activity.
     */
    fun getMvpDelegate(): MvpDelegate<out MvpWearableActivity> {
        if (mMvpDelegate == null) {
            mMvpDelegate = MvpDelegate<MvpWearableActivity>(this)
        }
        return mMvpDelegate!!
    }

}