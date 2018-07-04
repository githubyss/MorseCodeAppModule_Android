package com.githubyss.mobile.morsecode.app.learningpage.charselectingpage

import android.os.Bundle
import android.widget.CheckBox
import com.githubyss.mobile.common.ui.basemvp.ComuiIBasePresenter
import com.githubyss.mobile.common.ui.basemvp.ComuiIBaseView

/**
 * MscdCharsSelectingContract.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdCharsSelectingContract {
    interface IView : ComuiIBaseView<IPresenter> {
        fun showHint(hintStr: String?)
        fun onRandomTrainingMessageBuilt(randomTrainingMsgStr: String)
        fun gotoTrainingPage(bundle: Bundle)
    }

    interface IPresenter : ComuiIBasePresenter {
        fun buildRandomTrainingMessage(chkBtnList: List<CheckBox>, messageLength: String, wordSize: String, needRuleless: Boolean)
        fun buildGotoTrainingPageBundle(trainingMsgStr: String, ditDuration: String)
    }
}
