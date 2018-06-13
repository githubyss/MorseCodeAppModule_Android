package com.githubyss.mobile.morsecode.app.util.player.controller.action

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
class MscdAudioOnAction : MscdAudioAction {
    override fun startPlay(audioDataArray: Array<Float>, onAudioPlayListener: MscdAudioPlayer.OnAudioPlayListener) {
        MscdAudioPlayer.instance.startPlayAudio(audioDataArray, onAudioPlayListener)
    }

    override fun stopPlay() {
        MscdAudioPlayer.instance.stopPlayAudio()
        MscdAudioPlayer.instance.stopAllAudioTrack()
    }

    override fun releaseResource() {
        MscdAudioPlayer.instance.releaseAudioTrack()
    }
}
