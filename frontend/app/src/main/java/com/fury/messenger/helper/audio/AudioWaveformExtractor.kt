package com.fury.messenger.helper.audio

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import java.io.File
import kotlin.math.abs
import kotlin.math.max

object AudioWaveformExtractor {
    private const val OUTPUT_BARS = 32
    private const val TIMEOUT_US = 10_000L

    fun extractWaveform(file: File, bars: Int = OUTPUT_BARS): List<Float> {
        return runCatching {
            val amplitudes = decodeAmplitudes(file)
            normalizeWaveform(amplitudes, bars)
        }.getOrElse {
            fallbackWaveform(file, bars)
        }
    }

    internal fun normalizeWaveform(
        amplitudes: List<Int>,
        bars: Int = OUTPUT_BARS
    ): List<Float> {
        if (bars <= 0) {
            return emptyList()
        }
        if (amplitudes.isEmpty()) {
            return List(bars) { 0.18f }
        }
        val chunkSize = max(1, amplitudes.size / bars)
        val chunked = amplitudes.chunked(chunkSize).take(bars).map { bucket ->
            bucket.maxOrNull()?.toFloat() ?: 0f
        }.toMutableList()
        while (chunked.size < bars) {
            chunked.add(chunked.lastOrNull() ?: 0f)
        }
        val maxAmplitude = chunked.maxOrNull()?.takeIf { it > 0f } ?: 1f
        return chunked.map { amplitude ->
            val normalized = amplitude / maxAmplitude
            0.18f + (normalized * 0.82f)
        }
    }

    private fun decodeAmplitudes(file: File): List<Int> {
        val extractor = MediaExtractor()
        try {
            extractor.setDataSource(file.absolutePath)
            val trackIndex = (0 until extractor.trackCount).firstOrNull { index ->
                extractor.getTrackFormat(index).getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true
            } ?: return fallbackByteAmplitudes(file)

            extractor.selectTrack(trackIndex)
            val format = extractor.getTrackFormat(trackIndex)
            val mimeType = format.getString(MediaFormat.KEY_MIME) ?: return fallbackByteAmplitudes(file)
            val codec = MediaCodec.createDecoderByType(mimeType)
            val amplitudes = ArrayList<Int>()
            val info = MediaCodec.BufferInfo()

            try {
                codec.configure(format, null, null, 0)
                codec.start()

                var inputDone = false
                var outputDone = false

                while (!outputDone) {
                    if (!inputDone) {
                        val inputBufferId = codec.dequeueInputBuffer(TIMEOUT_US)
                        if (inputBufferId >= 0) {
                            val inputBuffer = codec.getInputBuffer(inputBufferId) ?: continue
                            val sampleSize = extractor.readSampleData(inputBuffer, 0)
                            if (sampleSize < 0) {
                                codec.queueInputBuffer(
                                    inputBufferId,
                                    0,
                                    0,
                                    0,
                                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                                )
                                inputDone = true
                            } else {
                                codec.queueInputBuffer(
                                    inputBufferId,
                                    0,
                                    sampleSize,
                                    extractor.sampleTime,
                                    0
                                )
                                extractor.advance()
                            }
                        }
                    }

                    when (val outputBufferId = codec.dequeueOutputBuffer(info, TIMEOUT_US)) {
                        MediaCodec.INFO_TRY_AGAIN_LATER -> Unit
                        MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> Unit
                        else -> {
                            if (outputBufferId >= 0) {
                                val outputBuffer = codec.getOutputBuffer(outputBufferId)
                                if (outputBuffer != null && info.size > 0) {
                                    outputBuffer.position(info.offset)
                                    outputBuffer.limit(info.offset + info.size)
                                    val bytes = ByteArray(info.size)
                                    outputBuffer.get(bytes)
                                    appendPcmAmplitudes(bytes, amplitudes)
                                }
                                codec.releaseOutputBuffer(outputBufferId, false)
                                if ((info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                                    outputDone = true
                                }
                            }
                        }
                    }
                }
                return amplitudes.ifEmpty { fallbackByteAmplitudes(file) }
            } finally {
                codec.stop()
                codec.release()
            }
        } finally {
            extractor.release()
        }
    }

    private fun appendPcmAmplitudes(bytes: ByteArray, amplitudes: MutableList<Int>) {
        var index = 0
        while (index + 1 < bytes.size) {
            val sample = ((bytes[index + 1].toInt() shl 8) or (bytes[index].toInt() and 0xFF)).toShort()
            amplitudes.add(abs(sample.toInt()))
            index += 2
        }
    }

    private fun fallbackWaveform(file: File, bars: Int): List<Float> {
        return normalizeWaveform(fallbackByteAmplitudes(file), bars)
    }

    private fun fallbackByteAmplitudes(file: File): List<Int> {
        val bytes = file.readBytes()
        if (bytes.isEmpty()) {
            return emptyList()
        }
        return bytes.map { abs(it.toInt()) }
    }
}
