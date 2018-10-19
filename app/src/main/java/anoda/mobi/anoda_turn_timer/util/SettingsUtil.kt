package anoda.mobi.anoda_turn_timer.util

import android.text.Editable

private const val TIME_MASK = "##:##"

fun maskWorker(s: Editable) {
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
}
