package com.githubyss.mobile.morsecode.app.learningpage.charselectingpage

import android.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.githubyss.mobile.common.kit.constant.ComkitFontConstants
import com.githubyss.mobile.common.kit.util.ComkitFontUtils
import com.githubyss.mobile.common.kit.util.ComkitToastUtils
import com.githubyss.mobile.common.kit.util.checker.ComkitNumberCheckUtils
import com.githubyss.mobile.common.kit.util.formatter.ComkitNumberFormatUtils
import com.githubyss.mobile.common.ui.basemvp.ComuiBaseFragment
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.util.randommessage.MscdRandomStringGenerator
import kotlinx.android.synthetic.main.mscd_fragment_chars_selecting.*

/**
 * MscdCharsSelectingFragment.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
@Route(path = "/morsecode/app/learningpage/charselectingpage/MscdCharsSelectingFragment")
class MscdCharsSelectingFragment : ComuiBaseFragment() {
    companion object {
        val TAG = "MscdCharsSelectingFragment"
    }

    private lateinit var rootView: View
    private lateinit var chkBtnList: MutableList<CheckBox>

    private lateinit var mscdCharsSelectingIPresenter: MscdCharsSelectingContract.IPresenter
    private var mscdCharSelectingIView = object : MscdCharsSelectingContract.IView {
        override fun setPresenter(iPresenter: MscdCharsSelectingContract.IPresenter) {
            mscdCharsSelectingIPresenter = iPresenter
        }

        override fun showHint(hintStr: String?) {
            changeBtnStatus(btnConfirm, true)
            ComkitToastUtils.showMessage(msgStr = hintStr ?: "")
        }

        override fun onRandomTrainingMessageBuilt(randomTrainingMsgStr: String) {
            mscdCharsSelectingIPresenter.buildGotoTrainingPageBundle(randomTrainingMsgStr, etDitDuration.text.toString())
        }

        override fun gotoTrainingPage(bundle: Bundle) {
            changeBtnStatus(btnConfirm, true)
            val fragment = ARouter.getInstance().build("/morsecode/app/learningpage/traininggpage/MscdTrainingFragment").navigation() as Fragment
            fragment.arguments = bundle
            replaceFragment(fragment, "MscdTrainingFragment", true)
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.btnConfirm -> {
                changeBtnStatus(btnConfirm, false)
                mscdCharsSelectingIPresenter.buildRandomTrainingMessage(chkBtnList, etMessageLength.text.toString(), etWordSize.text.toString(), tglBtnRandomStringGenerateStrategy.isChecked)
            }

            else -> {
            }
        }
    }

    private val onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            when (v.id) {
                R.id.etKeySpeed -> {
                    etKeySpeed.addTextChangedListener(etKeySpeedWatcher)
                    etDitDuration.removeTextChangedListener(etDitDurationWatcher)
                }

                R.id.etDitDuration -> {
                    etDitDuration.addTextChangedListener(etDitDurationWatcher)
                    etKeySpeed.removeTextChangedListener(etKeySpeedWatcher)
                }

                else -> {
                }
            }
        }
    }

    private val etKeySpeedWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etKeySpeed.isFocused) {
                when {
                    s.isNullOrEmpty() -> {
                        etDitDuration.setText("")
                    }

                    ComkitNumberCheckUtils.checkConventionalIntegerNonNegative(s.toString()) -> {
                        if (s.toString().toLong() == 0L) {
                            etDitDuration.setText("0")
                        } else {
                            etDitDuration.setText(convertSpeedAndDuration(s.toString().toLong()).toString())
                        }
                    }

                    else -> {
                        etKeySpeed.setText(ComkitNumberFormatUtils.formatConventionalIntegerNonNegative(s.toString()))
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    private val etDitDurationWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etDitDuration.isFocused) {
                when {
                    s.isNullOrEmpty() -> {
                        etKeySpeed.setText("")
                    }

                    ComkitNumberCheckUtils.checkConventionalIntegerNonNegative(s.toString()) -> {
                        if (s.toString().toLong() == 0L) {
                            etKeySpeed.setText("0")
                        } else {
                            etKeySpeed.setText(convertSpeedAndDuration(s.toString().toLong()).toString())
                        }
                    }

                    else -> {
                        etDitDuration.setText(ComkitNumberFormatUtils.formatConventionalIntegerNonNegative(s.toString()))
                    }
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }


    private fun convertSpeedAndDuration(input: Long): Long {
        return 1300 / input
    }


    override fun bindPresenter() {
        MscdCharsSelectingPresenter(mscdCharSelectingIView)
    }

    override fun initView() {
        btnConfirm.setOnClickListener(onClickListener)

        etKeySpeed.onFocusChangeListener = onFocusChangeListener
        etDitDuration.onFocusChangeListener = onFocusChangeListener

        etKeySpeed.addTextChangedListener(etKeySpeedWatcher)
        etDitDuration.addTextChangedListener(etDitDurationWatcher)

        tglBtnRandomStringGenerateStrategy.isChecked = false

        ComkitFontUtils.replaceFontFromAsset(llCharSelectContainer, ComkitFontConstants.FontPath.MONOSPACE_DEFAULT)
        ComkitFontUtils.replaceFontFromAsset(llConfigContainer, ComkitFontConstants.FontPath.MONOSPACE_DEFAULT)
    }

    override fun initData() {
        chkBtnList = ArrayList()

        for (idxOfOutsideLlContainer in 0 until llCharSelectContainer.childCount) {
            val charSelectBtnContainer = llCharSelectContainer.getChildAt(idxOfOutsideLlContainer) as LinearLayout
            for (idxOfInsideBtnContainer in 0 until charSelectBtnContainer.childCount) {
                chkBtnList.add(charSelectBtnContainer.getChildAt(idxOfInsideBtnContainer) as CheckBox)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindPresenter()
        mscdCharsSelectingIPresenter.onStandby()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater?.inflate(R.layout.mscd_fragment_chars_selecting, container, false) ?: rootView
        return rootView
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle(R.string.mscdCharSelectingTitle)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (activity.isFinishing) {
            MscdRandomStringGenerator.instance.stopGenerateRandomString()
        }
    }
}
