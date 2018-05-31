package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

/**
 * MscdTrainingPresenter.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTrainingPresenter(iView: MscdTrainingContract.IView) {
    private var mscdTrainingIView = iView
    private var mscdTrainingIPresenter = object : MscdTrainingContract.IPresenter {
        override fun onStandby() {
        }
    }

    init {
        this@MscdTrainingPresenter.mscdTrainingIView.setPresenter(this@MscdTrainingPresenter.mscdTrainingIPresenter)
    }
}
