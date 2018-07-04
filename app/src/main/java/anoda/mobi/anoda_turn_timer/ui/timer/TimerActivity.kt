package anoda.mobi.anoda_turn_timer.ui.timer

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.ui.settings.SettingsActivity
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

    override fun playSignal() {
        val uri = Uri.parse("android.resource://anoda.mobi.anoda_turn_timer/${R.raw.signal}")
        val mediaPlayer: MediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setVolume(1f, 1f)
        mediaPlayer.setDataSource(this, uri)
        mediaPlayer.prepare()
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            it.start()
            it.release()
        }
    }

    override fun updateTimerBackgroundProgress(angle: Float) {
        runOnUiThread {
            tpbvTimerBackground.update(angle)
        }
    }
}