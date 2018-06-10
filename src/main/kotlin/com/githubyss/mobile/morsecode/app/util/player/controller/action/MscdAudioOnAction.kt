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
    override fun startPlay(audioDataArray: Array<Float>) {
        MscdAudioPlayer.instance.startAudioPlayerAsyncTask(audioDataArray)
    }

    override fun stopPlay() {
        MscdAudioPlayer.instance.cancelAudioPlayerAsyncTask()
        MscdAudioPlayer.instance.stopAllPlayAudio()
    }

    override fun releaseResource() {
        MscdAudioPlayer.instance.releaseAudioTrack()
    }
}
