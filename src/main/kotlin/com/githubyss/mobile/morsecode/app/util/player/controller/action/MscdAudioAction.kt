package com.githubyss.mobile.morsecode.app.util.player.controller.action

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
    fun startPlay(audioDataArray: Array<Float>)
    fun stopPlay()
    fun releaseResource()
}
