package anoda.mobi.anoda_turn_timer.ui.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.ui.settings.SettingsActivity
import anoda.mobi.anoda_turn_timer.utils.PlaySoundManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_timer.*
import org.jetbrains.anko.share

class TimerActivity : MvpAppCompatActivity(), TimerView {

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, TimerActivity::class.java)

        const val TIMER_IN_PROGRESS = 0
        const val TIMER_END_PROGRESS = 1
        const val PAUSE_BUTTON = 1
        const val START_BUTTON = 0
        const val RESET_BUTTON = 0
        const val SHARE_BUTTON = 1

        const val MAIN_SIGNAL_URI = "android.resource://anoda.mobi.anoda_turn_timer/${R.raw.start_end}"
        const val SECONDARY_SIGNAL_URI = "android.resource://anoda.mobi.anoda_turn_timer/${R.raw.alarm}"
    }

    @InjectPresenter
    lateinit var presenter: TimerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        init()
    }

    private fun init() {
        ivBtnReset.setOnClickListener { presenter.onResetTimerClick() }
        ivSettings.setOnClickListener { presenter.onSettingsClick() }
        ivStart.setOnClickListener { presenter.onStartTimerClick() }
        ivPause.setOnClickListener { presenter.onPauseTimerClick() }
        ivReset.setOnClickListener { presenter.onResetTimerClick() }
        ivShare.setOnClickListener { presenter.onShareTimerClick() }
    }

    override fun startSettingsActivity() {
        startActivity(SettingsActivity.getStartIntent(this))
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
            vsTimerButtons.displayedChild = TIMER_IN_PROGRESS
            vsActionResetButtons.displayedChild = RESET_BUTTON
        }
    }

    override fun showTimerEndProgress() {
        runOnUiThread {
            vsTimerButtons.displayedChild = TIMER_END_PROGRESS
            vsActionResetButtons.displayedChild = SHARE_BUTTON
        }
    }

    override fun updateTimerText(text: String) {
        runOnUiThread {
            tvTimerText.text = text
        }
    }

    override fun shareApp() {
        share(getString(R.string.share_description))
    }

    override fun playMainSignal() {
        PlaySoundManager.playSound(this, MAIN_SIGNAL_URI)
    }

    override fun playSecondarySignal() {
        PlaySoundManager.playSound(this, SECONDARY_SIGNAL_URI)
    }

    override fun updateTimerBackgroundProgress(angle: Float) {
        runOnUiThread {
            tpbvTimerBackground.update(angle)
        }
    }
}