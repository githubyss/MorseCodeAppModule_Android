package com.githubyss.mobile.morsecode.app.homepage.demo

import com.githubyss.mobile.common.kit.base.ComkitIBasePresenter
import com.githubyss.mobile.common.kit.base.ComkitIBaseView

/**
 * MscdHomepageDemoContract.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdHomepageDemoContract {
    interface IView : ComkitIBaseView<IPresenter> {
    }

    interface IPresenter : ComkitIBasePresenter {
        fun onActivityResult(requestCode: Int, resultCode: Int)
    }
}