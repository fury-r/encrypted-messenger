package com.fury.messenger.helper.audio

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AudioWaveformExtractorTest {
    @Test
    fun normalizeWaveform_returnsRequestedBarCount() {
        val waveform = AudioWaveformExtractor.normalizeWaveform(listOf(1, 4, 9, 16, 25, 36), 4)

        assertEquals(4, waveform.size)
    }

    @Test
    fun normalizeWaveform_preservesVariationAcrossBars() {
        val waveform = AudioWaveformExtractor.normalizeWaveform(
            listOf(1, 1, 2, 8, 3, 15, 5, 20),
            4
        )

        assertTrue(waveform.distinct().size > 1)
        assertTrue(waveform.all { it in 0.18f..1.0f })
    }
}
