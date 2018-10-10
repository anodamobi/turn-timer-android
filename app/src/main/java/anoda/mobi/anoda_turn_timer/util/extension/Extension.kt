package anoda.mobi.anoda_turn_timer.util.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

const val TIME_MASK = "##:##"

fun EditText.maskCorrector() {
    var isRunning = false
    var isDeleting = false

    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (isRunning || isDeleting || s.isEmpty()) return

            isRunning = true

            val editableLength = s.length
            if (editableLength < TIME_MASK.length) {
                if (TIME_MASK[editableLength] != '#') {
                    s.append(TIME_MASK[editableLength])
                } else if (TIME_MASK[editableLength - 1] != '#') {
                    val filters = s.filters // save filters
                    s.filters = arrayOf() // clear filters
                    s.insert(editableLength - 1, TIME_MASK[editableLength - 1].toString()) // edit text
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
    })


}