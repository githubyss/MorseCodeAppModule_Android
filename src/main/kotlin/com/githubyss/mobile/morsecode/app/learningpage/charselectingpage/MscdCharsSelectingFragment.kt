package com.githubyss.mobile.morsecode.app.learningpage.charselectingpage

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.githubyss.mobile.common.kit.base.ComkitBaseFragment
import com.githubyss.mobile.morsecode.app.R
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

        override fun gotoTrainingPage() {
            val fragment = ARouter.getInstance().build("/morsecode/app/learningpage/traininggpage/MscdTrainingFragment").navigation() as Fragment
            replaceFragment(fragment, "MscdTrainingFragment", true)
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        val id = v.id
        when (id) {
            R.id.btnConfirm -> {
                mscdCharsSelectingIPresenter.buildRandomTrainingMessage(chkBtnList)
            }

            else -> {
            }
        }
    }


    override fun bindPresenter() {
        MscdCharsSelectingPresenter(this@MscdCharsSelectingFragment.mscdCharSelectingIView)
    }

    override fun initView() {
        btnConfirm.setOnClickListener(this@MscdCharsSelectingFragment.onClickListener)
    }

    override fun initData() {
        chkBtnList = ArrayList()

        for (idxOfOutsideLlContainer in 0 until llCharSelectingContainer.childCount) {
            val charSelectingBtnContainer = llCharSelectingContainer.getChildAt(idxOfOutsideLlContainer) as LinearLayout
            for (idxOfInsideBtnContainer in 0 until charSelectingBtnContainer.childCount) {
                chkBtnList.add(charSelectingBtnContainer.getChildAt(idxOfInsideBtnContainer) as CheckBox)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindPresenter()
        this@MscdCharsSelectingFragment.mscdCharsSelectingIPresenter.onStandby()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.mscd_fragment_chars_selecting, container, false) ?: this@MscdCharsSelectingFragment.rootView
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
}
