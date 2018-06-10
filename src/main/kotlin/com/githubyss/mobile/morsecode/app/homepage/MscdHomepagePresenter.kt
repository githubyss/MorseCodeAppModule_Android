package com.githubyss.mobile.morsecode.app.homepage

/**
 * MscdHomepagePresenter.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdHomepagePresenter(iView: MscdHomepageContract.IView) {
    private var mscdHomepageIView = iView
    private var mscdHomepageIPresenter = object : MscdHomepageContract.IPresenter {
        override fun onStandby() {
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int) {
        }
    }

    init {
        mscdHomepageIView.setPresenter(mscdHomepageIPresenter)
    }
}
