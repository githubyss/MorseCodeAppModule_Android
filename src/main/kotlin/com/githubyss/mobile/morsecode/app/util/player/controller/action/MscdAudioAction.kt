package com.githubyss.mobile.morsecode.app.util.player.controller.action

import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayer

/**
 * MscdAudioAction.kt
 * <Description>
 * <Details>
 *
 * @designPatterns State
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdAudioAction {
    fun startPlay(audioDataArray: Array<Float>, onAudioPlayListener: MscdAudioPlayer.OnAudioPlayListener)
    fun stopPlay()
    fun releaseResource()
}
