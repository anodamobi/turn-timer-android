package anoda.mobi.anoda_turn_timer.ui.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.ui.settings.SettingsActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : MvpAppCompatActivity(), TimerView {

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, TimerActivity::class.java)
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
        startActivity(SettingsActivity.getStartIntent(this))
    }

    override fun showPauseButton() {
        vsActionButtons.displayedChild = 1
    }

    override fun showStartButton() {
        vsActionButtons.displayedChild = 0
    }

    override fun showTimerInProgress() {
        vsTimerReset.displayedChild = 0
    }

    override fun showTimerEndProgress() {
        vsTimerReset.displayedChild = 1
    }

    override fun updateTimerText(text: String) {
        tvTimerText.text = text
    }
}