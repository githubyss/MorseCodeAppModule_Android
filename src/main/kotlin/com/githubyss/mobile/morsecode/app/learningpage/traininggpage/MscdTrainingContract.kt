package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

import android.view.View
import com.githubyss.mobile.common.ui.basemvp.ComuiIBasePresenter
import com.githubyss.mobile.common.ui.basemvp.ComuiIBaseView

/**
 * MscdTrainingContract.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdTrainingContract {
    interface IView : ComuiIBaseView<IPresenter> {
        fun showHint(hintStr: String?)
        fun onAudioDataBuilt(audioDataArray: Array<Float>)
        fun onTypewriterDurationBuilt(typewriterDurationList: List<Int>)
        fun onPlayFinished()
    }

    interface IPresenter : ComuiIBasePresenter {
        fun buildPlayData(trainingMsgStr: String)
        fun startPlay(audioDataArray: Array<Float>, flashlightDataArray: Array<Any>, vibratorDataArray: Array<Any>, typewriterDataStr: String, typewriterDurationList: List<Int>, typewriterView: View)
    }
}
