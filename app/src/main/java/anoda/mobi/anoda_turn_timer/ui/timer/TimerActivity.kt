package anoda.mobi.anoda_turn_timer.ui.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.ui.settings.SettingsActivity
import anoda.mobi.anoda_turn_timer.util.PlaySoundManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_timer.*
import java.io.File

class TimerActivity : MvpAppCompatActivity(), TimerView {

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, TimerActivity::class.java)

        const val TIMER_IN_PROGRESS = 0
        const val TIMER_END_PROGRESS = 1
        const val PAUSE_BUTTON = 1
        const val START_BUTTON = 0
    }

    @InjectPresenter
    lateinit var mPresenter: TimerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        init()
    }

    private fun init() {
        ivBtnReset.setOnClickListener { mPresenter.onResetTimerClick() }
        ivSettings.setOnClickListener { mPresenter.onSettingsClick() }
        ivStart.setOnClickListener { mPresenter.onStartTimerClick() }
        ivPause.setOnClickListener { mPresenter.onPauseTimerClick() }
        ivReset.setOnClickListener { mPresenter.onResetTimerClick() }
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
        }
    }

    override fun showTimerEndProgress() {
        runOnUiThread {
            vsTimerButtons.displayedChild = TIMER_END_PROGRESS
        }
    }

    override fun updateTimerText(text: String) {
        runOnUiThread {
            tvTimerText.text = text
        }
    }

    override fun playMainSignal() {
        PlaySoundManager.playSound(this, rawSoundParser(R.raw.start_end))
    }

    override fun playSecondarySignal() {
        PlaySoundManager.playSound(this, rawSoundParser(R.raw.alarm))
    }

    override fun updateTimerBackgroundProgress(angle: Float) {
        runOnUiThread {
            tpbvTimerBackground.update(angle)
        }
    }

    private fun rawSoundParser(rawId: Int): String = "android.resource://$packageName${File.separator}$rawId"
}