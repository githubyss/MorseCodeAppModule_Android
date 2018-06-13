package com.githubyss.mobile.morsecode.app.util.player.controller.action

import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayer

/**
 * MscdAudioOffAction.kt
 * <Description>
 * <Details>
 *
 * @designPatterns State
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdAudioOffAction : MscdAudioAction {
    override fun startPlay(audioDataArray: Array<Float>, onAudioPlayListener: MscdAudioPlayer.OnAudioPlayListener) {
    }

    override fun stopPlay() {
    }

    override fun releaseResource() {
    }
}
