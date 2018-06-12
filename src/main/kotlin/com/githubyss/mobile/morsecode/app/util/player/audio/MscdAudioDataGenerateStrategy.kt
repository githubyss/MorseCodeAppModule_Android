package com.githubyss.mobile.morsecode.app.util.player.audio

/**
 * MscdAudioDataGenerateStrategy.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
abstract class MscdAudioDataGenerateStrategy {
    interface OnAudioDataGenerateListener {
        fun onSucceeded(audioDataArray: Array<Float>)
        fun onFailed(failingInfo: String)
        fun onCancelled()
    }


    abstract fun startGenerateAudioData(delayPatternList: List<Int>, onAudioDataGenerateListener: OnAudioDataGenerateListener)
    abstract fun startGenerateAudioData(audioDurationInMs: Int, onAudioDataGenerateListener: OnAudioDataGenerateListener)
    abstract fun startGenerateAudioData(message: String, onAudioDataGenerateListener: OnAudioDataGenerateListener)
    abstract fun stopGenerateAudioData()


    /** Building the audioConfig by default variate value in itself when it was not built by user. by Ace Yan */
    protected val audioConfig =
            if (!MscdAudioConfig.instance.hasBuilt)
                MscdAudioConfig.Builder
                        .create()
            else
                MscdAudioConfig.instance


    protected fun buildTimeSampleArray(audioConfig: MscdAudioConfig, audioDurationInMs: Int): Array<Float> {
        val audioSampleRateInHz = audioConfig.audioSampleRateInHz

        /** Calculate out the size of timeSampleArray . by Ace Yan */
        val timeSampleArraySize = calculateTimeSampleCollectionSize(audioSampleRateInHz, audioDurationInMs)

        /** Init timeSampleArray. by Ace Yan */
        val timeSampleArray = Array(timeSampleArraySize, { 0.toFloat() })

        /** Traverse timeSampleArray to calculate out each element in it. by Ace Yan */
        for (idx in 0 until timeSampleArraySize) {
            timeSampleArray[idx] = calculateTimeSample(audioSampleRateInHz, idx)
        }

        return timeSampleArray
    }

    protected fun buildTimeSampleList(audioConfig: MscdAudioConfig, audioDurationInMs: Int): List<Float> {
        val audioSampleRateInHz = audioConfig.audioSampleRateInHz

        /** Calculate out the size of timeSampleList . by Ace Yan */
        val timeSampleListSize = calculateTimeSampleCollectionSize(audioSampleRateInHz, audioDurationInMs)

        /** Init timeSampleList. by Ace Yan */
        val timeSampleList = ArrayList<Float>()

        /** Traverse timeSampleList to calculate out each element in it. by Ace Yan */
        (0 until timeSampleListSize)
                .mapTo(timeSampleList) { calculateTimeSample(audioSampleRateInHz, it) }

        return timeSampleList
    }

    /**
     * MscdAudioDataGenerateStrategy.calculateTimeSampleCollectionSize(audioSampleRateInHz, audioDurationInMs)
     * <Description> Calculate out the size of time sample collection.
     * <Details>
     *
     * @param audioSampleRateInHz
     * @param audioDurationInMs
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    protected fun calculateTimeSampleCollectionSize(audioSampleRateInHz: Int, audioDurationInMs: Int): Int {
        return (Math.floor(audioDurationInMs / 1000.00 * audioSampleRateInHz)).toInt()
    }

    /**
     * MscdAudioDataGenerateStrategy.calculateTimeSample(audioSampleRateInHz, idx)
     * <Description> Calculate out each time sample element of time sample collection.
     * <Details> Time sample will act as x-coordinate in coordinate system of time-signal.
     *
     * @param audioSampleRateInHz
     * @param idx
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun calculateTimeSample(audioSampleRateInHz: Int, idx: Int): Float {
        return (0 + (1 / audioSampleRateInHz.toFloat()) * idx)
    }
}
