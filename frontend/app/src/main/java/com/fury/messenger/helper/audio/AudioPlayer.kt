package com.fury.messenger.helper.audio

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.io.File

interface  IAudioPlayer{
    fun playFile(file: File,onComplete: (() -> Unit)?)
    fun stop()

}
class AudioPlayer(private val ctx: Context) :IAudioPlayer{

    private var mediaPlayer: MediaPlayer? =null

    private  var file:File?=null
   override fun playFile(file: File, onComplete: (() -> Unit)?) {
        stop()
        mediaPlayer= MediaPlayer().apply {
            mediaPlayer=this
            file.setReadable(true)
            setDataSource(file.absolutePath)
            prepare()
            setOnCompletionListener {
                stop()
                Log.d("Ddd","finished")

                if (onComplete!=null){
                    Log.d("Ddd","finished callback")
                    onComplete()
                }
            }

            this@AudioPlayer.file=file
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