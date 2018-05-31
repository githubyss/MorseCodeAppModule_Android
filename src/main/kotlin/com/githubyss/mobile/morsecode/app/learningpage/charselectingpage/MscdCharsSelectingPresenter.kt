package com.githubyss.mobile.morsecode.app.learningpage.charselectingpage

import android.widget.CheckBox
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils

/**
 * MscdCharsSelectingPresenter.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdCharsSelectingPresenter(iView: MscdCharsSelectingContract.IView) {
    private var mscdCharSelectingIView = iView
    private var mscdCharSelectingIPresenter = object : MscdCharsSelectingContract.IPresenter {
        override fun onStandby() {
        }

        override fun buildRandomTrainingMessage(chkBtnList: List<CheckBox>) {
            val selectedCharList = getSelectedCharList(chkBtnList)

            if (selectedCharList.isEmpty()) {
                return
            }


        }
    }

    init {
        this@MscdCharsSelectingPresenter.mscdCharSelectingIView.setPresenter(this@MscdCharsSelectingPresenter.mscdCharSelectingIPresenter)
    }


    private fun getSelectedCharList(chkBtnList: List<CheckBox>): List<String> {
        if (chkBtnList.isEmpty()) {
            return emptyList()
        }

        val selectedCharList = ArrayList<String>()
        chkBtnList
                .filter { it.isChecked }
                .mapTo(selectedCharList) { it.text.toString() }

        ComkitLogcatUtils.`object`(chkBtnList)

        return selectedCharList
    }
}
