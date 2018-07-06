package anoda.mobi.anoda_turn_timer.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.core.net.toUri

object PlaySoundManager {

    private var isPlayingSound = false

    fun playSound(context: Context, uriStr: String) {
        if (isPlayingSound.not()) {
            isPlayingSound = true
            val uri = uriStr.toUri()
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.setVolume(1f, 1f)
            mediaPlayer.setDataSource(context, uri)
            mediaPlayer.prepare()
            mediaPlayer.start()

            mediaPlayer.setOnCompletionListener {
                it.start()
                it.release()
                isPlayingSound = false
            }
        }
    }
}