package anoda.mobi.anoda_turn_timer.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.util.extension.maskCorrector
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.share

//private const val TIME_MASK = "##:##"

class SettingsActivity : MvpAppCompatActivity(), SettingsView {
    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initView()
    }

    private fun initView() {
//        initTypeFaces()
        setUIListeners()
    }

//    private fun initTypeFaces() {
//        incTimeDeep.npMinutes.typeface = App.typeFaceRancho
//        incTimeDeep.npSeconds.typeface = App.typeFaceRancho
//        incTimeDuration.npMinutes.typeface = App.typeFaceRancho
//        incTimeDuration.npSeconds.typeface = App.typeFaceRancho
//    }

    private fun setUIListeners() {
        etRoundDuration?.maskCorrector()
//        etRoundDuration?.setOnFocusChangeListener()
        etBeepTime?.maskCorrector()

        ivBackButton.setOnClickListener { onBackPressed() }
        ivShareButton.setOnClickListener { presenter.onShareAppLink() }


//        incTimeDeep.npMinutes.setOnValueChangedListener { _, _, minutes -> presenter.onChangeBeepTimeMinutes(minutes) }
//        incTimeDeep.npSeconds.setOnValueChangedListener { _, _, seconds -> presenter.onChangeBeepTimeSecond(seconds) }
//        incTimeDuration.npMinutes.setOnValueChangedListener { _, _, minutes -> presenter.onChangeRoundDurationTimeMinutes(minutes) }
//        incTimeDuration.npSeconds.setOnValueChangedListener { _, _, seconds -> presenter.onChangeRoundDurationTimeSecond(seconds) }

//        etRoundDuration?.addTextChangedListener(MaskWatcher(TIME_MASK))
//        etBeepTime?.addTextChangedListener(MaskWatcher(TIME_MASK))
    }

    override fun showShareAppVariant() {
        share(getString(R.string.share_description))
    }

    override fun setBeforeBeepTime(minutes: Int, seconds: Int) {
//        incTimeDeep.npMinutes.value = minutes
//        incTimeDeep.npSeconds.value = seconds
    }

    override fun setRoundDuration(minutes: Int, seconds: Int) {
//        incTimeDuration.npMinutes.value = minutes
//        incTimeDuration.npSeconds.value = seconds
    }
}