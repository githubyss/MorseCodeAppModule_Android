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
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerateStrategyConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioDataGenerator
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayerConfig
import com.githubyss.mobile.morsecode.app.util.player.controller.MscdPlayerController
import com.githubyss.mobile.morsecode.app.util.player.typewriter.*
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
class MscdTrainingFragment : ComkitBaseFragment(), MscdTrainingContract.IView {
    companion object {
        val TAG = "MscdTrainingFragment"
    }

    private lateinit var rootView: View
    private lateinit var inputBtnList: MutableList<Button>

    private var baseDuration = 0
    private var trainingMessageStr = String()

    private var audioDataArray = emptyArray<Float>()
    private var flashlightDataArray = emptyArray<Any>()
    private var vibratorDataArray = emptyArray<Any>()
    private var typewriterDurationList = emptyList<Int>()

    private var copiedMessageStr = String()
    private var copiedMessageStrBuilder = StringBuilder()

    private lateinit var mscdTrainingIPresenter: MscdTrainingContract.IPresenter

    private val onClickListener = View.OnClickListener { v ->
        val id = v.id
        when (id) {
            R.id.btnStartPlay -> {
                changeBtnStatus(btnStartPlay, false)
                changeBtnStatus(btnStopPlay, true)

                refreshViewOnTypewriterStatusChanged()
                tvMorseCodeCopy.text = ""

                initAudioPlayerConfig()

                mscdTrainingIPresenter.startPlay(audioDataArray, flashlightDataArray, vibratorDataArray, trainingMessageStr, typewriterDurationList, tvMorseCodeCopy)
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
        when (v.id) {
            R.id.btnClean -> {
                tvMorseCodeCopy.text = ""
                changeBtnStatus(btnSubmit, false)
            }

            else -> {
                tvMorseCodeCopy.append(getBtnInput(inputBtnList, v.id))
                ComkitTypewriteUtils.textViewAutoScrollBottom(tvMorseCodeCopy)
            }
        }
    }


    private fun initConfig() {
        initMorseCodeConverterConfig()
        initAudioPlayerConfig()
        initAudioDataGenerateStrategyConfig()
        initTypewriterPlayerConfig()
        initTypewriterPlayStrategyConfig()
        initTypewriterDurationGenerateStrategyConfig()
    }

    private fun initMorseCodeConverterConfig() {
        MscdMorseCodeConverterConfig.Builder
                .setBaseDuration(baseDuration)
                .create()
    }

    private fun initAudioPlayerConfig() {
        MscdAudioPlayerConfig.Builder
                .setAudioFrequencyHz(880)
                .setAudioSampleRateHz(4000)
                .setAudioChannelFormat(AudioFormat.CHANNEL_OUT_MONO)
                .setAudioEncodingPcmFormat(AudioFormat.ENCODING_PCM_16BIT)
                .setAudioStreamType(AudioManager.STREAM_MUSIC)
                .setAudioTrackMode(AudioTrack.MODE_STREAM)
                .create()
    }

    private fun initAudioDataGenerateStrategyConfig() {
        MscdAudioDataGenerateStrategyConfig.Builder
                .setStrategy(MscdAudioDataGenerateSineWaveStrategy())
                .create()
    }

    private fun initTypewriterPlayerConfig() {
        MscdTypewriterPlayerConfig.Builder
                .setStartIdx(0)
                .setCanAutoScrollBottom(true)
                .create()
    }

    private fun initTypewriterPlayStrategyConfig() {
        MscdTypewriterPlayStrategyConfig.Builder
                .setStrategy(MscdTypewriterPlayTextViewStrategy())
                .create()
    }

    private fun initTypewriterDurationGenerateStrategyConfig() {
        MscdTypewriterDurationGenerateStrategyConfig.Builder
                .setStrategy(MscdTypewriterDurationGeneratePresentStrategy())
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
        audioDataArray = emptyArray()
        flashlightDataArray = emptyArray()
        vibratorDataArray = emptyArray()
        typewriterDurationList = emptyList()
    }

    private fun getBtnInput(btnList: List<Button>, id: Int): String {
        btnList
                .filter { id == it.id }
                .forEach {
                    return when (it.text.toString()) {
                        ComkitResUtils.getString(resId = R.string.mscdCharSignSpace) -> " "
                        else -> it.text.toString()
                    }
                }

        return ""
    }


    // ---------- ---------- ---------- IView ---------- ---------- ----------

    override fun setPresenter(iPresenter: MscdTrainingContract.IPresenter) {
        mscdTrainingIPresenter = iPresenter
    }

    override fun showHint(hintStr: String?) {
        ComkitToastUtils.showMessage(msgStr = hintStr ?: "")
    }

    override fun onAudioDataBuilt(audioDataArray: Array<Float>) {
        changeBtnStatus(btnStartPlay, true)
        this@MscdTrainingFragment.audioDataArray = audioDataArray
    }

    override fun onTypewriterDurationBuilt(typewriterDurationList: List<Int>) {
        changeBtnStatus(btnStartPlay, true)
        this@MscdTrainingFragment.typewriterDurationList = typewriterDurationList
    }

    override fun onPlayFinished() {
        changeBtnStatus(btnStartPlay, true)
        changeBtnStatus(btnStopPlay, false)

        if (tvMorseCodeCopy != null
                && tvMorseCodeCopy.text.isNotEmpty()) {
            changeBtnStatus(btnSubmit, true)
        }
    }


    // ---------- ---------- ---------- Super ---------- ---------- ----------

    override fun bindPresenter() {
        MscdTrainingPresenter(this)
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
        inputBtnList
                .forEach { changeBtnStatus(it, false) }

        tvMorseCodeCopy.text = trainingMessageStr
    }


    // ---------- ---------- ---------- Lifecycle ---------- ---------- ----------

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
        MscdTypewriterDurationGenerator.instance.stopGenerateTypewriteDuration()

        MscdPlayerController.instance.stopPlay()
        MscdPlayerController.instance.releaseResource()

        clearPlayerData()
    }


    // ---------- ---------- ---------- Listener ---------- ---------- ----------

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
