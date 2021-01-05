package com.smzh.piano

import kotlin.math.sin

object BeatsProducer {

    private const val frequency = 440f

    private const val period = 1 / frequency

    const val sampleRate = 48000

    private const val beatsDuration = 250f

    private const val samples: Int = (beatsDuration / 1000f * sampleRate).toInt()

    private val beatsData = ShortArray(samples)

    private val pianoFrequency = floatArrayOf(
        440f,
        493f,
        523f,
        587f,
        659f,
        698f,
        783f,

        880f,
        987f,
        1046f,
        1174f,
        1318f,
        1396f,
        1567f,

        1760f
    )


    fun getBeatsData(index: Int = 0): ShortArray {
        for (i: Int in 0 until samples) {
            val value = sin(i / (sampleRate / pianoFrequency[index]) * Math.PI * 2)
            beatsData[i] = if (value > 0) {
                (value * 32767).toShort()
            } else {
                (value * 32768).toShort()
            }
        }
        return beatsData
    }

}