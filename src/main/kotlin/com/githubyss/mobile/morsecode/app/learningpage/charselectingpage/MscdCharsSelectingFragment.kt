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
import com.githubyss.mobile.common.kit.base.ComkitBaseFragment
import com.githubyss.mobile.common.kit.constant.ComkitFontConstants
import com.githubyss.mobile.common.kit.util.ComkitFontUtils
import com.githubyss.mobile.common.kit.util.ComkitToastUtils
import com.githubyss.mobile.common.kit.util.checker.ComkitNumberCheckUtils
import com.githubyss.mobile.common.kit.util.formatter.ComkitNumberFormatUtils
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
class MscdCharsSelectingFragment : ComkitBaseFragment() {
    companion object {
        val TAG = "MscdCharsSelectingFragment"
    }

    private lateinit var rootView: View
    private lateinit var chkBtnList: MutableList<CheckBox>

    private lateinit var mscdCharsSelectingIPresenter: MscdCharsSelectingContract.IPresenter
    private var mscdCharSelectingIView = object : MscdCharsSelectingContract.IView {
        override fun setPresenter(iPresenter: MscdCharsSelectingContract.IPresenter) {
            this@MscdCharsSelectingFragment.mscdCharsSelectingIPresenter = iPresenter
        }

        override fun showHint(hintStr: String) {
            changeBtnStatus(btnConfirm, true)
            ComkitToastUtils.showMessage(msgStr = hintStr)
        }

        override fun onRandomTrainingMessageBuilt(randomTrainingMsgStr: String) {
            changeBtnStatus(btnConfirm, true)
            this@MscdCharsSelectingFragment.mscdCharsSelectingIPresenter.buildGotoTrainingPageBundle(randomTrainingMsgStr, etDitDuration.text.toString())
        }

        override fun gotoTrainingPage(bundle: Bundle) {
            val fragment = ARouter.getInstance().build("/morsecode/app/learningpage/traininggpage/MscdTrainingFragment").navigation() as Fragment
            fragment.arguments = bundle
            replaceFragment(fragment, "MscdTrainingFragment", true)
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.btnConfirm -> {
                changeBtnStatus(btnConfirm, false)
                this@MscdCharsSelectingFragment.mscdCharsSelectingIPresenter.buildRandomTrainingMessage(chkBtnList, etMessageLength.text.toString(), etWordSize.text.toString(), tglBtnRandomStringGenerateStrategy.isChecked)
            }

            else -> {
            }
        }
    }

    private val onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            when (v.id) {
                R.id.etKeySpeed -> {
                    etKeySpeed.addTextChangedListener(this@MscdCharsSelectingFragment.etKeySpeedWatcher)
                    etDitDuration.removeTextChangedListener(this@MscdCharsSelectingFragment.etDitDurationWatcher)
                }

                R.id.etDitDuration -> {
                    etDitDuration.addTextChangedListener(this@MscdCharsSelectingFragment.etDitDurationWatcher)
                    etKeySpeed.removeTextChangedListener(this@MscdCharsSelectingFragment.etKeySpeedWatcher)
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
                            etDitDuration.setText(this@MscdCharsSelectingFragment.convertSpeedAndDuration(s.toString().toLong()).toString())
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
                            etKeySpeed.setText(this@MscdCharsSelectingFragment.convertSpeedAndDuration(s.toString().toLong()).toString())
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
        MscdCharsSelectingPresenter(this@MscdCharsSelectingFragment.mscdCharSelectingIView)
    }

    override fun initView() {
        btnConfirm.setOnClickListener(this@MscdCharsSelectingFragment.onClickListener)

        etKeySpeed.onFocusChangeListener = this@MscdCharsSelectingFragment.onFocusChangeListener
        etDitDuration.onFocusChangeListener = this@MscdCharsSelectingFragment.onFocusChangeListener

        etKeySpeed.addTextChangedListener(this@MscdCharsSelectingFragment.etKeySpeedWatcher)
        etDitDuration.addTextChangedListener(this@MscdCharsSelectingFragment.etDitDurationWatcher)

        tglBtnRandomStringGenerateStrategy.isChecked = false

        ComkitFontUtils.replaceFontFromAsset(llCharSelectingContainer, ComkitFontConstants.FontPath.MONOSPACE_DEFAULT)
        ComkitFontUtils.replaceFontFromAsset(llConfigContainer, ComkitFontConstants.FontPath.MONOSPACE_DEFAULT)
    }

    override fun initData() {
        this@MscdCharsSelectingFragment.chkBtnList = ArrayList()

        for (idxOfOutsideLlContainer in 0 until llCharSelectingContainer.childCount) {
            val charSelectingBtnContainer = llCharSelectingContainer.getChildAt(idxOfOutsideLlContainer) as LinearLayout
            for (idxOfInsideBtnContainer in 0 until charSelectingBtnContainer.childCount) {
                this@MscdCharsSelectingFragment.chkBtnList.add(charSelectingBtnContainer.getChildAt(idxOfInsideBtnContainer) as CheckBox)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindPresenter()
        this@MscdCharsSelectingFragment.mscdCharsSelectingIPresenter.onStandby()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        this@MscdCharsSelectingFragment.rootView = inflater?.inflate(R.layout.mscd_fragment_chars_selecting, container, false) ?: this@MscdCharsSelectingFragment.rootView
        return this@MscdCharsSelectingFragment.rootView
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

        MscdRandomStringGenerator.instance.cancelRandomStringGeneratorAsyncTask()
    }
}
