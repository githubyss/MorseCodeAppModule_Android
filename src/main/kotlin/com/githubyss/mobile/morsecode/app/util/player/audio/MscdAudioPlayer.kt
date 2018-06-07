package com.githubyss.mobile.morsecode.app.util.player.audio

import android.media.AudioFormat
import android.media.AudioTrack
import android.os.AsyncTask
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitTypeCastUtils

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


    /** Building the config by default variate value in itself when it was not built by user. by Ace Yan */
    private val config =
            if (!MscdAudioConfig.instance.hasBuilt)
                MscdAudioConfig.Builder.create()
            else
                MscdAudioConfig.instance

    private var audioPlayerAsyncTask: AudioPlayerAsyncTask? = null


    interface OnAudioPlayListener {
        fun onFinished()
        fun onStopped()
        fun onPaused()
        fun onCancelled()
    }

    private inner class AudioPlayerAsyncTask : AsyncTask<Array<Double>, Int, Boolean>() {
        override fun doInBackground(vararg params: Array<Double>?): Boolean {
            if (isCancelled) {
                ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> doInBackground() >>> isCancelled")
                return true
            }

            logcatAudioTrackState("doInBackground", this@MscdAudioPlayer.config.audioTrack, "Execution started! Before startPlayAudio().")

            return try {
                this@MscdAudioPlayer.startPlayAudio(params[0] ?: emptyArray())
                true
            } catch (exception: InterruptedException) {
                ComkitLogcatUtils.e(exception)
                false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            if (isCancelled) {
                return
            }

            logcatAudioTrackState("onPostExecute", this@MscdAudioPlayer.config.audioTrack, "Execution finished!")
        }

        override fun onCancelled() {
            logcatAudioTrackState("onCancelled", this@MscdAudioPlayer.config.audioTrack, "Execution cancelled!")

            stopAllPlayAudio()
        }
    }


    fun startAudioPlayerAsyncTask(audioData: Array<Double>) {
        this@MscdAudioPlayer.audioPlayerAsyncTask = AudioPlayerAsyncTask()
        this@MscdAudioPlayer.audioPlayerAsyncTask?.execute(audioData)
    }

    fun startAudioPlayerAsyncTask(audioDurationInMs: Long) {
        val audioData = MscdAudioDataGenerator.instance.buildSineWaveAudioDataArray(audioDurationInMs)
        this@MscdAudioPlayer.audioPlayerAsyncTask = AudioPlayerAsyncTask()
        this@MscdAudioPlayer.audioPlayerAsyncTask?.execute(audioData)
    }

    fun startAudioPlayerAsyncTask(delayPatternArray: Array<Long>) {
        val audioData = MscdAudioDataGenerator.instance.buildSineWaveAudioDataArray(delayPatternArray)
        this@MscdAudioPlayer.audioPlayerAsyncTask = AudioPlayerAsyncTask()
        this@MscdAudioPlayer.audioPlayerAsyncTask?.execute(audioData)
    }

    fun startAudioPlayerAsyncTask(message: String) {
        val audioData = MscdAudioDataGenerator.instance.buildSineWaveAudioDataArray(message)
        this@MscdAudioPlayer.audioPlayerAsyncTask = AudioPlayerAsyncTask()
        this@MscdAudioPlayer.audioPlayerAsyncTask?.execute(audioData)
    }

    fun cancelAudioPlayerAsyncTask() {
        if (this@MscdAudioPlayer.audioPlayerAsyncTask?.status == AsyncTask.Status.RUNNING) {
            this@MscdAudioPlayer.audioPlayerAsyncTask?.cancel(true)
        }
    }

    /**
     * MscdAudioPlayer.startPlayAudio(audioData)
     * <Description> Try to start audio play of AudioTrack.
     * <Details>
     *
     * @param audioData
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun startPlayAudio(audioData: Array<Double>): Boolean {
        val audioTrack = this@MscdAudioPlayer.config.audioTrack

        logcatAudioTrackState("startPlayAudio", audioTrack, "Before try play().")

        try {
            /** No matter what play state is, the audioTrack can do play() if the state is STATE_INITIALIZED. by Ace Yan */
            when (audioTrack.state) {
                AudioTrack.STATE_UNINITIALIZED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> startPlayAudio() >>> state = STATE_UNINITIALIZED, try to play failed!")
                    return false
                }

                AudioTrack.STATE_INITIALIZED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> startPlayAudio() >>> state = STATE_INITIALIZED, try to play!")
                    audioTrack.play()
                    writeAudioDataToTrack(audioData, audioTrack)
                }

                else -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> startPlayAudio() >>> else in when, try to play failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(exception)
            return false
        }

//        logcatAudioTrackState("startPlayAudio", audioTrack, "After write().")
        return true
    }

    /**
     * MscdAudioPlayer.writeAudioDataToTrack(audioData, audioTrack)
     * <Description> Try to write audioData to AudioTrack.
     * <Details>
     *
     * @param audioData
     * @param audioTrack
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun writeAudioDataToTrack(audioData: Array<Double>, audioTrack: AudioTrack): Boolean {
        val audioEncodingPcmFormat = this@MscdAudioPlayer.config.audioEncodingPcmFormat

        logcatAudioTrackState("writeAudioDataToTrack", audioTrack, "Before try write().")

        try {
            when (audioEncodingPcmFormat) {
                AudioFormat.ENCODING_PCM_FLOAT -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> writeAudioDataToTrack() >>> pcmFormat = ENCODING_PCM_FLOAT, try to write in FLOAT!")
                    audioTrack.write(ComkitTypeCastUtils.doubleArrayToFloatArray(audioData).toFloatArray(), 0, audioData.size, AudioTrack.WRITE_BLOCKING)
                }

                AudioFormat.ENCODING_PCM_16BIT -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> writeAudioDataToTrack() >>> pcmFormat = ENCODING_PCM_16BIT, try to write in 16BIT!")
                    audioTrack.write(ComkitTypeCastUtils.doubleArrayToShortArray(audioData).toShortArray(), 0, audioData.size, AudioTrack.WRITE_BLOCKING)
                }

                AudioFormat.ENCODING_PCM_8BIT -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> writeAudioDataToTrack() >>> pcmFormat = ENCODING_PCM_8BIT, try to write in 8BIT!")
                    audioTrack.write(ComkitTypeCastUtils.doubleArrayToByteArray(audioData).toByteArray(), 0, audioData.size, AudioTrack.WRITE_BLOCKING)
                }

                else -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> writeAudioDataToTrack() >>> else in when, try to write failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(exception)
            return false
        }

        return true
    }

    /**
     * MscdAudioPlayer.stopAllPlayAudio()
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
    fun stopAllPlayAudio(): Boolean {
        val audioTrack = this@MscdAudioPlayer.config.audioTrack

        logcatAudioTrackState("stopAllPlayAudio", audioTrack, "Before try stop() or flush() or release().")

        try {
            when (audioTrack.playState) {
                AudioTrack.PLAYSTATE_PLAYING -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> stopAllPlayAudio() >>> playState = PLAYSTATE_PLAYING, try to stop and release!")
                    audioTrack.stop()
                    audioTrack.release()
                }

                AudioTrack.PLAYSTATE_PAUSED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> stopAllPlayAudio() >>> playState = PLAYSTATE_PAUSED, try to flush, stop and release!")
                    audioTrack.flush()
                    audioTrack.stop()
                    audioTrack.release()
                }

                AudioTrack.PLAYSTATE_STOPPED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> stopAllPlayAudio() >>> playState = PLAYSTATE_STOPPED, try to release!")
                    audioTrack.release()
                }

                else -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> stopAllPlayAudio() >>> else in when, try to stop failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(exception)
            return false
        }

        logcatAudioTrackState("stopAllPlayAudio", audioTrack, "After try stop() or flush() or release().")
        return true
    }

    /**
     * MscdAudioPlayer.stopCurrentPlayAudio()
     * <Description> Try to stop the current audio play of AudioTrack.
     * <Details> You can stop the audio play of the instance of AudioTrack in current thread and continue the next audio play in the thread queue of AsyncTask when you are trying to play several audio one by one by the same AudioTrack instance.
     *
     * @param
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun stopCurrentPlayAudio(): Boolean {
        val audioTrack = this@MscdAudioPlayer.config.audioTrack

        logcatAudioTrackState("stopCurrentPlayAudio", audioTrack, "Before try stop() or flush() or release().")

        try {
            when (audioTrack.playState) {
                AudioTrack.PLAYSTATE_PLAYING -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> stopCurrentPlayAudio() >>> playState = PLAYSTATE_PLAYING, try to stop!")
                    audioTrack.stop()
                }

                AudioTrack.PLAYSTATE_PAUSED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> stopCurrentPlayAudio() >>> playState = PLAYSTATE_PAUSED, try to stop!")
                    audioTrack.stop()
                }

                AudioTrack.PLAYSTATE_STOPPED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> stopCurrentPlayAudio() >>> playState = PLAYSTATE_STOPPED, try to stop failed!")
                }

                else -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> stopCurrentPlayAudio() >>> else in when, try to stop failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(exception)
            return false
        }

        logcatAudioTrackState("stopCurrentPlayAudio", audioTrack, "After try stop().")
        return true
    }

    /**
     * MscdAudioPlayer.pausePlayAudio()
     * <Description> Try to pause audio play of AudioTrack.
     * <Details>
     *
     * @attention There is a extraordinary usage: You can stop the audio play of the instance of AudioTrack in current thread and continue the next audio play in the thread queue of AsyncTask when you are trying to play several audio one by one by the same AudioTrack instance.
     * There is a method you can use to get the same result {@link #stopCurrentPlayAudio()}.
     *
     * @param
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun pausePlayAudio(): Boolean {
        val audioTrack = this@MscdAudioPlayer.config.audioTrack

        logcatAudioTrackState("pausePlayAudio", audioTrack, "Before try pause().")

        try {
            when (audioTrack.playState) {
                AudioTrack.PLAYSTATE_PLAYING -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> pausePlayAudio() >>> playState = PLAYSTATE_PLAYING, try to pause!")
                    audioTrack.pause()
                }

                AudioTrack.PLAYSTATE_PAUSED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> pausePlayAudio() >>> playState = PLAYSTATE_PAUSED, try to pause failed!")
                    return false
                }

                AudioTrack.PLAYSTATE_STOPPED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> pausePlayAudio() >>> playState = PLAYSTATE_STOPPED, try to pause failed!")
                    return false
                }

                else -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> pausePlayAudio() >>> else in when, try to pause failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(exception)
            return false
        }

        logcatAudioTrackState("pausePlayAudio", audioTrack, "After try pause().")
        return true
    }

    /**
     * MscdAudioPlayer.resumePlayAudio()
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
    fun resumePlayAudio(): Boolean {
        val audioTrack = this@MscdAudioPlayer.config.audioTrack

        logcatAudioTrackState("resumePlayAudio", audioTrack, "Before try to resume.")

        try {
            when (audioTrack.state) {
                AudioTrack.STATE_UNINITIALIZED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> resumePlayAudio() >>> state = STATE_UNINITIALIZED, try to resume failed!")
                    return false
                }

                AudioTrack.STATE_INITIALIZED -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> resumePlayAudio() >>> state = STATE_INITIALIZED, judge play state!")

                    when (audioTrack.playState) {
                        AudioTrack.PLAYSTATE_PLAYING -> {
                            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> resumePlayAudio() >>> playState = PLAYSTATE_PLAYING, try to resume failed!")
                            return false
                        }

                        AudioTrack.PLAYSTATE_PAUSED -> {
                            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> resumePlayAudio() >>> playState = PLAYSTATE_PAUSED, try to resume!")
                            audioTrack.play()
                        }

                        AudioTrack.PLAYSTATE_STOPPED -> {
                            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> resumePlayAudio() >>> playState = PLAYSTATE_STOPPED, try to resume failed!")
                            return false
                        }

                        else -> {
                            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> resumePlayAudio() >>> else in when, try to resume failed!")
                            return false
                        }
                    }
                }

                else -> {
                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> resumePlayAudio() >>> else in when, try to resume failed!")
                    return false
                }
            }
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(exception)
            return false
        }

        logcatAudioTrackState("resumePlayAudio", audioTrack, "After try to resume.")
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
        val audioTrack = this@MscdAudioPlayer.config.audioTrack

        logcatAudioTrackState("startPlayAudio", audioTrack, "Before try release().")

        try {
            audioTrack.release()
        } catch (exception: IllegalStateException) {
            ComkitLogcatUtils.e(exception)
            return false
        }

        logcatAudioTrackState("startPlayAudio", audioTrack, "After try release().")
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
        ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> " +
                "$methodName() >>> " +
                "$audioTrack >>> " +
                "state = ${if (audioTrack.state == AudioTrack.STATE_INITIALIZED) "STATE_INITIALIZED" else if (audioTrack.state == AudioTrack.STATE_UNINITIALIZED) "STATE_UNINITIALIZED" else "NONE"}, " +
                "playStatus = ${if (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) "PLAYSTATE_PLAYING" else if (audioTrack.playState == AudioTrack.PLAYSTATE_PAUSED) "PLAYSTATE_PAUSED" else if (audioTrack.playState == AudioTrack.PLAYSTATE_STOPPED) "PLAYSTATE_STOPPED" else "NONE"}. $message")
    }
}
