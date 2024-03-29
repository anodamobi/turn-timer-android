package anoda.mobi.anoda_turn_timer.ui.splash

import android.os.Bundle
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.ui.timer.TimerActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter

class SplashActivity : MvpAppCompatActivity(), SplashView {

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    }

    override fun startTimerActivity() {
        startActivity(TimerActivity.getStartIntent(this))
        finish()
    }

}