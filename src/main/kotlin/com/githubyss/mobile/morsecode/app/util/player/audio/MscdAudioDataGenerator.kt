package com.githubyss.mobile.morsecode.app.util.player.audio

import android.media.AudioFormat
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverter
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig

/**
 * MscdAudioDataGenerator.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdAudioDataGenerator {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdAudioDataGenerator()
    }


    private val config = MscdAudioConfig.instance


    fun buildSineWaveAudioDataArray(audioDurationInMs: Long): Array<Double> {
        val audioFrequencyInHz = this@MscdAudioDataGenerator.config.audioFrequencyInHz
        val audioEncodingPcmFormat = this@MscdAudioDataGenerator.config.audioEncodingPcmFormat

        /** Build timeSampleArray according to audio duration. by Ace Yan */
        val timeSampleArray = buildTimeSampleArray(audioDurationInMs)

        /** Init audioDataArray according to the size of timeSampleArray. by Ace Yan */
        val audioDataArraySize = timeSampleArray.size
        val audioDataArray = Array(audioDataArraySize, { 0.toDouble() })

        /** Traverse audioDataArray to set audio data into it. by Ace Yan */
        for (idxOfAudioDataArray in 0 until audioDataArraySize) {
            /** Calculate out sine wave signal according to audioEncodingPcmFormat and audioFrequency. by Ace Yan */
            when (audioEncodingPcmFormat) {
                AudioFormat.ENCODING_PCM_FLOAT -> {
                    audioDataArray[idxOfAudioDataArray] = 1 * calculateOutSineWaveData(audioFrequencyInHz, timeSampleArray[idxOfAudioDataArray])
                }

                AudioFormat.ENCODING_PCM_16BIT -> {
                    audioDataArray[idxOfAudioDataArray] = Short.MAX_VALUE * calculateOutSineWaveData(audioFrequencyInHz, timeSampleArray[idxOfAudioDataArray])
                }

                AudioFormat.ENCODING_PCM_8BIT -> {
                    audioDataArray[idxOfAudioDataArray] = Byte.MAX_VALUE * calculateOutSineWaveData(audioFrequencyInHz, timeSampleArray[idxOfAudioDataArray])
                }

                else -> {
                }
            }
        }

        return audioDataArray
    }

    fun buildSineWaveAudioDataArray(delayPatternArray: Array<Long>): Array<Double> {
        val audioFrequencyInHz = this@MscdAudioDataGenerator.config.audioFrequencyInHz
        val audioEncodingPcmFormat = this@MscdAudioDataGenerator.config.audioEncodingPcmFormat

        val delayPatternArraySize = delayPatternArray.size

        var audioDurationInMs = 0.toLong()

        /** Traverse delayPatternArray to calculate out total duration of the audio. by Ace Yan */
        for (idx in 0 until delayPatternArraySize) {
            audioDurationInMs += delayPatternArray[idx]
        }

        /** Build total timeSampleArray according to total audio duration. by Ace Yan */
        val timeSampleArray = buildTimeSampleArray(audioDurationInMs)

        /** Init total audioDataArray according to the size of total timeSampleArray. by Ace Yan */
        val audioDataArraySize = timeSampleArray.size
        val audioDataArray = Array(audioDataArraySize, { 0.toDouble() })

        var positionInAudioDataArray = 0

        /**
         * Traverse delayPatternArray to calculate out each item in audioDataArray.
         * Every delay item in even position of delayPatternArray corresponding to mute audio data and every delay item in odd position corresponding to vocal audio data because the the delay item at 0 in delayPatternArray is a startDelay.
         * by Ace Yan
         */
        for (idxOfDelayPatternArray in 0 until delayPatternArraySize) {
            /** Build timeSampleArray of each delay item in the delayPatternArray. by Ace Yan */
            val timeSampleArrayOfEachDelay = buildTimeSampleArray(delayPatternArray[idxOfDelayPatternArray])

            /** Init size of audioDataArray of each delay item in the delayPatternArray. by Ace Yan */
            val audioDataArraySizeOfEachDelay = timeSampleArrayOfEachDelay.size

            /** Judge the index of delayPatternArray is even or odd. by Ace Yan */
            if (idxOfDelayPatternArray % 2 == 0) {
                for (idxOfAudioDataArrayOfEachDelay in 0 until audioDataArraySizeOfEachDelay) {
                    audioDataArray[positionInAudioDataArray] = 0.toDouble()
                    positionInAudioDataArray++
                }
            } else {
                for (idxOfAudioDataArrayOfEachDelay in 0 until audioDataArraySizeOfEachDelay) {
                    when (audioEncodingPcmFormat) {
                        AudioFormat.ENCODING_PCM_FLOAT -> {
                            audioDataArray[positionInAudioDataArray] = 1 * calculateOutSineWaveData(audioFrequencyInHz, timeSampleArray[idxOfAudioDataArrayOfEachDelay])
                        }

                        AudioFormat.ENCODING_PCM_16BIT -> {
                            audioDataArray[positionInAudioDataArray] = Short.MAX_VALUE * calculateOutSineWaveData(audioFrequencyInHz, timeSampleArray[idxOfAudioDataArrayOfEachDelay])
                        }

                        AudioFormat.ENCODING_PCM_8BIT -> {
                            audioDataArray[positionInAudioDataArray] = Byte.MAX_VALUE * calculateOutSineWaveData(audioFrequencyInHz, timeSampleArray[idxOfAudioDataArrayOfEachDelay])
                        }

                        else -> {
                        }
                    }
                    positionInAudioDataArray++
                }
            }
        }

        return audioDataArray
    }

    fun buildSineWaveAudioDataArray(message: String): Array<Double> {
        /** Init MscdMorseCodeConverterConfig by default data in itself when it was not built. by Ace Yan */
        if (!MscdMorseCodeConverterConfig.instance.beBuilt) {
            MscdMorseCodeConverterConfig.Builder.create()
        }
        return buildSineWaveAudioDataArray(MscdMorseCodeConverter.instance.buildMessageStringDelayPatternArray(message))
    }

    private fun buildTimeSampleArray(audioDurationInMs: Long): Array<Double> {
        val audioSampleRateInHz = this@MscdAudioDataGenerator.config.audioSampleRateInHz

        /** Converter audioDuration from ms to s. by Ace Yan */
        val audioDurationInS = audioDurationInMs / 1000.00

        /** Calculate out the size of timeSampleArray . by Ace Yan */
        val timeSampleArraySize = (Math.round(audioDurationInS * audioSampleRateInHz)).toInt()

        /** Init timeSampleArray. by Ace Yan */
        val timeSampleArray = Array(timeSampleArraySize, { 0.00 })

        /** Traverse timeSampleArray to calculate out each item in it. by Ace Yan */
        for (idx in 0 until timeSampleArraySize) {
            timeSampleArray[idx] = 0 + (1 / audioSampleRateInHz.toDouble()) * idx
        }

        return timeSampleArray
    }

    private fun calculateOutSineWaveData(audioFrequencyInHz: Int, timeSample: Double): Double {
        return Math.sin(2 * Math.PI * audioFrequencyInHz * timeSample)
    }
}
