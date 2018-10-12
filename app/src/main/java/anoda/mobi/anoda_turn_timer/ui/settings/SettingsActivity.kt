package anoda.mobi.anoda_turn_timer.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import anoda.mobi.anoda_turn_timer.R
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.share

private const val TIME_MASK = "##:##"
private const val COLON_SYMBOL = ":"
private const val DOUBLE_ZERO = "00"
private const val MINUTES_LIMIT = 59
private const val SECONDS_LIMIT = 59

class SettingsActivity : MvpAppCompatActivity(), SettingsView {

    private var isRunning = false
    private var isDeleting = false

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }

    @InjectPresenter
    lateinit var mPresenter: SettingsPresenter

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

    override fun setBeforeBeepTime(minutes: Int, seconds: Int) {
        etBeepTime.text = getEditableTimeText("${minutesManager(minutes.toString())}:${minutesManager(seconds.toString())}")
        etBeepTime.setSelection(etRoundDuration.text.length)
    }

    override fun setRoundDuration(minutes: Int, seconds: Int) {
        etRoundDuration.text = getEditableTimeText("${minutesManager(minutes.toString())}:${minutesManager(seconds.toString())}")
        etRoundDuration.setSelection(etRoundDuration.text.length)
    }

    private fun setUIListeners() {
        etRoundDuration?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (isRunning || isDeleting || s.isEmpty()) return

                maskCorrector(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        etRoundDuration?.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus.not()) {
                    saveRoundTime()
                }
            }
        }


        etBeepTime?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (isRunning || isDeleting || s.isEmpty()) return

                maskCorrector(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                isDeleting = count > after
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        etBeepTime?.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus.not()) {
                    saveBeepTime()
                }
            }
        }

        ivBackButton.setOnClickListener { onBackPressed() }
        ivShareButton.setOnClickListener { mPresenter.onShareAppLink() }
    }

    private fun maskCorrector(s: Editable) {
        isRunning = true

        val editableLength = s.length
        if (editableLength < TIME_MASK.length) {
            if (TIME_MASK[editableLength] != '#') {
                s.append(TIME_MASK[editableLength])
            } else if (TIME_MASK[editableLength - 1] != '#') {
                val filters = s.filters
                s.filters = arrayOf()
                s.insert(editableLength - 1, TIME_MASK[editableLength - 1].toString())
                s.filters = filters
            }
        }

        isRunning = false
    }

    private fun getEditableTimeText(value: String): Editable = Editable.Factory.getInstance().newEditable(value)

    private fun getMinutes(value: String): String {
        if (TextUtils.equals(value.first().toString(), COLON_SYMBOL)) return DOUBLE_ZERO

        var minutes = when (value.length) {
            0 -> minutesManager(DOUBLE_ZERO)
            else -> minutesManager(value.substring(0, 2))
        }
        if (minutes.contains(COLON_SYMBOL)) {
            minutes = minutes.substring(0, minutes.indexOf(COLON_SYMBOL))
        }
        return if (minutes.toInt() > MINUTES_LIMIT) MINUTES_LIMIT.toString() else minutes
    }

    private fun getSeconds(value: String): String {
        if (TextUtils.equals(value.first().toString(), COLON_SYMBOL)) return value.substring(value.indexOf(COLON_SYMBOL) + 1)

        var seconds = when (value.length) {
            0, 1, 2 -> secondsManager(DOUBLE_ZERO)
            else -> secondsManager(value.substring(3))
        }
        if (seconds.contains(COLON_SYMBOL)) seconds = seconds.substring(seconds.indexOf(COLON_SYMBOL) + 1)
        return if (seconds.toInt() > SECONDS_LIMIT) SECONDS_LIMIT.toString() else seconds
    }

    private fun minutesManager(value: String): String = if (value.length == 1) "0$value" else value

    private fun secondsManager(value: String): String = if (value.length == 1) "${value}0" else value

    private fun saveRoundTime() {
        val timeStr = etRoundDuration.text.toString()
        val roundMinutes = getMinutes(timeStr)
        val roundSeconds = getSeconds(timeStr)
        etRoundDuration.text = getEditableTimeText("$roundMinutes:$roundSeconds")
        mPresenter.onChangeRoundDurationTimeMinutes(roundMinutes.toInt())
        mPresenter.onChangeRoundDurationTimeSecond(roundSeconds.toInt())
    }

    private fun saveBeepTime() {
        val timeStr = etBeepTime.text.toString()
        val beepMinutes = getMinutes(timeStr)
        val beepSeconds = getSeconds(timeStr)
        etBeepTime.text = getEditableTimeText("$beepMinutes:$beepSeconds")
        mPresenter.onChangeBeepTimeMinutes(beepMinutes.toInt())
        mPresenter.onChangeBeepTimeSecond(beepSeconds.toInt())
    }
}