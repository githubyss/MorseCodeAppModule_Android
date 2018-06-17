package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

import android.view.View
import com.githubyss.mobile.common.kit.base.ComkitIBasePresenter
import com.githubyss.mobile.common.kit.base.ComkitIBaseView

/**
 * MscdTrainingContract.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdTrainingContract {
    interface IView : ComkitIBaseView<IPresenter> {
        fun showHint(hintStr: String)
        fun onAudioDataBuilt(audioDataArray: Array<Float>)
        fun onTypewriterDataBuilt(typewriterDataList: List<Int>)
        fun onPlayFinished()
    }

    interface IPresenter : ComkitIBasePresenter {
        fun buildPlayData(trainingMsgStr: String)
        fun startPlay(audioData: Array<Float>, flashlightData: Array<Any>, vibratorData: Array<Any>, typewriterData: String, typewriterDataDuration: List<Int>, typewriterView: View)
    }
}
