package com.fury.messenger.helper.audio

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import java.io.File

interface IAudioPlayer {
    fun playFile(file: File, seek: Int = 0, onStart: (() -> Unit)?, onComplete: (() -> Unit)?)
    fun resume()
    fun seekTo(seek: Int = 0)
    fun pause()
    fun stop()

    fun getPosition(): Int

    fun isPlaying(): Boolean?

}

class AudioPlayer(private val ctx: Context) : IAudioPlayer, SensorEventListener {
    private var sensorManager: SensorManager =
        ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var proximitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    private var audioManager = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager


    private var mediaPlayer: MediaPlayer? = null
    var init: Boolean = false

    private var file: File? = null

    override fun playFile(
        file: File,
        seek: Int,
        onStart: (() -> Unit)?,
        onComplete: (() -> Unit)?
    ) {
        stop()

        if (mediaPlayer == null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)

            mediaPlayer = MediaPlayer().apply {
                init = true
                file.setReadable(true)
                setDataSource(file.absolutePath)
                prepare()
                seekTo(0)
                mediaPlayer = this


                setOnCompletionListener {
                    this@AudioPlayer.stop()
                    Log.d("Ddd", "finished")
                    init = false

                    if (onComplete != null) {
                        Log.d("Ddd", "finished callback")
                        onComplete()
                    }

                }

                this@AudioPlayer.file = file
                start()
                if (onStart != null) onStart()

            }
        } else {
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
        this.file = null
        this.mediaPlayer = null
        this.init = false
    }

    override fun getPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    override fun isPlaying(): Boolean {
        if (mediaPlayer == null) return false
        return mediaPlayer!!.isPlaying
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
            val distance = event.values[0]
            Log.d("event", event.values[0].toString() + " " + proximitySensor?.maximumRange)
            pause()

            if (proximitySensor!!.maximumRange > distance) {
                Log.d("switch", "call")
                // switch to ear speaker

                audioManager.mode = AudioManager.MODE_IN_CALL

            } else {
                audioManager.mode = AudioManager.MODE_NORMAL
            }
            resume()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        when (p0?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
            }

            Sensor.TYPE_GYROSCOPE -> {
            }

        }
    }

}