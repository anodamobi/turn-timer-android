package anoda.mobi.anoda_turn_timer.ui.main

import android.os.Bundle
import android.support.v4.content.ContextCompat
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.base.MvpWearableActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpWearableActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        // Enables Always-on
        setAmbientEnabled()
        btnSettings.setOnClickListener { presenter.onSettingsClick() }
        tvTimerText.setOnClickListener { presenter.onTimerClick() }
    }

    override fun showTimerInProgress() {
        runOnUiThread {
            tvTimerText.setTextColor(ContextCompat.getColor(this, R.color.colorGreen))
        }
    }

    override fun showTimerPauseProgress() {
        runOnUiThread {
            tvTimerText.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
        }
    }

    override fun onTimerFinished() {
        runOnUiThread {
            tvTimerText.text = getString(R.string.restart)
            tvTimerText.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
        }
    }

    override fun updateTimerText(text: String) {
        runOnUiThread {
            tvTimerText.text = text
        }
    }

    override fun startSettingsActivity() {

    }
}
