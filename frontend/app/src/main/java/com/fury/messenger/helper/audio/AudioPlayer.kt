package com.fury.messenger.helper.audio

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

interface  IAudioPlayer{
    fun playFile(file: File)
    fun stop()

}
class AudioPlayer(private val ctx: Context) :IAudioPlayer{

    private var mediaPlayer: MediaPlayer? =null


    override fun playFile(file: File) {
        MediaPlayer.create(ctx,file.toUri()).apply {
            mediaPlayer=this
            start()
        }
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer=null
    }
}