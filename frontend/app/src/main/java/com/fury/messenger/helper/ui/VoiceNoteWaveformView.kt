package com.fury.messenger.helper.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.fury.messenger.R
import kotlin.math.max

class VoiceNoteWaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val playedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.white)
        strokeCap = Paint.Cap.ROUND
        alpha = 255
    }
    private val remainingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.white)
        strokeCap = Paint.Cap.ROUND
        alpha = 90
    }
    private var waveformBars: List<Float> = List(32) { 0.25f }
    private var progressRatio: Float = 0f
    private var durationMs: Int = 0
    var onSeekRequested: ((Int) -> Unit)? = null

    fun setWaveformData(data: List<Float>) {
        waveformBars = if (data.isEmpty()) List(32) { 0.25f } else data
        invalidate()
    }

    fun setPlaybackState(positionMs: Int, durationMs: Int) {
        this.durationMs = durationMs
        progressRatio = if (durationMs <= 0) 0f else positionMs.toFloat() / durationMs.toFloat()
        progressRatio = progressRatio.coerceIn(0f, 1f)
        invalidate()
    }

    fun reset() {
        progressRatio = 0f
        invalidate()
    }

    fun getSeekPosition(): Int {
        return (durationMs * progressRatio).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (waveformBars.isEmpty()) {
            return
        }
        val drawableWidth = width - paddingLeft - paddingRight
        val drawableHeight = height - paddingTop - paddingBottom
        if (drawableWidth <= 0 || drawableHeight <= 0) {
            return
        }

        val segmentWidth = drawableWidth.toFloat() / waveformBars.size
        val barWidth = max(4f, segmentWidth * 0.44f)
        playedPaint.strokeWidth = barWidth
        remainingPaint.strokeWidth = barWidth

        waveformBars.forEachIndexed { index, amplitude ->
            val centerX = paddingLeft + segmentWidth * index + (segmentWidth / 2f)
            val barHeight = max(10f, drawableHeight * amplitude)
            val top = paddingTop + ((drawableHeight - barHeight) / 2f)
            val bottom = top + barHeight
            val isPlayed = ((index + 1).toFloat() / waveformBars.size.toFloat()) <= progressRatio
            canvas.drawLine(centerX, top, centerX, bottom, if (isPlayed) playedPaint else remainingPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (durationMs <= 0) {
            return super.onTouchEvent(event)
        }
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                val usableWidth = (width - paddingLeft - paddingRight).toFloat().takeIf { it > 0f } ?: return false
                val relativeX = (event.x - paddingLeft).coerceIn(0f, usableWidth)
                progressRatio = (relativeX / usableWidth).coerceIn(0f, 1f)
                invalidate()
                onSeekRequested?.invoke(getSeekPosition())
                if (event.actionMasked == MotionEvent.ACTION_UP) {
                    performClick()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}
