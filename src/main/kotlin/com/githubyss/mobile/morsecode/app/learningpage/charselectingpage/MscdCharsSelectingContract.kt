package com.githubyss.mobile.morsecode.app.learningpage.charselectingpage

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
        fun gotoTrainingPage()
    }

    interface IPresenter : ComkitIBasePresenter {
        fun buildRandomTrainingMessage(chkBtnList: List<CheckBox>)
    }
}
