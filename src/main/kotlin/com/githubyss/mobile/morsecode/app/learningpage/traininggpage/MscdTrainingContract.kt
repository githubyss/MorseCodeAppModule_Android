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
        fun onTypewriterDurationBuilt(typewriterDurationList: List<Int>)
        fun onPlayFinished()
    }

    interface IPresenter : ComkitIBasePresenter {
        fun buildPlayData(trainingMsgStr: String)
        fun startPlay(audioDataArray: Array<Float>, flashlightDataArray: Array<Any>, vibratorDataArray: Array<Any>, typewriterDataStr: String, typewriterDurationList: List<Int>, typewriterView: View)
    }
}
