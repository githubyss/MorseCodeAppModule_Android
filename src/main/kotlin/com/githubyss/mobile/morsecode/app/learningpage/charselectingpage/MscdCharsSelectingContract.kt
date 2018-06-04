package com.githubyss.mobile.morsecode.app.learningpage.charselectingpage

import android.os.Bundle
import android.widget.CheckBox
import com.githubyss.mobile.common.kit.base.ComkitIBasePresenter
import com.githubyss.mobile.common.kit.base.ComkitIBaseView

/**
 * MscdCharsSelectingContract.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdCharsSelectingContract {
    interface IView : ComkitIBaseView<IPresenter> {
        fun showHint(hint: String)
        fun onRandomTrainingMessageBuilt(message: String)
        fun gotoTrainingPage(bundle: Bundle)
    }

    interface IPresenter : ComkitIBasePresenter {
        fun buildRandomTrainingMessage(chkBtnList: List<CheckBox>, messageLength: String, wordSize: String)
    }
}
