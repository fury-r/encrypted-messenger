package com.fury.messenger.helper.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

interface IAudioRecorder{
    fun start(outputFile: File)
    fun stop()
}
class AudioRecorder (private  val ctx: Context):IAudioRecorder {
    private val root="/audio/"
    private var recorder:MediaRecorder?=null
    override fun start(outputFile: File) {
        createMediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile)
            prepare()
            start()
            recorder=this

        }


    }

   private fun createMediaRecorder():MediaRecorder{
       return if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            MediaRecorder(ctx)
        }
        else MediaRecorder()
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder=null
    }
}