package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

import android.view.View
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerateStrategy
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerator
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayer
import com.githubyss.mobile.morsecode.app.util.player.controller.MscdPlayerController
import com.githubyss.mobile.morsecode.app.util.player.typewriter.MscdTypewriterDataGenerateStrategy
import com.githubyss.mobile.morsecode.app.util.player.typewriter.MscdTypewriterDataGenerator
import com.githubyss.mobile.morsecode.app.util.player.typewriter.MscdTypewriterPlayStrategy

/**
 * MscdTrainingPresenter.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTrainingPresenter(iView: MscdTrainingContract.IView) {
    private var mscdTrainingIView = iView
    private var mscdTrainingIPresenter = object : MscdTrainingContract.IPresenter {
        override fun onStandby() {
        }

        override fun buildPlayData(trainingMsgStr: String) {
            MscdAudioDataGenerator.instance.startGenerateAudioData(
                    trainingMsgStr,
                    object : MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener {
                        override fun onSucceeded(audioDataArray: Array<Float>) {
                            mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdAudioDataGenerateSucceeded))
                            mscdTrainingIView.onAudioDataBuilt(audioDataArray)
                        }

                        override fun onFailed(failingInfo: String) {
                            mscdTrainingIView.showHint("${ComkitResUtils.getString(resId = R.string.mscdAudioDataGenerateFailed)} $failingInfo")
                        }

                        override fun onCancelled() {
                            mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdAudioDataGenerateCancelled))
                        }
                    }
            )

            MscdTypewriterDataGenerator.instance.startGenerateTypewriteData(
                    trainingMsgStr,
                    object : MscdTypewriterDataGenerateStrategy.OnTypewriterDataGenerateListener {
                        override fun onSucceeded(typewriterDataList: List<Int>) {
                            mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdTypewriterDataGenerateSucceeded))
                            mscdTrainingIView.onTypewriterDataBuilt(typewriterDataList)
                        }

                        override fun onFailed(failingInfo: String) {
                            mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdTypewriterDataGenerateFailed))
                        }

                        override fun onCancelled() {
                            mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdTypewriterDataGenerateCancelled))
                        }
                    }
            )
        }

        override fun startPlay(audioData: Array<Float>, flashlightData: Array<Any>, vibratorData: Array<Any>, typewriterData: String, typewriterDataDuration: List<Int>, typewriterView: View) {
            MscdPlayerController.instance.startPlay(
                    audioData,
                    object : MscdAudioPlayer.OnAudioPlayListener {
                        override fun onSucceeded() {
                            mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdAudioPlaySucceeded))
                        }

                        override fun onFailed(failingInfo: String) {
                            mscdTrainingIView.showHint("${ComkitResUtils.getString(resId = R.string.mscdAudioPlayFailed)} $failingInfo")
                        }

                        override fun onCancelled() {
                            mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdAudioPlayCancelled))
                        }
                    },
                    flashlightData,
                    vibratorData,
                    typewriterData,
                    typewriterDataDuration,
                    typewriterView,
                    object : MscdTypewriterPlayStrategy.OnTypewriterPlayListener {
                        override fun onSucceeded() {
                        }

                        override fun onFailed(failingInfo: String) {
                        }

                        override fun onCancelled() {
                        }
                    }
            )
        }
    }

    init {
        mscdTrainingIView.setPresenter(mscdTrainingIPresenter)
    }
}
