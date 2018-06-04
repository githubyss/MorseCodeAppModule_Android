package com.githubyss.mobile.morsecode.app.learningpage.charselectingpage

import android.widget.CheckBox
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.util.randommessage.MscdRandomStringGenerator
import com.githubyss.mobile.morsecode.app.util.randommessage.MscdRandomStringGeneratorConfig
import com.githubyss.mobile.morsecode.app.util.randommessage.MscdRulelessRandomStringStrategy

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

        override fun buildRandomTrainingMessage(chkBtnList: List<CheckBox>, messageLength: String, wordSize: String) {
            val selectedCharList = getSelectedCharList(chkBtnList)

            if (selectedCharList.isEmpty()) {
                mscdCharSelectingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdCharSelectingHintAtLeastOneChar))
                return
            }

            if (messageLength.isEmpty()) {
                mscdCharSelectingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdCharSelectingConfigMsgLengthHintCorrect))
                return
            }

            if (messageLength.toLong() == 0L) {
                mscdCharSelectingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdCharSelectingConfigMsgLengthHintNotZero))
                return
            }

            if (wordSize.isEmpty()) {
                mscdCharSelectingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdCharSelectingConfigWordSizeHintCorrect))
                return
            }

            if (wordSize.toInt() == 0) {
                mscdCharSelectingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdCharSelectingConfigWordSizeHintNotZero))
                return
            }


//            MscdRandomStringGeneratorConfig.Builder.setStrategy(MscdRegularRandomStringStrategy()).create()
            MscdRandomStringGeneratorConfig.Builder.setStrategy(MscdRulelessRandomStringStrategy()).create()
            MscdRandomStringGenerator.instance.startRandomStringGeneratorAsyncTask(
                    selectedCharList, messageLength.toLong(), wordSize.toInt(),
                    object : MscdRandomStringGenerator.OnRandomStringGenerateListener {
                        override fun onSucceeded(randomString: String) {
                            mscdCharSelectingIView.onRandomTrainingMessageBuilt(randomString)
                            mscdCharSelectingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdCharSelectingHintBuildingSucceeded))
                        }

                        override fun onFailed(failingInfo: String) {
                            mscdCharSelectingIView.showHint("${ComkitResUtils.getString(resId = R.string.mscdCharSelectingHintBuildingFailed)} $failingInfo")
                        }

                        override fun onCancelled() {
                            mscdCharSelectingIView.showHint(ComkitResUtils.getString(resId = R.string.mscdCharSelectingHintBuildingCancelled))
                        }
                    }
            )
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

        ComkitLogcatUtils.`object`(selectedCharList)

        return selectedCharList
    }
}
