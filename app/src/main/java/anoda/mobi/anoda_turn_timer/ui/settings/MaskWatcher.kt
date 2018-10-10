package anoda.mobi.anoda_turn_timer.ui.settings

import android.text.Editable
import android.text.TextWatcher


class MaskWatcher(private val mask: String) : TextWatcher {

    private var isRunning = false
    private var isDeleting = false

    override fun afterTextChanged(s: Editable?) {
        if (isRunning || isDeleting) return

        isRunning = true

        val editableLength = s!!.length
        if (editableLength < mask.length) {
            if (mask[editableLength] != '#') {
                s.append(mask[editableLength])
            } else if (mask[editableLength - 1] != '#') {
                val filters = s.filters // save filters
                s.filters = arrayOf() // clear filters
                s.insert(editableLength - 1, mask[editableLength - 1].toString()) // edit text
                s.filters = filters
            }
        }

        isRunning = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}