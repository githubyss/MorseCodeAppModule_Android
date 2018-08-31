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
import com.githubyss.mobile.common.kit.font.ComkitFontConfig
import com.githubyss.mobile.common.kit.font.ComkitFontUtils
import com.githubyss.mobile.common.kit.hint.ComkitToastUtils
import com.githubyss.mobile.common.kit.resource.ComkitResUtils
import com.githubyss.mobile.common.kit.viewoperator.ComkitTypewriteUtils
import com.githubyss.mobile.common.ui.basemvp.ComuiBaseFragment
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.constant.MscdKeyConstants
import com.githubyss.mobile.morsecode.kit.constant.MckitStatusConstants
import com.githubyss.mobile.morsecode.kit.controller.MckitPlayerController
import com.githubyss.mobile.morsecode.kit.converter.MckitMorseCodeConverterConfig
import com.githubyss.mobile.morsecode.kit.global.MckitPlayModeGlobalInfo
import com.githubyss.mobile.morsecode.kit.player.audio.generator.MckitAudioDataGenerateSineWaveStrategy
import com.githubyss.mobile.morsecode.kit.player.audio.generator.MckitAudioDataGenerateStrategyConfig
import com.githubyss.mobile.morsecode.kit.player.audio.generator.MckitAudioDataGenerator
import com.githubyss.mobile.morsecode.kit.player.audio.player.MckitAudioPlayerConfig
import com.githubyss.mobile.morsecode.kit.player.typewriter.generator.MckitTypewriterDurationGeneratePresentStrategy
import com.githubyss.mobile.morsecode.kit.player.typewriter.generator.MckitTypewriterDurationGenerateStrategyConfig
import com.githubyss.mobile.morsecode.kit.player.typewriter.generator.MckitTypewriterDurationGenerator
import com.githubyss.mobile.morsecode.kit.player.typewriter.player.MckitTypewriterPlayStrategyConfig
import com.githubyss.mobile.morsecode.kit.player.typewriter.player.MckitTypewriterPlayTextViewStrategy
import com.githubyss.mobile.morsecode.kit.player.typewriter.player.MckitTypewriterPlayerConfig
import kotlinx.android.synthetic.main.mscd_fragment_training.*


/**
 * MscdTrainingFragment
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
@Route(path = "/morsecode/app/learningpage/traininggpage/MscdTrainingFragment")
class MscdTrainingFragment : ComuiBaseFragment(), MscdTrainingContract.IView {
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
                MckitPlayerController.instance.stopPlay()
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
        MckitMorseCodeConverterConfig.Builder
                .setBaseDuration(baseDuration)
                .create()
    }

    private fun initAudioPlayerConfig() {
        MckitAudioPlayerConfig.Builder
                .setAudioFrequencyHz(880)
                .setAudioSampleRateHz(4000)
                .setAudioChannelFormat(AudioFormat.CHANNEL_OUT_MONO)
                .setAudioEncodingPcmFormat(AudioFormat.ENCODING_PCM_16BIT)
                .setAudioStreamType(AudioManager.STREAM_MUSIC)
                .setAudioTrackMode(AudioTrack.MODE_STREAM)
                .create()
    }

    private fun initAudioDataGenerateStrategyConfig() {
        MckitAudioDataGenerateStrategyConfig.Builder
                .setStrategy(MckitAudioDataGenerateSineWaveStrategy())
                .create()
    }

    private fun initTypewriterPlayerConfig() {
        MckitTypewriterPlayerConfig.Builder
                .setStartIdx(0)
                .setCanAutoScrollBottom(true)
                .create()
    }

    private fun initTypewriterPlayStrategyConfig() {
        MckitTypewriterPlayStrategyConfig.Builder
                .setStrategy(MckitTypewriterPlayTextViewStrategy())
                .create()
    }

    private fun initTypewriterDurationGenerateStrategyConfig() {
        MckitTypewriterDurationGenerateStrategyConfig.Builder
                .setStrategy(MckitTypewriterDurationGeneratePresentStrategy())
                .create()
    }

    private fun refreshViewOnTypewriterStatusChanged() {
        when (MckitPlayModeGlobalInfo.typewriterStatus) {
            MckitStatusConstants.PlayModeStatus.TYPEWRITER_ON -> {
                tvMorseCodeCopy.text = ComkitResUtils.getString(resId = R.string.mscdTrainingTransmitted)

                inputBtnList
                        .forEach { changeBtnStatus(it, false) }
            }

            MckitStatusConstants.PlayModeStatus.TYPEWRITER_OFF -> {
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

        ComkitFontUtils.replaceFontFromAsset(llMorseCodeCopyDisplay, ComkitFontConfig.FontPath.MONOSPACE_DEFAULT)
        ComkitFontUtils.replaceFontFromAsset(llCharInputContainer, ComkitFontConfig.FontPath.MONOSPACE_DEFAULT)
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

        MckitAudioDataGenerator.instance.stopGenerateAudioData()
        MckitTypewriterDurationGenerator.instance.stopGenerateTypewriteDuration()

        MckitPlayerController.instance.stopPlay()
        MckitPlayerController.instance.releaseResource()

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
