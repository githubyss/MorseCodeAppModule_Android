package com.githubyss.mobile.morsecode.app.util.player.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.os.AsyncTask
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
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
    private var audioDataGeneratorAsyncTask: AudioDataGeneratorAsyncTask? = null


    @SuppressLint("StaticFieldLeak")
    private inner class AudioDataGeneratorAsyncTask(private val onAudioDataGenerateListener: OnAudioDataGenerateListener) : AsyncTask<List<Int>, Int, Array<Float>>() {
        override fun doInBackground(vararg params: List<Int>?): Array<Float>? {
            if (isCancelled) {
                ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGeneratorAsyncTask.doInBackground() >>> isCancelled")
                return emptyArray()
            }

            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGeneratorAsyncTask.doInBackground() >>> Current time is ${System.currentTimeMillis()}.")

            return try {
                val delayPatternList = params[0]
                buildAudioDataList(audioConfig, delayPatternList ?: emptyList()).toTypedArray()
            } catch (exception: InterruptedException) {
                ComkitLogcatUtils.e(t = exception)
                emptyArray()
            }
        }

        override fun onPostExecute(result: Array<Float>?) {
            if (isCancelled) {
                return
            }

            if (result?.isEmpty() != false) {
                onAudioDataGenerateListener.onFailed()
                return
            }

            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGeneratorAsyncTask.onPostExecute() >>> Current time is ${System.currentTimeMillis()}.")
            onAudioDataGenerateListener.onSucceeded(result)
        }

        override fun onCancelled() {
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGeneratorAsyncTask.onCancelled() >>> Current time is ${System.currentTimeMillis()}.")
            onAudioDataGenerateListener.onCancelled()
        }
    }


    override fun startGenerateAudioData(delayPatternList: List<Int>, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> startGenerateAudioData() >>> Current time is ${System.currentTimeMillis()}.")

        audioDataGeneratorAsyncTask = AudioDataGeneratorAsyncTask(onAudioDataGenerateListener)
        audioDataGeneratorAsyncTask?.execute(delayPatternList)
    }

    override fun startGenerateAudioData(audioDurationInMs: Int, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> startGenerateAudioData() >>> Current time is ${System.currentTimeMillis()}.")

        val delayPatternList = ArrayList<Int>()
        delayPatternList[0] = 0
        delayPatternList[1] = audioDurationInMs

        audioDataGeneratorAsyncTask = AudioDataGeneratorAsyncTask(onAudioDataGenerateListener)
        audioDataGeneratorAsyncTask?.execute(delayPatternList)
    }

    override fun startGenerateAudioData(message: String, onAudioDataGenerateListener: OnAudioDataGenerateListener) {
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> startGenerateAudioData() >>> Current time is ${System.currentTimeMillis()}.")

        val delayPatternList = MscdMorseCodeConverter.instance.buildMessageStringDelayPatternList(message)

        audioDataGeneratorAsyncTask = AudioDataGeneratorAsyncTask(onAudioDataGenerateListener)
        audioDataGeneratorAsyncTask?.execute(delayPatternList)
    }

    override fun stopGenerateAudioData() {
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopGenerateAudioData() >>> Current time is ${System.currentTimeMillis()}.")
        if (audioDataGeneratorAsyncTask?.status == AsyncTask.Status.RUNNING) {
            audioDataGeneratorAsyncTask?.cancel(true)
            audioDataGeneratorAsyncTask = null
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopGenerateAudioData() >>> Set strategy hasCancelled true.")
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
                if (audioDataGeneratorAsyncTask?.isCancelled != false) {
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
                if (audioDataGeneratorAsyncTask?.isCancelled != false) {
                    return emptyArray()
                }

                val timeSampleArraySizeEachDelay = super.calculateTimeSampleCollectionSize(audioSampleRateInHz, delayPatternArray[idxPattern])

                /** Judge the index of delayPatternArray is even or odd. by Ace Yan */
                if (idxPattern % 2 == 0) {
                    for (idxSample in 0 until timeSampleArraySizeEachDelay) {
                        if (audioDataGeneratorAsyncTask?.isCancelled != false) {
                            return emptyArray()
                        }

                        audioDataArray[positionInAudioDataArray] = 0.toFloat()
                        positionInAudioDataArray++
                    }
                } else {
                    for (idxSample in 0 until timeSampleArraySizeEachDelay) {
                        if (audioDataGeneratorAsyncTask?.isCancelled != false) {
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
            emptyArray()
        } catch (exception: OutOfMemoryError) {
            ComkitLogcatUtils.e(t = exception)
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
                if (audioDataGeneratorAsyncTask?.isCancelled != false) {
                    return emptyList()
                }

                val timeSampleListSizeEachDelay = super.calculateTimeSampleCollectionSize(audioSampleRateInHz, delayPatternList[idxPattern])

                if (idxPattern % 2 == 0) {
                    for (idxSample in 0 until timeSampleListSizeEachDelay) {
                        if (audioDataGeneratorAsyncTask?.isCancelled != false) {
                            return emptyList()
                        }

                        audioDataList.add(0.toFloat())
                        positionInAudioDataList++
                    }
                } else {
                    for (idxSample in 0 until timeSampleListSizeEachDelay) {
                        if (audioDataGeneratorAsyncTask?.isCancelled != false) {
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
            emptyList()
        } catch (exception: OutOfMemoryError) {
            ComkitLogcatUtils.e(t = exception)
            emptyList()
        }
    }

    private fun calculateSineWaveData(audioFrequencyInHz: Int, timeSample: Float): Float {
        return (Math.sin(2 * Math.PI * audioFrequencyInHz * timeSample)).toFloat()
    }
}
