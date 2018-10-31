package anoda.mobi.anoda_turn_timer.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import anoda.mobi.anoda_turn_timer.R
import anoda.mobi.anoda_turn_timer.util.maskWorker
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.share

class SettingsActivity : MvpAppCompatActivity(), SettingsView {

    @InjectPresenter
    lateinit var mPresenter: SettingsPresenter

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }

    private var isRunning = false
    private var isDeleting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setUIListeners()
    }

    override fun onStop() {
        saveRoundTime()
        saveBeepTime()
        super.onStop()
    }

    override fun showShareAppVariant() {
        share(String.format(getString(R.string.share_description), packageName))
    }

    override fun setRoundDuration(roundTime: Editable) {
        etRoundDuration?.text = roundTime
        etRoundDuration.setSelection(roundTime.length)
    }

    override fun setBeepTime(beepTime: Editable) {
        etBeepTime?.text = beepTime
        etBeepTime?.setSelection(beepTime.length)
    }

    private fun setUIListeners() {
        etRoundDuration?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (isRunning || isDeleting || s.isEmpty()) return

                maskCorrector(s)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val caretPosition = etRoundDuration.selectionStart
                if (isDeleting.not()) {
                    mPresenter.onTextChanged(caretPosition, s, true)
                }
            }
        })
        etRoundDuration?.setOnFocusChangeListener { _, hasFocus -> if (hasFocus.not()) saveRoundTime() }

        etBeepTime?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (isRunning || isDeleting || s.isEmpty()) return

                maskCorrector(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val caretPosition = etBeepTime.selectionStart
                if (isDeleting.not()) {
                    mPresenter.onTextChanged(caretPosition, s, false)
                }
            }
        })
        etBeepTime?.setOnFocusChangeListener { _, hasFocus -> if (hasFocus.not()) saveBeepTime() }

        ivBackButton.setOnClickListener { onBackPressed() }
        ivShareButton.setOnClickListener { mPresenter.onShareAppLink() }
    }

    private fun saveRoundTime() {
        mPresenter.saveRoundTime(etRoundDuration?.text.toString())
    }

    private fun saveBeepTime() {
        mPresenter.saveBeepTime(etBeepTime?.text.toString())
    }

    private fun maskCorrector(s: Editable) {
        isRunning = true
        maskWorker(s)
        isRunning = false
    }
}