package com.githubyss.mobile.morsecode.app.util.player.controller

/**
 * MscdPlayerPowerController.kt
 * <Description>
 * <Details>
 *
 * @designPatterns State
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdPlayerPowerController {
    fun audioOn()
    fun audioOff()

    fun flashlightOn()
    fun flashlightOff()

    fun vibratorOn()
    fun vibratorOff()

    fun typewriterOn()
    fun typewriterOff()
}
