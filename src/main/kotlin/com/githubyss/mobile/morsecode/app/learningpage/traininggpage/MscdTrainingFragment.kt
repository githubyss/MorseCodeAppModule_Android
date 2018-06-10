package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
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
import com.githubyss.mobile.common.kit.util.ComkitToastUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.constant.MscdKeyConstants
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerateSineWaveStrategy
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerator
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGeneratorConfig
import com.githubyss.mobile.morsecode.app.util.player.controller.MscdPlayerController
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
    private var baseDelay = 0

    private var audioData = emptyArray<Float>()
    private var flashlightData = emptyArray<Any>()
    private var vibratorData = emptyArray<Any>()

    private lateinit var mscdTrainingIPresenter: MscdTrainingContract.IPresenter
    private var mscdTrainingIView = object : MscdTrainingContract.IView {
        override fun setPresenter(iPresenter: MscdTrainingContract.IPresenter) {
            mscdTrainingIPresenter = iPresenter
        }

        override fun showHint(hintStr: String) {
            ComkitToastUtils.showMessage(msgStr = hintStr)
        }

        override fun onAudioDataBuilt(audioDataArray: Array<Float>) {
            changeBtnStatus(btnStartPlay, true)
           audioData = audioDataArray
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        val id = v.id
        when (id) {
            R.id.btnStartPlay -> {
                changeBtnStatus(btnStartPlay, false)
                changeBtnStatus(btnStopPlay, true)

                initAudioConfig()

                MscdPlayerController.instance.startPlay(audioData, flashlightData, vibratorData)
            }

            R.id.btnStopPlay -> {
                changeBtnStatus(btnStartPlay, true)
                changeBtnStatus(btnStopPlay, false)
                MscdPlayerController.instance.stopPlay()
            }

            R.id.btnSubmit -> {
            }

            else -> {
            }
        }
    }


    override fun bindPresenter() {
        MscdTrainingPresenter(mscdTrainingIView)
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

        btnStartPlay.setOnClickListener(onClickListener)
        btnStopPlay.setOnClickListener(onClickListener)
        btnSubmit.setOnClickListener(onClickListener)

        changeBtnStatus(btnStartPlay, false)
        changeBtnStatus(btnStopPlay, false)
        changeBtnStatus(btnSubmit, false)

        ComkitFontUtils.replaceFontFromAsset(llMorseCodeCopyDisplay, ComkitFontConstants.FontPath.MONOSPACE_DEFAULT)
        ComkitFontUtils.replaceFontFromAsset(llCharClickContainer, ComkitFontConstants.FontPath.MONOSPACE_DEFAULT)
    }

    override fun initData() {
        trainingMsgStr = arguments.getString(MscdKeyConstants.MorseCodeConverterConfigKey.TRAINING_MESSAGE)
        baseDelay = arguments.getInt(MscdKeyConstants.MorseCodeConverterConfigKey.BASE_DELAY)
    }

    override fun refreshView() {
        tvMorseCodeCopy.text = trainingMsgStr
    }


    private fun initConfig() {
        initMorseCodeConverterConfig()
        initAudioConfig()
        initAudioDataGeneratorConfig()
    }

    private fun initMorseCodeConverterConfig() {
        MscdMorseCodeConverterConfig.Builder
                .setBaseDelay(baseDelay)
                .create()
    }

    private fun initAudioConfig() {
        MscdAudioConfig.Builder
                .setAudioFrequencyInHz(880)
                .setAudioSampleRateInHz(4000)
                .setAudioChannelFormat(AudioFormat.CHANNEL_OUT_MONO)
                .setAudioEncodingPcmFormat(AudioFormat.ENCODING_PCM_16BIT)
                .setAudioStreamType(AudioManager.STREAM_MUSIC)
                .setAudioTrackMode(AudioTrack.MODE_STREAM)
                .create()
    }

    private fun initAudioDataGeneratorConfig() {
        MscdAudioDataGeneratorConfig.Builder
                .setStrategy(MscdAudioDataGenerateSineWaveStrategy())
                .create()
    }

    private fun generatePlayerData() {
        mscdTrainingIPresenter.buildPlayerData(trainingMsgStr)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindPresenter()
        mscdTrainingIPresenter.onStandby()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater?.inflate(R.layout.mscd_fragment_training, container, false) ?: rootView
        return rootView
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle(R.string.mscdTrainingTitle)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
        initConfig()
        generatePlayerData()
        refreshView()
    }

    override fun onDestroy() {
        super.onDestroy()

        MscdAudioDataGenerator.instance.stopGenerateAudioData()

        MscdPlayerController.instance.stopPlay()
        MscdPlayerController.instance.releaseResource()
    }
}
