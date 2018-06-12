package com.githubyss.mobile.morsecode.app.util.player.audio

import android.media.AudioFormat
import android.os.AsyncTask
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.common.kit.util.ComkitTimeUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverter
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
                val delayPattern = params[0] ?: emptyList()
                buildAudioDataList(audioConfig, delayPattern).toTypedArray()
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


    override fun startGenerateAudioData(delayPatternArray: Array<Int>, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        audioDataGenerateAsyncTask = AudioDataGenerateAsyncTask(onAudioDataGenerateListener)
        audioDataGenerateAsyncTask?.execute(delayPatternArray.toList())
//        audioDataGenerateAsyncTask?.execute(delayPatternArray)
    }

    override fun startGenerateAudioData(delayPatternList: List<Int>, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        audioDataGenerateAsyncTask = AudioDataGenerateAsyncTask(onAudioDataGenerateListener)
        audioDataGenerateAsyncTask?.execute(delayPatternList)
//        audioDataGenerateAsyncTask?.execute(delayPatternList.toTypedArray())
    }

    override fun startGenerateAudioData(audioDurationInMs: Int, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        audioDataGenerateAsyncTask = AudioDataGenerateAsyncTask(onAudioDataGenerateListener)

        val delayPatternList = ArrayList<Int>()
        delayPatternList.add(0)
        delayPatternList.add(audioDurationInMs)
        audioDataGenerateAsyncTask?.execute(delayPatternList)

//        val delayPatternArray = Array(2) { it -> it }
//        delayPatternArray[0] = 0
//        delayPatternArray[1] = audioDurationInMs
//        audioDataGenerateAsyncTask?.execute(delayPatternArray)
    }

    override fun startGenerateAudioData(message: String, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        audioDataGenerateAsyncTask = AudioDataGenerateAsyncTask(onAudioDataGenerateListener)

        val delayPatternList = MscdMorseCodeConverter.instance.buildMessageStringDelayPatternList(message)
        audioDataGenerateAsyncTask?.execute(delayPatternList)

//        val delayPatternArray = MscdMorseCodeConverter.instance.buildMessageStringDelayPatternArray(message)
//        audioDataGenerateAsyncTask?.execute(delayPatternArray)
    }

    override fun stopGenerateAudioData() {
        if (audioDataGenerateAsyncTask?.status == AsyncTask.Status.RUNNING) {
            audioDataGenerateAsyncTask?.cancel(true)
            audioDataGenerateAsyncTask = null
        }
    }


    private fun buildAudioDataArray(audioConfig: MscdAudioConfig, delayPatternArray: Array<Int>): Array<Float> {
        if (delayPatternArray.isEmpty()) {
            return emptyArray()
        }

        val audioFrequencyInHz = audioConfig.audioFrequencyInHz
        val audioSampleRateInHz = audioConfig.audioSampleRateInHz
        val audioEncodingPcmFormat = audioConfig.audioEncodingPcmFormat

        val delayPatternArraySize = delayPatternArray.size

        var audioDurationTotalInMs = 0

        return try {
            /** Traverse delayPatternArray to calculate out total duration of the audio. by Ace Yan */
            for (idx in 0 until delayPatternArraySize) {
                if (audioDataGenerateAsyncTask?.isCancelled != false) {
                    return emptyArray()
                }

                audioDurationTotalInMs += delayPatternArray[idx]
            }

            /** Build total timeSampleArray according to total audio duration. by Ace Yan */
            val timeSampleArraySize = super.calculateTimeSampleCollectionSize(audioSampleRateInHz, audioDurationTotalInMs)

            /** Init total audioDataArray according to the size of total timeSampleArray. by Ace Yan */
            val audioDataArray = Array(timeSampleArraySize, { 0.toFloat() })

            var positionInAudioDataArray = 0

            /**
             * Traverse delayPatternArray to calculate out each audio data element in audioDataArray.
             * Every delay item in even position of delayPatternArray corresponding to mute audio data and every delay item in odd position corresponding to vocal audio data because the the delay item at 0 in delayPatternArray is a startDelay.
             * by Ace Yan
             */
            for (idxPattern in 0 until delayPatternArraySize) {
                if (audioDataGenerateAsyncTask?.isCancelled != false) {
                    return emptyArray()
                }

                val timeSampleArraySizeEachDelay = super.calculateTimeSampleCollectionSize(audioSampleRateInHz, delayPatternArray[idxPattern])

                /** Judge the index of delayPatternArray is even or odd. by Ace Yan */
                if (idxPattern % 2 == 0) {
                    for (idxSample in 0 until timeSampleArraySizeEachDelay) {
                        if (audioDataGenerateAsyncTask?.isCancelled != false) {
                            return emptyArray()
                        }

                        audioDataArray[positionInAudioDataArray] = 0.toFloat()
                        positionInAudioDataArray++
                    }
                } else {
                    for (idxSample in 0 until timeSampleArraySizeEachDelay) {
                        if (audioDataGenerateAsyncTask?.isCancelled != false) {
                            return emptyArray()
                        }

                        /** Build timeSampleArray of each delay in the delayPatternArray. by Ace Yan */
                        val timeSampleArrayEachDelay = super.buildTimeSampleArray(audioConfig, delayPatternArray[idxPattern])

                        when (audioEncodingPcmFormat) {
                            AudioFormat.ENCODING_PCM_FLOAT -> {
                                audioDataArray[positionInAudioDataArray] = 1 * calculateSineWaveData(audioFrequencyInHz, timeSampleArrayEachDelay[idxSample])
                            }

                            AudioFormat.ENCODING_PCM_16BIT -> {
                                audioDataArray[positionInAudioDataArray] = Short.MAX_VALUE * calculateSineWaveData(audioFrequencyInHz, timeSampleArrayEachDelay[idxSample])
                            }

                            AudioFormat.ENCODING_PCM_8BIT -> {
                                audioDataArray[positionInAudioDataArray] = Byte.MAX_VALUE * calculateSineWaveData(audioFrequencyInHz, timeSampleArrayEachDelay[idxSample])
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

    private fun buildAudioDataList(audioConfig: MscdAudioConfig, delayPatternList: List<Int>): List<Float> {
        if (delayPatternList.isEmpty()) {
            return emptyList()
        }

        val audioFrequencyInHz = audioConfig.audioFrequencyInHz
        val audioSampleRateInHz = audioConfig.audioSampleRateInHz
        val audioEncodingPcmFormat = audioConfig.audioEncodingPcmFormat

        val delayPatternListSize = delayPatternList.size

        return try {
            val audioDataList = ArrayList<Float>()

            var positionInAudioDataList = 0

            for (idxPattern in 0 until delayPatternListSize) {
                if (audioDataGenerateAsyncTask?.isCancelled != false) {
                    return emptyList()
                }

                val timeSampleListSizeEachDelay = super.calculateTimeSampleCollectionSize(audioSampleRateInHz, delayPatternList[idxPattern])

                if (idxPattern % 2 == 0) {
                    for (idxSample in 0 until timeSampleListSizeEachDelay) {
                        if (audioDataGenerateAsyncTask?.isCancelled != false) {
                            return emptyList()
                        }

                        audioDataList.add(0.toFloat())
                        positionInAudioDataList++
                    }
                } else {
                    for (idxSample in 0 until timeSampleListSizeEachDelay) {
                        if (audioDataGenerateAsyncTask?.isCancelled != false) {
                            return emptyList()
                        }

                        val timeSampleListEachDelay = super.buildTimeSampleList(audioConfig, delayPatternList[idxPattern])

                        when (audioEncodingPcmFormat) {
                            AudioFormat.ENCODING_PCM_FLOAT -> {
                                audioDataList.add(1 * calculateSineWaveData(audioFrequencyInHz, timeSampleListEachDelay[idxSample]))
                            }

                            AudioFormat.ENCODING_PCM_16BIT -> {
                                audioDataList.add(Short.MAX_VALUE * calculateSineWaveData(audioFrequencyInHz, timeSampleListEachDelay[idxSample]))
                            }

                            AudioFormat.ENCODING_PCM_8BIT -> {
                                audioDataList.add(Byte.MAX_VALUE * calculateSineWaveData(audioFrequencyInHz, timeSampleListEachDelay[idxSample]))
                            }

                            else -> {
                            }
                        }
                        positionInAudioDataList++
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

    private fun calculateSineWaveData(audioFrequencyInHz: Int, timeSample: Float): Float {
        return (Math.sin(2 * Math.PI * audioFrequencyInHz * timeSample)).toFloat()
    }
}
