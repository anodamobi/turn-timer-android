package anoda.mobi.anoda_turn_timer.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.core.net.toUri

object PlaySoundManager {

    private var isPlayingSound = false

    fun playSound(context: Context, uriStr: String) {
        if (isPlayingSound.not()) {
            isPlayingSound = true
            val uri = uriStr.toUri()
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build())
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