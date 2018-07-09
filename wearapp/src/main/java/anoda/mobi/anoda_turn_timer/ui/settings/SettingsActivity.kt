package anoda.mobi.anoda_turn_timer.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.base.MvpWearableActivity
import anoda.mobi.anoda_turn_timer.ui.settings.SettingsPresenter.Companion.getTimerPickerValues
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : MvpWearableActivity(), SettingsView {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        init()
    }

    private fun init() {
        // Enables Always-on
        setAmbientEnabled()
        tvBack.setOnClickListener { onBackPressed() }
        npTimerPicker.wrapSelectorWheel = false
        npTimerPicker.minValue = 0
        npTimerPicker.maxValue = getTimerPickerValues().size - 1
        npTimerPicker.displayedValues = getTimerPickerValues()
        btnSet.setOnClickListener { presenter.saveTimeToPrefs(npTimerPicker.value) }
    }

    override fun setRoundDuration(position: Int) {
        npTimerPicker.value = position
    }

    override fun closeActivity() {
        finish()
    }
}