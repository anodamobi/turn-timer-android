package anoda.mobi.anoda_turn_timer.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri

object PlaySoundManager {

    fun playSound(context: Context, uriStr: String) {
        val uri = Uri.parse(uriStr)
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setVolume(1f, 1f)
        mediaPlayer.setDataSource(context, uri)
        mediaPlayer.prepare()
        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            it.start()
            it.release()
        }
    }
}