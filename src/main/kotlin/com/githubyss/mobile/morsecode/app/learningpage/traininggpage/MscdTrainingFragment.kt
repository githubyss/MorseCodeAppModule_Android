package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.githubyss.mobile.common.kit.base.ComkitBaseFragment
import com.githubyss.mobile.morsecode.app.R

/**
 * MscdTrainingFragment.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
@Route(path = "/morsecode/app/learningpage/traininggpage/MscdTrainingFragment")
class MscdTrainingFragment : ComkitBaseFragment() {
    companion object {
        val TAG = "MscdTrainingFragment"
    }

    private lateinit var rootView: View

    private lateinit var mscdTrainingIPresenter: MscdTrainingContract.IPresenter
    private var mscdTrainingIView = object : MscdTrainingContract.IView {
        override fun setPresenter(iPresenter: MscdTrainingContract.IPresenter) {
            this@MscdTrainingFragment.mscdTrainingIPresenter = iPresenter
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        val id = v.id
        when (id) {
            else -> {
            }
        }
    }


    override fun bindPresenter() {
        MscdTrainingPresenter(this@MscdTrainingFragment.mscdTrainingIView)
    }

    override fun initView() {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindPresenter()
        this@MscdTrainingFragment.mscdTrainingIPresenter.onStandby()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        this@MscdTrainingFragment.rootView = inflater?.inflate(R.layout.mscd_fragment_training, container, false) ?: this@MscdTrainingFragment.rootView
        return this@MscdTrainingFragment.rootView
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle(R.string.mscdTrainingTitle)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }
}
