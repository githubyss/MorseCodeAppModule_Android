package com.githubyss.mobile.morsecode.app.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.githubyss.mobile.common.kit.base.ComkitBaseFragment
import com.githubyss.mobile.morsecode.app.R
import kotlinx.android.synthetic.main.mscd_fragment_homepage.*

/**
 * MscdHomepageFragment.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
@Route(path = "/morsecode/app/homepage/MscdHomepageFragment")
class MscdHomepageFragment : ComkitBaseFragment() {
    companion object {
        val TAG = "MscdHomepageFragment"
    }

    private lateinit var rootView: View

    private lateinit var mscdHomepageIPresenter: MscdHomepageContract.IPresenter
    private var mscdHomepageIView = object : MscdHomepageContract.IView {
        override fun setPresenter(iPresenter: MscdHomepageContract.IPresenter) {
            this@MscdHomepageFragment.mscdHomepageIPresenter = iPresenter
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        val id = v.id
        when (id) {
            R.id.btnMorseCodeTranslator -> {
//                ARouter.getInstance().build("/morsecode/app/translatorpage/MscdTranslatorActivity").navigation()
            }

            R.id.btnMorseCodeLearning -> {
                ARouter.getInstance().build("/morsecode/app/learningpage/MscdLearningActivity").navigation()
            }

            else -> {
            }
        }
    }


    override fun bindPresenter() {
        MscdHomepagePresenter(this@MscdHomepageFragment.mscdHomepageIView)
    }

    override fun initView() {
        btnMorseCodeTranslator.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnMorseCodeLearning.setOnClickListener(this@MscdHomepageFragment.onClickListener)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindPresenter()
        this@MscdHomepageFragment.mscdHomepageIPresenter.onStandby()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        this@MscdHomepageFragment.rootView = inflater?.inflate(R.layout.mscd_fragment_homepage, container, false) ?: this@MscdHomepageFragment.rootView
        return this@MscdHomepageFragment.rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        this@MscdHomepageFragment.mscdHomepageIPresenter.onActivityResult(requestCode, resultCode)
    }
}