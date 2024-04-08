package com.fury.messenger.helper.audio

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.io.File

interface  IAudioPlayer{
    fun playFile(file: File,seek:Int=0,onStart:(()->Unit)?,onComplete: (() -> Unit)?)
    fun  resume()
    fun seekTo(seek: Int=0)
    fun pause()
    fun stop()

    fun getPosition():Int

    fun isPlaying():Boolean?

}
class AudioPlayer(private val ctx: Context) :IAudioPlayer{

    private var mediaPlayer: MediaPlayer? =null
      var init:Boolean=false

    private  var file:File?=null
   override fun playFile(file: File, seek: Int,onStart:(()->Unit)?, onComplete: (() -> Unit)?) {
       stop()

       if(mediaPlayer==null){
            mediaPlayer= MediaPlayer().apply {
                mediaPlayer=this
                init=true
                file.setReadable(true)
                setDataSource(file.absolutePath)
                prepare()
                seekTo(0)

                setOnCompletionListener {
                    this@AudioPlayer.stop()
                    Log.d("Ddd","finished")
                    init=false

                    if (onComplete!=null){
                        Log.d("Ddd","finished callback")
                        onComplete()
                    }

                }

                this@AudioPlayer.file=file
                start()
                if(onStart!=null)onStart()

            }
        }
       else {
            mediaPlayer?.start()
        }
    }

    override fun resume() {
        mediaPlayer?.start()
    }

    override fun seekTo(seek: Int) {

        mediaPlayer?.seekTo(seek)

    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        this.file=null
        this.mediaPlayer=null
        this.init=false
    }

    override fun getPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun isPlaying(): Boolean? {
        return  mediaPlayer?.isPlaying
    }

}