package com.fury.messenger.helper.audio

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import java.io.File

interface  IAudioPlayer{
    fun playFile(file: File)
    fun stop()

}
class AudioPlayer(private val ctx: Context) :IAudioPlayer{

    private var mediaPlayer: MediaPlayer? =null

    private  var file:File?=null
    override fun playFile(file: File) {
        stop()
        mediaPlayer= MediaPlayer().apply {
            mediaPlayer=this
            file.setReadable(true)
            setDataSource(file.absolutePath)
            prepare()
            setOnCompletionListener { stop() }

            this@AudioPlayer.file=file
            Log.d("file",file.exists().toString()+" "+file.toUri())
            start()
        }

    }

    override fun stop() {
        mediaPlayer?.stop()
        this.file?.delete()
        this.file=null
        mediaPlayer=null
    }
}