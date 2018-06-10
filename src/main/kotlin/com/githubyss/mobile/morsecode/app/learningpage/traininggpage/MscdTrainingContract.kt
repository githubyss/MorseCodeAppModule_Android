package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

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
    }

    interface IPresenter : ComkitIBasePresenter {
        fun buildPlayerData(trainingMsgStr:String)
    }
}
