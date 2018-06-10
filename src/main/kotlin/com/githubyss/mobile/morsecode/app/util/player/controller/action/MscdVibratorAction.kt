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
interface MscdVibratorAction {
    fun startPlay(vibratorData: Array<Any>)
    fun stopPlay()
    fun releaseResource()
}