package com.githubyss.mobile.morsecode.app.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.githubyss.mobile.common.kit.base.ComkitBaseFragment
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.converter.MscdMorseCodeConverter
import com.githubyss.mobile.morsecode.app.converter.MscdMorseCodeConverterConfig
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
        val TAG = "/morsecode/app/homepage/MscdHomepageFragment"
    }

    private lateinit var rootView: View

    private lateinit var mscdHomepageIPresenter: MscdHomepageContract.IPresenter
    private var mscdHomepageIView = object : MscdHomepageContract.IView {
        override fun setPresenter(iPresenter: MscdHomepageContract.IPresenter) {
            mscdHomepageIPresenter = iPresenter
        }

        override fun gotoTranslatorPage() {
        }

        override fun gotoLearningPage() {
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        val id = v.id
        when (id) {
            R.id.btnMorseCodeTranslator -> {
                logcatMessageDelayPatternArray("AB AB")
            }

            R.id.btnMorseCodeLearning -> {
                initMorseCodeConverterConfig(100L)
            }

            else -> {
            }
        }
    }

    override fun bindPresenter() {
        MscdHomepagePresenter(mscdHomepageIView)
    }

    override fun initView() {
        btnMorseCodeTranslator.setOnClickListener(onClickListener)
        btnMorseCodeLearning.setOnClickListener(onClickListener)
    }

    private fun initMorseCodeConverterConfig(delay: Long) {
        MscdMorseCodeConverterConfig.Builder.setBaseDelay(delay).create()
    }

    private fun logcatMessageDelayPatternArray(message: String) {
        val messageDelayPatternArray = MscdMorseCodeConverter.instance.buildMessageStringDelayPatternArray(message, MscdMorseCodeConverterConfig.instance)

        val messageDelayPatternList = MscdMorseCodeConverter.instance.buildMessageStringDelayPatternList(message, MscdMorseCodeConverterConfig.instance)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindPresenter()
        mscdHomepageIPresenter.onStandby()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.mscd_fragment_homepage, container, false) ?: rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mscdHomepageIPresenter.onActivityResult(requestCode, resultCode)
    }
}
