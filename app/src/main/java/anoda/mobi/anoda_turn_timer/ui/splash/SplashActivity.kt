package anoda.mobi.anoda_turn_timer.ui.splash

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.ui.timer.TimerActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_splash.*


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

    override fun startAnimation() {
        val rotate = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = SplashPresenter.DURATION_ANIMATE
        ivHourglass.startAnimation(rotate)
    }
}