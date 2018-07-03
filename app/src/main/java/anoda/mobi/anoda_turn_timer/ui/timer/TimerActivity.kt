package anoda.mobi.anoda_turn_timer.ui.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import anoda.mobi.anoda_turn_timer.R
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : MvpAppCompatActivity(), TimerView {

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, TimerActivity::class.java)

        const val TIMER_IN_PROGRESS = 0
        const val TIMER_END_PROGRESS = 1
        const val PAUSE_BUTTON = 1
        const val START_BUTTON = 0
    }

    @InjectPresenter
    lateinit var presenter: TimerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        init()
    }

    private fun init() {
        ivSettings.setOnClickListener { presenter.onSettingsClick() }
        ivStart.setOnClickListener { presenter.onStartTimerClick() }
        ivPause.setOnClickListener { presenter.onPauseTimerClick() }
        tvTimerText.setOnClickListener { presenter.onTimerTextClick() }
        ivReset.setOnClickListener { presenter.onResetTimerClick() }
    }

    override fun startSettingsActivity() {

    }

    override fun showPauseButton() {
        runOnUiThread {
            vsActionButtons.displayedChild = PAUSE_BUTTON
        }
    }

    override fun showStartButton() {
        runOnUiThread {
            vsActionButtons.displayedChild = START_BUTTON
        }
    }

    override fun showTimerInProgress() {
        runOnUiThread {
            vsTimerReset.displayedChild = TIMER_IN_PROGRESS
        }
    }

    override fun showTimerEndProgress() {
        runOnUiThread {
            vsTimerReset.displayedChild = TIMER_END_PROGRESS
        }
    }

    override fun updateTimerText(text: String) {
        runOnUiThread {
            tvTimerText.text = text
        }
    }
}