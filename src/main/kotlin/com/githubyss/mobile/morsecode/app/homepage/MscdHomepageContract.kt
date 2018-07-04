package com.githubyss.mobile.morsecode.app.homepage

import com.githubyss.mobile.common.ui.basemvp.ComuiIBasePresenter
import com.githubyss.mobile.common.ui.basemvp.ComuiIBaseView

/**
 * MscdHomepageContract.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdHomepageContract {
    interface IView : ComuiIBaseView<IPresenter> {
    }

    interface IPresenter : ComuiIBasePresenter {
        fun onActivityResult(requestCode: Int, resultCode: Int)
    }
}
