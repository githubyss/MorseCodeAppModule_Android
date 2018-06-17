package com.githubyss.mobile.morsecode.app.util.player.controller

import android.view.View
import com.githubyss.mobile.morsecode.app.constant.MscdStatusConstants
import com.githubyss.mobile.morsecode.app.global.MscdPlayModeGlobalInfo
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayer
import com.githubyss.mobile.morsecode.app.util.player.controller.action.*
import com.githubyss.mobile.morsecode.app.util.player.typewriter.MscdTypewriterPlayStrategy

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
    private var typewriterAction = MscdTypewriterOffAction() as MscdTypewriterAction


    fun init() {
        audioAction = if (MscdPlayModeGlobalInfo.audioStatus == MscdStatusConstants.PlayModeStatus.AUDIO_ON) MscdAudioOnAction() else MscdAudioOffAction()
        flashlightAction = if (MscdPlayModeGlobalInfo.flashlightStatus == MscdStatusConstants.PlayModeStatus.FLASHLIGHT_ON) MscdFlashlightOnAction() else MscdFlashlightOffAction()
        vibratorAction = if (MscdPlayModeGlobalInfo.vibratorStatus == MscdStatusConstants.PlayModeStatus.VIBRATION_ON) MscdVibratorOnAction() else MscdVibratorOffAction()
        typewriterAction = if (MscdPlayModeGlobalInfo.typewriterStatus == MscdStatusConstants.PlayModeStatus.TYPEWRITER_ON) MscdTypewriterOnAction() else MscdTypewriterOffAction()
    }

    fun startPlay(
            audioDataArray: Array<Float>, onAudioPlayListener: MscdAudioPlayer.OnAudioPlayListener,
            flashlightDataArray: Array<Any>,
            vibratorDataArray: Array<Any>,
            typewriterDataStr: String, typewriterDurationList: List<Int>, typewriterView: View, onTypewriterPlayListener: MscdTypewriterPlayStrategy.OnTypewriterPlayListener) {
        audioAction.startPlay(audioDataArray, onAudioPlayListener)
        flashlightAction.startPlay(flashlightDataArray)
        vibratorAction.startPlay(vibratorDataArray)
        typewriterAction.startPlay(typewriterDataStr, typewriterDurationList, typewriterView, onTypewriterPlayListener)
    }

    fun stopPlay() {
        audioAction.stopPlay()
        flashlightAction.stopPlay()
        vibratorAction.stopPlay()
        typewriterAction.stopPlay()
    }

    fun releaseResource() {
        audioAction.releaseResource()
        flashlightAction.releaseResource()
        vibratorAction.releaseResource()
        typewriterAction.releaseResource()
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

    override fun typewriterOn() {
        typewriterAction = MscdTypewriterOnAction()
    }

    override fun typewriterOff() {
        typewriterAction = MscdTypewriterOffAction()
    }
}
