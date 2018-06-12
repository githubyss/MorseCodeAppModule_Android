package com.githubyss.mobile.morsecode.app.util.player.audio

import android.media.AudioFormat
import android.os.AsyncTask
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.common.kit.util.ComkitTimeUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverter
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig
import java.io.EOFException

/**
 * MscdAudioDataGenerateSineWaveStrategy.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdAudioDataGenerateSineWaveStrategy : MscdAudioDataGenerateStrategy() {
    private var audioDataGenerateAsyncTask: AudioDataGenerateAsyncTask? = null

    private var beginTime = 0L
    private var endTime = 0L

    private var exceptionInfo = ""


    private inner class AudioDataGenerateAsyncTask(private val onAudioDataGenerateListener: OnAudioDataGenerateListener) : AsyncTask<List<Int>, Int, Array<Float>>() {
        override fun doInBackground(vararg params: List<Int>?): Array<Float>? {
            if (isCancelled) {
                ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGenerateAsyncTask.doInBackground() >>> isCancelled")
                return emptyArray()
            }

            beginTime = ComkitTimeUtils.currentTimeMillis()

            return try {
                val durationPattern = params[0] ?: emptyList()
                buildSignalAudioDataList(audioConfig, morseCodeConverterConfig, durationPattern).toTypedArray()
            } catch (exception: InterruptedException) {
                ComkitLogcatUtils.e(t = exception)
                emptyArray()
            }
        }

        override fun onPostExecute(result: Array<Float>?) {
            if (isCancelled) {
                return
            }

            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGenerateAsyncTask.onPostExecute() >>> Elapsed time = ${endTime - beginTime} ms.")
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGenerateAsyncTask.onPostExecute() >>> audioDataSize = ${result?.size}")

            if (result?.isEmpty() != false
                    || exceptionInfo.contains(ComkitResUtils.getString(resId = R.string.mscdFailingInfo))) {
                onAudioDataGenerateListener.onFailed(exceptionInfo)
                return
            }

            onAudioDataGenerateListener.onSucceeded(result)
        }

        override fun onCancelled() {
            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGenerateAsyncTask.onCancelled() >>> Elapsed time = ${endTime - beginTime} ms.")

            onAudioDataGenerateListener.onCancelled()
        }
    }


    override fun startGenerateAudioData(durationPatternArray: Array<Int>, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        audioDataGenerateAsyncTask = AudioDataGenerateAsyncTask(onAudioDataGenerateListener)
        audioDataGenerateAsyncTask?.execute(durationPatternArray.toList())
//        audioDataGenerateAsyncTask?.execute(durationPatternArray)
    }

    override fun startGenerateAudioData(durationPatternList: List<Int>, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        audioDataGenerateAsyncTask = AudioDataGenerateAsyncTask(onAudioDataGenerateListener)
        audioDataGenerateAsyncTask?.execute(durationPatternList)
//        audioDataGenerateAsyncTask?.execute(durationPatternList.toTypedArray())
    }

    override fun startGenerateAudioData(audioDurationMillis: Int, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        audioDataGenerateAsyncTask = AudioDataGenerateAsyncTask(onAudioDataGenerateListener)

        val durationPatternList = ArrayList<Int>()
        durationPatternList.add(0)
        durationPatternList.add(audioDurationMillis)
        audioDataGenerateAsyncTask?.execute(durationPatternList)

//        val durationPatternArray = Array(2) { it -> it }
//        durationPatternArray[0] = 0
//        durationPatternArray[1] = audioDurationMillis
//        audioDataGenerateAsyncTask?.execute(durationPatternArray)
    }

    override fun startGenerateAudioData(message: String, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        audioDataGenerateAsyncTask = AudioDataGenerateAsyncTask(onAudioDataGenerateListener)

        val durationPatternList = MscdMorseCodeConverter.instance.buildMessageStringDurationPatternList(message)
        audioDataGenerateAsyncTask?.execute(durationPatternList)

//        val durationPatternArray = MscdMorseCodeConverter.instance.buildMessageStringDurationPatternArray(message)
//        audioDataGenerateAsyncTask?.execute(durationPatternArray)
    }

    override fun stopGenerateAudioData() {
        if (audioDataGenerateAsyncTask?.status == AsyncTask.Status.RUNNING) {
            audioDataGenerateAsyncTask?.cancel(true)
            audioDataGenerateAsyncTask = null
        }
    }


    private fun buildAudioDataArray(audioConfig: MscdAudioConfig, durationPatternArray: Array<Int>): Array<Float> {
        if (durationPatternArray.isEmpty()) {
            return emptyArray()
        }

        val audioFrequencyHz = audioConfig.audioFrequencyHz
        val audioSampleRateHz = audioConfig.audioSampleRateHz
        val audioEncodingPcmFormat = audioConfig.audioEncodingPcmFormat

        val durationPatternArraySize = durationPatternArray.size

        var audioDurationTotalMillis = 0

        return try {
            /** Traverse durationPatternArray to calculate out total duration of the audio. by Ace Yan */
            for (idx in 0 until durationPatternArraySize) {
                if (audioDataGenerateAsyncTask?.isCancelled != false) {
                    return emptyArray()
                }

                audioDurationTotalMillis += durationPatternArray[idx]
            }

            /** Build total timeSampleArray according to total audio duration. by Ace Yan */
            val timeSampleArraySize = super.calculateTimeSampleCollectionSize(audioSampleRateHz, audioDurationTotalMillis)

            /** Init total audioDataArray according to the size of total timeSampleArray. by Ace Yan */
            val audioDataArray = Array(timeSampleArraySize, { 0.toFloat() })

            var positionInAudioDataArray = 0

            /**
             * Traverse durationPatternArray to calculate out each audio data element in audioDataArray.
             * Every duration item in even position of durationPatternArray corresponding to mute audio data and every duration item in odd position corresponding to vocal audio data because the the duration item at 0 in durationPatternArray is a startDuration.
             * by Ace Yan
             */
            for (idxPattern in 0 until durationPatternArraySize) {
                if (audioDataGenerateAsyncTask?.isCancelled != false) {
                    return emptyArray()
                }

                val eachDurationTimeSampleArraySize = super.calculateTimeSampleCollectionSize(audioSampleRateHz, durationPatternArray[idxPattern])

                /** Build timeSampleArray of each duration in the durationPatternArray. by Ace Yan */
                val eachDurationTimeSampleArray = super.buildTimeSampleArray(audioSampleRateHz, durationPatternArray[idxPattern])

                /** Judge the index of durationPatternArray is even or odd. by Ace Yan */
                if (idxPattern % 2 == 0) {
                    for (idxSample in 0 until eachDurationTimeSampleArraySize) {
                        if (audioDataGenerateAsyncTask?.isCancelled != false) {
                            return emptyArray()
                        }

                        audioDataArray[positionInAudioDataArray] = 0.toFloat()
                        positionInAudioDataArray++
                    }
                } else {
                    for (idxSample in 0 until eachDurationTimeSampleArraySize) {
                        if (audioDataGenerateAsyncTask?.isCancelled != false) {
                            return emptyArray()
                        }

                        when (audioEncodingPcmFormat) {
                            AudioFormat.ENCODING_PCM_FLOAT -> {
                                audioDataArray[positionInAudioDataArray] = 1 * calculateSineWaveData(audioFrequencyHz, eachDurationTimeSampleArray[idxSample])
                            }

                            AudioFormat.ENCODING_PCM_16BIT -> {
                                audioDataArray[positionInAudioDataArray] = Short.MAX_VALUE * calculateSineWaveData(audioFrequencyHz, eachDurationTimeSampleArray[idxSample])
                            }

                            AudioFormat.ENCODING_PCM_8BIT -> {
                                audioDataArray[positionInAudioDataArray] = Byte.MAX_VALUE * calculateSineWaveData(audioFrequencyHz, eachDurationTimeSampleArray[idxSample])
                            }

                            else -> {
                            }
                        }
                        positionInAudioDataArray++
                    }
                }
            }

            audioDataArray
        } catch (exception: EOFException) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            emptyArray()
        } catch (exception: OutOfMemoryError) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            emptyArray()
        }
    }

    /**
     * MscdAudioDataGenerateSineWaveStrategy.buildSignalAudioDataList(audioConfig, morseCodeConverterConfig, durationPatternList)
     * <Description> Build audioDataList by durationPatternList.
     * <Details>
     *
     * @param audioConfig
     * @param morseCodeConverterConfig
     * @param durationPatternList
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun buildSignalAudioDataList(audioConfig: MscdAudioConfig, morseCodeConverterConfig: MscdMorseCodeConverterConfig, durationPatternList: List<Int>): List<Float> {
        if (durationPatternList.isEmpty()) {
            return emptyList()
        }

        val audioSampleRateHz = audioConfig.audioSampleRateHz

        val durationPatternListSize = durationPatternList.size

        return try {
            /** Init the audioDataList. by Ace Yan */
            val audioDataList = ArrayList<Float>()

            /** DitAudioDataList and dahAudioDataList will be built firstly as signalAudioDataList cache to build the audioDataList. by Ace Yan */
            val ditAudioDataList = buildSignalAudioDataList(audioConfig, morseCodeConverterConfig.ditDuration)
            val dahAudioDataList = buildSignalAudioDataList(audioConfig, morseCodeConverterConfig.dahDuration)

            for (idxPattern in 0 until durationPatternListSize) {
                if (audioDataGenerateAsyncTask?.isCancelled != false) {
                    return emptyList()
                }

                if (idxPattern % 2 == 0) {
                    /** Add muteAudioDataList to audioDataList while index of pattern in durationPatternList is even. by Ace Yan */
                    audioDataList.addAll(super.buildMuteAudioDataList(audioSampleRateHz, durationPatternList[idxPattern]))
                } else {
                    /** Add signalAudioDataList to audioDataList while index of pattern in durationPatternList is odd, according to the dit-dah type of each durationPattern. by Ace Yan */
                    when {
                        durationPatternList[idxPattern] == morseCodeConverterConfig.ditDuration -> audioDataList.addAll(ditAudioDataList)
                        durationPatternList[idxPattern] == morseCodeConverterConfig.dahDuration -> audioDataList.addAll(dahAudioDataList)
                        else -> {
                        }
                    }
                }
            }

            audioDataList
        } catch (exception: EOFException) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            emptyList()
        } catch (exception: OutOfMemoryError) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            emptyList()
        }
    }

    /**
     * MscdAudioDataGenerateSineWaveStrategy.buildSignalAudioDataList(audioConfig, durationMillis)
     * <Description> Build signalAudioDataList by durationMillis.
     * <Details>
     *
     * @param audioConfig
     * @param durationMillis Duration of signal.
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun buildSignalAudioDataList(audioConfig: MscdAudioConfig, durationMillis: Int): List<Float> {
        if (durationMillis == 0) {
            return emptyList()
        }

        val audioFrequencyHz = audioConfig.audioFrequencyHz
        val audioSampleRateHz = audioConfig.audioSampleRateHz
        val audioEncodingPcmFormat = audioConfig.audioEncodingPcmFormat

        return try {
            val signalAudioDataList = ArrayList<Float>()

            val timeSampleListSize = super.calculateTimeSampleCollectionSize(audioSampleRateHz, durationMillis)
            val timeSampleList = super.buildTimeSampleList(audioSampleRateHz, durationMillis)

            for (idxSample in 0 until timeSampleListSize) {
                if (audioDataGenerateAsyncTask?.isCancelled != false) {
                    return emptyList()
                }

                when (audioEncodingPcmFormat) {
                    AudioFormat.ENCODING_PCM_FLOAT -> {
                        signalAudioDataList.add(1 * calculateSineWaveData(audioFrequencyHz, timeSampleList[idxSample]))
                    }

                    AudioFormat.ENCODING_PCM_16BIT -> {
                        signalAudioDataList.add(Short.MAX_VALUE * calculateSineWaveData(audioFrequencyHz, timeSampleList[idxSample]))
                    }

                    AudioFormat.ENCODING_PCM_8BIT -> {
                        signalAudioDataList.add(Byte.MAX_VALUE * calculateSineWaveData(audioFrequencyHz, timeSampleList[idxSample]))
                    }

                    else -> {
                    }
                }
            }

            signalAudioDataList
        } catch (exception: EOFException) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            emptyList()
        } catch (exception: OutOfMemoryError) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            emptyList()
        }
    }

    /**
     * MscdAudioDataGenerateSineWaveStrategy.calculateSineWaveData(audioFrequencyHz, timeSample)
     * <Description> Calculate sine wave data according to time sample.
     * <Details>
     *
     * @param audioFrequencyHz
     * @param timeSample
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun calculateSineWaveData(audioFrequencyHz: Int, timeSample: Float): Float {
        return (Math.sin(2 * Math.PI * audioFrequencyHz * timeSample)).toFloat()
    }
}
