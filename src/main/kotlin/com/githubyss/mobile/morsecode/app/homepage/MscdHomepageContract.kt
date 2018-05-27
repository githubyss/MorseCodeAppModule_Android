package com.githubyss.mobile.morsecode.app.homepage

import com.githubyss.mobile.common.kit.base.ComkitIBasePresenter
import com.githubyss.mobile.common.kit.base.ComkitIBaseView

/**
 * MscdHomepageContract.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdHomepageContract {
    interface IView : ComkitIBaseView<IPresenter> {
        fun gotoTranslatorPage()
        fun gotoLearningPage()
    }

    interface IPresenter : ComkitIBasePresenter {
        fun onActivityResult(requestCode: Int, resultCode: Int)
    }
}
