package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.githubyss.mobile.common.kit.base.ComkitBaseFragment
import com.githubyss.mobile.common.kit.constant.ComkitFontConstants
import com.githubyss.mobile.common.kit.util.ComkitFontUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.constant.MscdKeyConstants
import kotlinx.android.synthetic.main.mscd_fragment_training.*


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

    private var trainingMsgStr = ""
    private var ditDuration = 0L

    private lateinit var mscdTrainingIPresenter: MscdTrainingContract.IPresenter
    private var mscdTrainingIView = object : MscdTrainingContract.IView {
        override fun setPresenter(iPresenter: MscdTrainingContract.IPresenter) {
            this@MscdTrainingFragment.mscdTrainingIPresenter = iPresenter
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        val id = v.id
        when (id) {
            R.id.btnSubmit -> {
            }

            else -> {
            }
        }
    }


    override fun bindPresenter() {
        MscdTrainingPresenter(this@MscdTrainingFragment.mscdTrainingIView)
    }

    override fun initView() {
        tvMorseCodeCopy.movementMethod = ScrollingMovementMethod.getInstance()
        tvMorseCodeCopy.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_MOVE -> {
                    v?.parent?.requestDisallowInterceptTouchEvent(true)
                }

                MotionEvent.ACTION_UP -> {
                    v?.parent?.requestDisallowInterceptTouchEvent(false)
                }

                else -> {
                    v?.parent?.requestDisallowInterceptTouchEvent(false)
                }
            }

            false
        }

        btnStartPlay.setOnClickListener(this@MscdTrainingFragment.onClickListener)
        btnStopPlay.setOnClickListener(this@MscdTrainingFragment.onClickListener)
        btnSubmit.setOnClickListener(this@MscdTrainingFragment.onClickListener)

        ComkitFontUtils.replaceFontFromAsset(llMorseCodeCopyDisplay, ComkitFontConstants.FontPath.MONOSPACE_DEFAULT)
        ComkitFontUtils.replaceFontFromAsset(llCharClickContainer, ComkitFontConstants.FontPath.MONOSPACE_DEFAULT)
    }

    override fun initData() {
        this@MscdTrainingFragment.trainingMsgStr = arguments.getString(MscdKeyConstants.MorseCodeConverterConfigKey.TRAINING_MESSAGE)
        this@MscdTrainingFragment.ditDuration = arguments.getLong(MscdKeyConstants.MorseCodeConverterConfigKey.BASE_DELAY)
    }

    override fun refreshView() {
        tvMorseCodeCopy.text = this@MscdTrainingFragment.trainingMsgStr
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
        initData()
        refreshView()
    }
}
