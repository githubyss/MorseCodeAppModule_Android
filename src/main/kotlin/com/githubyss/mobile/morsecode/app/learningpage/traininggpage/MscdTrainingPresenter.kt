package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

import android.view.View
import com.githubyss.mobile.common.kit.resource.ComkitResUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.kit.controller.MckitPlayerController
import com.githubyss.mobile.morsecode.kit.player.audio.generator.MckitAudioDataGenerateStrategy
import com.githubyss.mobile.morsecode.kit.player.audio.generator.MckitAudioDataGenerator
import com.githubyss.mobile.morsecode.kit.player.audio.player.MckitAudioPlayer
import com.githubyss.mobile.morsecode.kit.player.typewriter.generator.MckitTypewriterDurationGenerateStrategy
import com.githubyss.mobile.morsecode.kit.player.typewriter.generator.MckitTypewriterDurationGenerator
import com.githubyss.mobile.morsecode.kit.player.typewriter.player.MckitTypewriterPlayStrategy

/**
 * MscdTrainingPresenter.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTrainingPresenter(private val mscdTrainingIView: MscdTrainingContract.IView) : MscdTrainingContract.IPresenter {
    init {
        mscdTrainingIView.setPresenter(this)
    }


    // ---------- ---------- ---------- IPresenter ---------- ---------- ----------

    override fun onStandby() {
    }

    override fun buildPlayData(trainingMsgStr: String) {
        MckitAudioDataGenerator.instance.startGenerateAudioData(
                trainingMsgStr,
                object : MckitAudioDataGenerateStrategy.OnAudioDataGenerateListener {
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

        MckitTypewriterDurationGenerator.instance.startGenerateTypewriteDuration(
                trainingMsgStr,
                object : MckitTypewriterDurationGenerateStrategy.OnTypewriterDurationGenerateListener {
                    override fun onSucceeded(typewriterDurationList: List<Int>) {
                        mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdTypewriterDurationGenerateSucceeded))
                        mscdTrainingIView.onTypewriterDurationBuilt(typewriterDurationList)
                    }

                    override fun onFailed(failingInfo: String) {
                        mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdTypewriterDurationGenerateFailed))
                    }

                    override fun onCancelled() {
                        mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdTypewriterDurationGenerateCancelled))
                    }
                }
        )
    }

    override fun startPlay(audioDataArray: Array<Float>, flashlightDataArray: Array<Any>, vibratorDataArray: Array<Any>, typewriterDataStr: String, typewriterDurationList: List<Int>, typewriterView: View) {
        MckitPlayerController.instance.startPlay(
                audioDataArray,
                object : MckitAudioPlayer.OnAudioPlayListener {
                    override fun onSucceeded() {
                        mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdAudioPlaySucceeded))
                        mscdTrainingIView.onPlayFinished()
                    }

                    override fun onFailed(failingInfo: String) {
                        mscdTrainingIView.showHint("${ComkitResUtils.getString(resId = R.string.mscdAudioPlayFailed)} $failingInfo")
                        mscdTrainingIView.onPlayFinished()
                    }

                    override fun onCancelled() {
                        mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdAudioPlayCancelled))
                        mscdTrainingIView.onPlayFinished()
                    }
                },
                flashlightDataArray,
                vibratorDataArray,
                typewriterDataStr,
                typewriterDurationList,
                typewriterView,
                object : MckitTypewriterPlayStrategy.OnTypewriterPlayListener {
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
