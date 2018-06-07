package com.githubyss.mobile.morsecode.app.util.player.controller.action

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayer

/**
 * MscdAudioOnAction.kt
 * <Description>
 * <Details>
 *
 * @designPatterns State
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdAudioOnAction : MscdPlayerAction {
    override fun startPlay(message: String, baseDelay: Long) {
        MscdMorseCodeConverterConfig.Builder
                .setBaseDelay(baseDelay)
                .create()

        MscdAudioConfig.Builder
                .setAudioFrequencyInHz(880)
                .setAudioSampleRateInHz(44100)
                .setAudioChannelFormat(AudioFormat.CHANNEL_OUT_MONO)
                .setAudioEncodingPcmFormat(AudioFormat.ENCODING_PCM_16BIT)
                .setAudioStreamType(AudioManager.STREAM_MUSIC)
                .setAudioTrackMode(AudioTrack.MODE_STREAM)
                .create()

        MscdAudioPlayer.instance.startAudioPlayerAsyncTask(message)
    }

    override fun stopPlay() {
        MscdAudioPlayer.instance.stopAllPlayAudio()
    }

    override fun releaseResource() {
        MscdAudioPlayer.instance.releaseAudioTrack()
    }
}
