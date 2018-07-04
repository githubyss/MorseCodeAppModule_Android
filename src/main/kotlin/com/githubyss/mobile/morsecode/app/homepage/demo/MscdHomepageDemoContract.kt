package com.githubyss.mobile.morsecode.app.homepage.demo

import com.githubyss.mobile.common.ui.basemvp.ComuiIBasePresenter
import com.githubyss.mobile.common.ui.basemvp.ComuiIBaseView

/**
 * MscdHomepageDemoContract.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdHomepageDemoContract {
    interface IView : ComuiIBaseView<IPresenter> {
    }

    interface IPresenter : ComuiIBasePresenter {
        fun onActivityResult(requestCode: Int, resultCode: Int)
    }
}