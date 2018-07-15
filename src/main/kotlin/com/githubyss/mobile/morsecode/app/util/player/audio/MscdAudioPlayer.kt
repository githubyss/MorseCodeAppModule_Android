package com.githubyss.mobile.morsecode.app.util.player.audio

import android.media.AudioFormat
import android.media.AudioTrack
import android.os.AsyncTask
import android.os.Build
import com.githubyss.mobile.common.kit.info.ComkitSystemInfo
import com.githubyss.mobile.common.kit.logcat.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.resource.ComkitResUtils
import com.githubyss.mobile.morsecode.app.R

/**
 * MscdAudioPlayer.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdAudioPlayer private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdAudioPlayer()
    }


    interface OnAudioPlayListener {
        fun onSucceeded()
        fun onFailed(failingInfo: String)
        fun onCancelled()
    }


    /** Building the audioPlayerConfig by default variate value in itself when it was not built by user. by Ace Yan */
    private val audioPlayerConfig =
            if (!MscdAudioPlayerConfig.instance.hasBuilt)
                MscdAudioPlayerConfig.Builder
                        .create()
            else
                MscdAudioPlayerConfig.instance

    private var audioPlayAsyncTask: AudioPlayAsyncTask? = null

    private var beginTime = 0L
    private var endTime = 0L

    private var exceptionInfo = ""


    private inner class AudioPlayAsyncTask(private val onAudioPlayListener: OnAudioPlayListener) : AsyncTask<Array<Float>, Int, Boolean>() {
        override fun doInBackground(vararg params: Array<Float>?): Boolean? {
            if (isCancelled) {
                ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioPlayAsyncTask.doInBackground() >>> isCancelled")
                return true
            }

            beginTime = ComkitSystemInfo.currentTimeMillis()

            logcatAudioTrackState("AudioPlayAsyncTask.doInBackground", audioPlayerConfig.audioTrack, "Execution started! Before startAudioTrack().")

            return try {
                startAudioTrack(params[0] ?: emptyArray())
                true
            } catch (exception: InterruptedException) {
                ComkitLogcatUtils.e(t = exception)
                exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
                false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            if (isCancelled) {
                return
            }

            endTime = ComkitSystemInfo.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGenerateAsyncTask.onPostExecute() >>> Elapsed time = ${endTime - beginTime} ms.")

            if (result != false) {
                onAudioPlayListener.onSucceeded()
            } else {
                onAudioPlayListener.onFailed(exceptionInfo)
            }

            logcatAudioTrackState("AudioPlayAsyncTask.onPostExecute", audioPlayerConfig.audioTrack, "Execution finished!")
        }

        override fun onCancelled() {
            logcatAudioTrackState("AudioPlayAsyncTask.onCancelled", audioPlayerConfig.audioTrack, "Execution cancelled!")

            endTime = ComkitSystemInfo.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> AudioDataGenerateAsyncTask.onPostExecute() >>> Elapsed time = ${endTime - beginTime} ms.")

            onAudioPlayListener.onCancelled()

            stopAllAudioTrack()
        }
    }


    fun startPlayAudio(audioDataArray: Array<Float>, onAudioPlayListener: OnAudioPlayListener) {
        audioPlayAsyncTask = AudioPlayAsyncTask(onAudioPlayListener)
        audioPlayAsyncTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, audioDataArray)
    }

    fun startPlayAudio(durationPatternArray: Array<Int>, onAudioPlayListener: OnAudioPlayListener) {
        MscdAudioDataGenerator.instance.startGenerateAudioData(
                durationPatternArray,
                object : MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener {
                    override fun onSucceeded(audioDataArray: Array<Float>) {
                        audioPlayAsyncTask = AudioPlayAsyncTask(onAudioPlayListener)
                        audioPlayAsyncTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, audioDataArray)
                    }

                    override fun onFailed(failingInfo: String) {
                    }

                    override fun onCancelled() {
                    }
                }
        )
    }

    fun startPlayAudio(durationPatternList: List<Int>, onAudioPlayListener: OnAudioPlayListener) {
        MscdAudioDataGenerator.instance.startGenerateAudioData(
                durationPatternList,
                object : MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener {
                    override fun onSucceeded(audioDataArray: Array<Float>) {
                        audioPlayAsyncTask = AudioPlayAsyncTask(onAudioPlayListener)
                        audioPlayAsyncTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, audioDataArray)
                    }

                    override fun onFailed(failingInfo: String) {
                    }

                    override fun onCancelled() {
                    }
                }
        )
    }

    fun startPlayAudio(audioDurationMillis: Int, onAudioPlayListener: OnAudioPlayListener) {
        MscdAudioDataGenerator.instance.startGenerateAudioData(
                audioDurationMillis,
                object : MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener {
                    override fun onSucceeded(audioDataArray: Array<Float>) {
                        audioPlayAsyncTask = AudioPlayAsyncTask(onAudioPlayListener)
                        audioPlayAsyncTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, audioDataArray)
                    }

                    override fun onFailed(failingInfo: String) {
                    }

                    override fun onCancelled() {
                    }
                }
        )
    }

    fun startPlayAudio(message: String, onAudioPlayListener: OnAudioPlayListener) {
        MscdAudioDataGenerator.instance.startGenerateAudioData(
                message,
                object : MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener {
                    override fun onSucceeded(audioDataArray: Array<Float>) {
                        audioPlayAsyncTask = AudioPlayAsyncTask(onAudioPlayListener)
                        audioPlayAsyncTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, audioDataArray)
                    }

                    override fun onFailed(failingInfo: String) {
                    }

                    override fun onCancelled() {
                    }
                }
        )
    }

    fun stopPlayAudio() {
        if (audioPlayAsyncTask?.status == AsyncTask.Status.RUNNING) {
            audioPlayAsyncTask?.cancel(true)
            audioPlayAsyncTask = null
        }
    }

    /**
     * MscdAudioPlayer.startAudioTrack(audioDataArray)
     * <Description> Try to start audio play of AudioTrack.
     * <Details>
     *
     * @param audioDataArray
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun startAudioTrack(audioDataArray: Array<Float>): Boolean {
        val audioTrack = audioPlayerConfig.audioTrack

        logcatAudioTrackState("startAudioTrack", audioTrack, "Before try play().")

        try {
            /** No matter what play state is, the audioTrack can do play() if the state is STATE_INITIALIZED. by Ace Yan */
            when (audioTrack.state) {
                AudioTrack.STATE_UNINITIALIZED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> startAudioTrack() >>> state = STATE_UNINITIALIZED, try to play failed!")
                    return false
                }

                AudioTrack.STATE_INITIALIZED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> startAudioTrack() >>> state = STATE_INITIALIZED, try to play!")
                    audioTrack.play()
                    writeToAudioTrack(audioDataArray, audioTrack)
                }

                else -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> startAudioTrack() >>> else in when, try to play failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            return false
        }

//        logcatAudioTrackState("startAudioTrack", audioTrack, "After write().")
        return true
    }

    /**
     * MscdAudioPlayer.writeToAudioTrack(audioDataArray, audioTrack)
     * <Description> Try to write audioDataArray to AudioTrack.
     * <Details>
     *
     * @param audioDataArray
     * @param audioTrack
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun writeToAudioTrack(audioDataArray: Array<Float>, audioTrack: AudioTrack): Boolean {
        val audioEncodingPcmFormat = audioPlayerConfig.audioEncodingPcmFormat

        logcatAudioTrackState("writeToAudioTrack", audioTrack, "Before try write().")

        try {
            when (audioEncodingPcmFormat) {
                AudioFormat.ENCODING_PCM_FLOAT -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> writeToAudioTrack() >>> pcmFormat = ENCODING_PCM_FLOAT, try to write in FLOAT!")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        audioTrack.write(audioDataArray.toFloatArray(), 0, audioDataArray.size, AudioTrack.WRITE_BLOCKING)
                    }
                }

                AudioFormat.ENCODING_PCM_16BIT -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> writeToAudioTrack() >>> pcmFormat = ENCODING_PCM_16BIT, try to write in 16BIT!")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        audioTrack.write(audioDataArray.map { it.toShort() }.toShortArray(), 0, audioDataArray.size, AudioTrack.WRITE_BLOCKING)
                    }
                }

                AudioFormat.ENCODING_PCM_8BIT -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> writeToAudioTrack() >>> pcmFormat = ENCODING_PCM_8BIT, try to write in 8BIT!")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        audioTrack.write(audioDataArray.map { it.toByte() }.toByteArray(), 0, audioDataArray.size, AudioTrack.WRITE_BLOCKING)
                    }
                }

                else -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> writeToAudioTrack() >>> else in when, try to write failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            return false
        }

        return true
    }

    /**
     * MscdAudioPlayer.stopAllAudioTrack()
     * <Description> Try to stop all audio play of AudioTrack.
     * <Details> You can stop all audio play in the thread queue of AsyncTask when you are trying to play several audio one by one by the same AudioTrack instance.
     *
     * @attention There ara some methods need our attention.
     *
     * @link AudioTrack.pause(): Pauses the playback of the audio data. Data that has not been played back will not be discarded.
     * @more This means you can continue to play the data back by calling play() again.
     *       And you can discard the data by calling flush().
     *
     * @link AudioTrack.flush(): Flushes the audio data currently queued for playback. Any data that has been written but not yet presented will be discarded. No-op if not stopped or paused, or if the track's creation mode is not MODE_STREAM.
     * @more
     *
     * @link AudioTrack.stop(): Stops playing the audio data. When used on an instance created in MODE_STREAM mode, audio will stop playing after the last buffer that was written has been played. For an immediate stop, use pause(), followed by flush() to discard audio data that hasn't been played back yet.
     * @more
     *
     * @link AudioTrack.release(): Releases the native AudioTrack resources.
     * @more After doing this, the state of instance of AudioTrack will be changed to STATE_UNINITIALIZED.
     *       If you want to play audio in the future, you have to renew an instance of AudioTrack to make the state of it to be STATE_INITIALIZED.
     *
     * @param
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun stopAllAudioTrack(): Boolean {
        val audioTrack = audioPlayerConfig.audioTrack

        logcatAudioTrackState("stopAllAudioTrack", audioTrack, "Before try stop() or flush() or release().")

        try {
            when (audioTrack.playState) {
                AudioTrack.PLAYSTATE_PLAYING -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopAllAudioTrack() >>> playState = PLAYSTATE_PLAYING, try to stop and release!")
                    audioTrack.stop()
                    audioTrack.release()
                }

                AudioTrack.PLAYSTATE_PAUSED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopAllAudioTrack() >>> playState = PLAYSTATE_PAUSED, try to flush, stop and release!")
                    audioTrack.flush()
                    audioTrack.stop()
                    audioTrack.release()
                }

                AudioTrack.PLAYSTATE_STOPPED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopAllAudioTrack() >>> playState = PLAYSTATE_STOPPED, try to release!")
                    audioTrack.release()
                }

                else -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopAllAudioTrack() >>> else in when, try to stop failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(t = exception)
            return false
        }

        logcatAudioTrackState("stopAllAudioTrack", audioTrack, "After try stop() or flush() or release().")
        return true
    }

    /**
     * MscdAudioPlayer.stopCurrentAudioTrack()
     * <Description> Try to stop the current audio play of AudioTrack.
     * <Details> You can stop the audio play of the instance of AudioTrack in current thread and continue the next audio play in the thread queue of AsyncTask when you are trying to play several audio one by one by the same AudioTrack instance.
     *
     * @param
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun stopCurrentAudioTrack(): Boolean {
        val audioTrack = audioPlayerConfig.audioTrack

        logcatAudioTrackState("stopCurrentAudioTrack", audioTrack, "Before try stop() or flush() or release().")

        try {
            when (audioTrack.playState) {
                AudioTrack.PLAYSTATE_PLAYING -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopCurrentAudioTrack() >>> playState = PLAYSTATE_PLAYING, try to stop!")
                    audioTrack.stop()
                }

                AudioTrack.PLAYSTATE_PAUSED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopCurrentAudioTrack() >>> playState = PLAYSTATE_PAUSED, try to stop!")
                    audioTrack.stop()
                }

                AudioTrack.PLAYSTATE_STOPPED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopCurrentAudioTrack() >>> playState = PLAYSTATE_STOPPED, try to stop failed!")
                }

                else -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> stopCurrentAudioTrack() >>> else in when, try to stop failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(t = exception)
            return false
        }

        logcatAudioTrackState("stopCurrentAudioTrack", audioTrack, "After try stop().")
        return true
    }

    /**
     * MscdAudioPlayer.pauseAudioTrack()
     * <Description> Try to pause audio play of AudioTrack.
     * <Details>
     *
     * @attention There is a extraordinary usage: You can stop the audio play of the instance of AudioTrack in current thread and continue the next audio play in the thread queue of AsyncTask when you are trying to play several audio one by one by the same AudioTrack instance.
     * There is a method you can use to get the same result {@link #stopCurrentAudioTrack()}.
     *
     * @param
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun pauseAudioTrack(): Boolean {
        val audioTrack = audioPlayerConfig.audioTrack

        logcatAudioTrackState("pauseAudioTrack", audioTrack, "Before try pause().")

        try {
            when (audioTrack.playState) {
                AudioTrack.PLAYSTATE_PLAYING -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> pauseAudioTrack() >>> playState = PLAYSTATE_PLAYING, try to pause!")
                    audioTrack.pause()
                }

                AudioTrack.PLAYSTATE_PAUSED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> pauseAudioTrack() >>> playState = PLAYSTATE_PAUSED, try to pause failed!")
                    return false
                }

                AudioTrack.PLAYSTATE_STOPPED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> pauseAudioTrack() >>> playState = PLAYSTATE_STOPPED, try to pause failed!")
                    return false
                }

                else -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> pauseAudioTrack() >>> else in when, try to pause failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(t = exception)
            return false
        }

        logcatAudioTrackState("pauseAudioTrack", audioTrack, "After try pause().")
        return true
    }

    /**
     * MscdAudioPlayer.resumeAudioTrack()
     * <Description> Try to resume audio play of AudioTrack.
     * <Details>
     *
     * @attention This method does not work effectively. In other word, it will not resume playing the audio by call this method.
     * I will find out the reason in the future.
     *
     * @param
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    @Deprecated("Unuseful")
    fun resumeAudioTrack(): Boolean {
        val audioTrack = audioPlayerConfig.audioTrack

        logcatAudioTrackState("resumeAudioTrack", audioTrack, "Before try to resume.")

        try {
            when (audioTrack.state) {
                AudioTrack.STATE_UNINITIALIZED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> resumeAudioTrack() >>> state = STATE_UNINITIALIZED, try to resume failed!")
                    return false
                }

                AudioTrack.STATE_INITIALIZED -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> resumeAudioTrack() >>> state = STATE_INITIALIZED, judge play state!")

                    when (audioTrack.playState) {
                        AudioTrack.PLAYSTATE_PLAYING -> {
                            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> resumeAudioTrack() >>> playState = PLAYSTATE_PLAYING, try to resume failed!")
                            return false
                        }

                        AudioTrack.PLAYSTATE_PAUSED -> {
                            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> resumeAudioTrack() >>> playState = PLAYSTATE_PAUSED, try to resume!")
                            audioTrack.play()
                        }

                        AudioTrack.PLAYSTATE_STOPPED -> {
                            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> resumeAudioTrack() >>> playState = PLAYSTATE_STOPPED, try to resume failed!")
                            return false
                        }

                        else -> {
                            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> resumeAudioTrack() >>> else in when, try to resume failed!")
                            return false
                        }
                    }
                }

                else -> {
                    ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> resumeAudioTrack() >>> else in when, try to resume failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(t = exception)
            return false
        }

        logcatAudioTrackState("resumeAudioTrack", audioTrack, "After try to resume.")
        return true
    }

    /**
     * MscdAudioPlayer.releaseAudioTrack()
     * <Description> Try to release resource of AudioTrack.
     * <Details> You can use it to release a instance of AudioTrack.
     *
     * @param
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun releaseAudioTrack(): Boolean {
        val audioTrack = audioPlayerConfig.audioTrack

        logcatAudioTrackState("startAudioTrack", audioTrack, "Before try release().")

        try {
            audioTrack.release()
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(t = exception)
            return false
        }

        logcatAudioTrackState("startAudioTrack", audioTrack, "After try release().")
        return true
    }

    /**
     * MscdAudioPlayer.logcatAudioTrackState(methodName, audioTrack, message)
     * <Description> Logcat the state and playState of audioTrack.
     * <Details>
     *
     * @param methodName
     * @param audioTrack
     * @param message
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun logcatAudioTrackState(methodName: String, audioTrack: AudioTrack, message: String) {
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> " +
                "$methodName() >>> " +
                "$audioTrack >>> " +
                "state = ${if (audioTrack.state == AudioTrack.STATE_INITIALIZED) "STATE_INITIALIZED" else if (audioTrack.state == AudioTrack.STATE_UNINITIALIZED) "STATE_UNINITIALIZED" else "NONE"}, " +
                "playStatus = ${if (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) "PLAYSTATE_PLAYING" else if (audioTrack.playState == AudioTrack.PLAYSTATE_PAUSED) "PLAYSTATE_PAUSED" else if (audioTrack.playState == AudioTrack.PLAYSTATE_STOPPED) "PLAYSTATE_STOPPED" else "NONE"}. $message")
    }
}
