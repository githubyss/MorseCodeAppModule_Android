package com.githubyss.mobile.morsecode.app.homepage.demo

/**
 * MscdHomepageDemoPresenter.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdHomepageDemoPresenter(iView: MscdHomepageDemoContract.IView) {
    private var mscdHomepageDemoIView = iView
    private var mscdHomepageDemoIPresenter = object : MscdHomepageDemoContract.IPresenter {
        override fun onStandby() {
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int) {
        }
    }

    init {
        this@MscdHomepageDemoPresenter.mscdHomepageDemoIView.setPresenter(this@MscdHomepageDemoPresenter.mscdHomepageDemoIPresenter)
    }
}
