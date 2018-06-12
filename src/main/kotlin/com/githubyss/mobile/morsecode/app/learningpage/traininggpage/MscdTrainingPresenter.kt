package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerateStrategy
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerator

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

        override fun buildPlayerData(trainingMsgStr: String) {
            MscdAudioDataGenerator.instance.startGenerateAudioData(
                    trainingMsgStr,
                    object : MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener {
                        override fun onSucceeded(audioDataArray: Array<Float>) {
                            mscdTrainingIView.onAudioDataBuilt(audioDataArray)
                            mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdAudioDataGenerateSucceeded))
                        }

                        override fun onFailed(failingInfo: String) {
                            mscdTrainingIView.showHint("${ComkitResUtils.getString(resId = R.string.mscdAudioDataGenerateFailed)} $failingInfo")
                        }

                        override fun onCancelled() {
                            mscdTrainingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdAudioDataGenerateCancelled))
                        }
                    }
            )
        }
    }

    init {
        mscdTrainingIView.setPresenter(mscdTrainingIPresenter)
    }
}
