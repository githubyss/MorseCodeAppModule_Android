package com.githubyss.mobile.morsecode.app.util.player.controller

import com.githubyss.mobile.morsecode.app.constant.MscdStatusConstants
import com.githubyss.mobile.morsecode.app.global.MscdPlayModeGlobalInfo
import com.githubyss.mobile.morsecode.app.util.player.controller.action.*

/**
 * MscdPlayerController.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, State
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdPlayerController private constructor() : MscdPlayerPowerController {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdPlayerController()
    }


    private var audioAction = MscdAudioOffAction() as MscdPlayerAction
    private var flashlightAction = MscdFlashlightOffAction() as MscdPlayerAction
    private var vibratorAction = MscdVibratorOffAction() as MscdPlayerAction


    fun init() {
        audioAction = if (MscdPlayModeGlobalInfo.audioStatus == MscdStatusConstants.PlayModeStatus.AUDIO_ON) MscdAudioOnAction() else MscdAudioOffAction()
        flashlightAction = if (MscdPlayModeGlobalInfo.flashlightStatus == MscdStatusConstants.PlayModeStatus.FLASHLIGHT_ON) MscdFlashlightOnAction() else MscdFlashlightOffAction()
        vibratorAction = if (MscdPlayModeGlobalInfo.vibratorStatus == MscdStatusConstants.PlayModeStatus.VIBRATION_ON) MscdVibratorOnAction() else MscdVibratorOffAction()
    }

    fun startPlay(message: String, baseDelay: Long) {
        audioAction.startPlay(message, baseDelay)
        flashlightAction.startPlay(message, baseDelay)
        vibratorAction.startPlay(message, baseDelay)
    }

    fun stopPlay() {
        audioAction.stopPlay()
        flashlightAction.stopPlay()
        vibratorAction.stopPlay()
    }

    fun releaseResource() {
        audioAction.releaseResource()
        flashlightAction.releaseResource()
        vibratorAction.releaseResource()
    }


    override fun audioOn() {
        audioAction = MscdAudioOnAction()
    }

    override fun audioOff() {
        audioAction = MscdAudioOffAction()
    }

    override fun flashlightOn() {
        flashlightAction = MscdFlashlightOnAction()
    }

    override fun flashlightOff() {
        flashlightAction = MscdFlashlightOffAction()
    }

    override fun vibratorOn() {
        vibratorAction = MscdVibratorOnAction()
    }

    override fun vibratorOff() {
        vibratorAction = MscdVibratorOffAction()
    }
}
