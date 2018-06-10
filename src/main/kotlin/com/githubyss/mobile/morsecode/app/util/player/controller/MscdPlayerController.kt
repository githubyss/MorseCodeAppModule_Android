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


    private var audioAction = MscdAudioOffAction() as MscdAudioAction
    private var flashlightAction = MscdFlashlightOffAction() as MscdFlashlightAction
    private var vibratorAction = MscdVibratorOffAction() as MscdVibratorAction


    fun init() {
        audioAction = if (MscdPlayModeGlobalInfo.audioStatus == MscdStatusConstants.PlayModeStatus.AUDIO_ON) MscdAudioOnAction() else MscdAudioOffAction()
        flashlightAction = if (MscdPlayModeGlobalInfo.flashlightStatus == MscdStatusConstants.PlayModeStatus.FLASHLIGHT_ON) MscdFlashlightOnAction() else MscdFlashlightOffAction()
        vibratorAction = if (MscdPlayModeGlobalInfo.vibratorStatus == MscdStatusConstants.PlayModeStatus.VIBRATION_ON) MscdVibratorOnAction() else MscdVibratorOffAction()
    }

    fun startPlay(audioData: Array<Float>, flashlightData: Array<Any>, vibratorData: Array<Any>) {
        audioAction.startPlay(audioData)
        flashlightAction.startPlay(flashlightData)
        vibratorAction.startPlay(vibratorData)
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
