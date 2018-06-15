package com.githubyss.mobile.morsecode.app.learningpage.traininggpage

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.githubyss.mobile.common.kit.base.ComkitBaseFragment
import com.githubyss.mobile.common.kit.constant.ComkitFontConstants
import com.githubyss.mobile.common.kit.util.ComkitFontUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.common.kit.util.ComkitToastUtils
import com.githubyss.mobile.common.kit.util.uioperate.ComkitTypewriteUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.constant.MscdKeyConstants
import com.githubyss.mobile.morsecode.app.constant.MscdStatusConstants
import com.githubyss.mobile.morsecode.app.global.MscdPlayModeGlobalInfo
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerateSineWaveStrategy
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerator
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGeneratorConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayerConfig
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
    private lateinit var inputBtnList: MutableList<Button>

    private var baseDuration = 0
    private var trainingMessageStr = String()

    private var audioData = emptyArray<Float>()
    private var flashlightData = emptyArray<Any>()
    private var vibratorData = emptyArray<Any>()

    private var copiedMessageStr = String()
    private var copiedMessageStrBuilder = StringBuilder()

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

        override fun onPlayFinished() {
            changeBtnStatus(btnStartPlay, true)
            changeBtnStatus(btnStopPlay, false)

            clearPlayerData()
        }
    }

    private val onClickListener = View.OnClickListener { v ->
        val id = v.id
        when (id) {
            R.id.btnStartPlay -> {
                changeBtnStatus(btnStartPlay, false)
                changeBtnStatus(btnStopPlay, true)

                tvMorseCodeCopy.text = ""

                initAudioConfig()

                mscdTrainingIPresenter.startPlay(audioData, flashlightData, vibratorData, trainingMessageStr, tvMorseCodeCopy)
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

    private val onInputBtnClickListener = View.OnClickListener { v ->
        tvMorseCodeCopy.append(getBtnInput(inputBtnList, v.id))
        ComkitTypewriteUtils.textViewAutoScrollBottom(tvMorseCodeCopy)
    }


    private fun initConfig() {
        initMorseCodeConverterConfig()
        initAudioConfig()
        initAudioDataGeneratorConfig()
    }

    private fun initMorseCodeConverterConfig() {
        MscdMorseCodeConverterConfig.Builder
                .setBaseDuration(baseDuration)
                .create()
    }

    private fun initAudioConfig() {
        MscdAudioPlayerConfig.Builder
                .setAudioFrequencyHz(880)
                .setAudioSampleRateHz(4000)
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

    private fun refreshViewOnTypewriterStatusChanged() {
        when (MscdPlayModeGlobalInfo.typewriterStatus) {
            MscdStatusConstants.PlayModeStatus.TYPEWRITER_ON -> {
                tvMorseCodeCopy.text = ComkitResUtils.getString(resId = R.string.mscdTrainingTransmitted)

                inputBtnList
                        .forEach { changeBtnStatus(it, false) }
            }

            MscdStatusConstants.PlayModeStatus.TYPEWRITER_OFF -> {
                tvMorseCodeCopy.text = ComkitResUtils.getString(resId = R.string.mscdTrainingRecoded)

                inputBtnList
                        .forEach { changeBtnStatus(it, true) }
            }

            else -> {
            }
        }
    }

    private fun generatePlayerData() {
        mscdTrainingIPresenter.buildPlayData(trainingMessageStr)
    }

    private fun clearPlayerData() {
        audioData = emptyArray()
        flashlightData = emptyArray()
        vibratorData = emptyArray()
    }

    private fun getBtnInput(btnList: List<Button>, id: Int): String {
        btnList
                .filter { id == it.id }
                .forEach {
                    return when (it.text.toString()) {
                        ComkitResUtils.getString(resId = R.string.mscdCharSignSpace) -> " "
                        ComkitResUtils.getString(resId = R.string.mscdCharSkip) -> " "
                        else -> it.text.toString()
                    }
                }

        return ""
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
        ComkitFontUtils.replaceFontFromAsset(llCharInputContainer, ComkitFontConstants.FontPath.MONOSPACE_DEFAULT)
    }

    override fun initData() {
        trainingMessageStr = arguments.getString(MscdKeyConstants.MorseCodeConverterConfigKey.TRAINING_MESSAGE)
        baseDuration = arguments.getInt(MscdKeyConstants.MorseCodeConverterConfigKey.BASE_DURATION)

        inputBtnList = ArrayList()

        for (idxOfOutsideLlContainer in 0 until llCharInputContainer.childCount) {
            val charInputBtnContainer = llCharInputContainer.getChildAt(idxOfOutsideLlContainer) as LinearLayout
            for (idxOfInsideBtnContainer in 0 until charInputBtnContainer.childCount) {
                val inputBtn = charInputBtnContainer.getChildAt(idxOfInsideBtnContainer) as Button
                inputBtn.setOnClickListener(onInputBtnClickListener)
                inputBtnList.add(inputBtn)

            }
        }
    }

    override fun refreshView() {
        refreshViewOnTypewriterStatusChanged()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

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

        clearPlayerData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.itemTypewriter -> {
                refreshViewOnTypewriterStatusChanged()
            }

            else -> {
                return false
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
