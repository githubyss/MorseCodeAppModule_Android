package com.githubyss.mobile.morsecode.app.homepage.demo

import android.content.Intent
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.githubyss.mobile.common.ui.basemvp.ComuiBaseFragment
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.kit.converter.MckitMorseCodeConverter
import com.githubyss.mobile.morsecode.kit.converter.MckitMorseCodeConverterConfig
import com.githubyss.mobile.morsecode.kit.player.audio.player.MckitAudioPlayer
import com.githubyss.mobile.morsecode.kit.player.audio.player.MckitAudioPlayerConfig
import kotlinx.android.synthetic.main.mscd_fragment_homepage_demo.*

/**
 * MscdHomepageDemoFragment.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
@Route(path = "/morsecode/app/homepage/demo/MscdHomepageDemoFragment")
class MscdHomepageDemoFragment : ComuiBaseFragment() {
    companion object {
        val TAG = "MscdHomepageDemoFragment"
    }

    private lateinit var rootView: View

    private lateinit var mscdHomepageDemoIPresenter: MscdHomepageDemoContract.IPresenter
    private var mscdHomepageDemoIView = object : MscdHomepageDemoContract.IView {
        override fun setPresenter(iPresenter: MscdHomepageDemoContract.IPresenter) {
            mscdHomepageDemoIPresenter = iPresenter
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

            R.id.btnBuildAudioConfig -> {
                MckitAudioPlayer.instance.logcatAudioTrackState("onClick", MckitAudioPlayerConfig.instance.audioTrack, "Before create().")

                MckitAudioPlayerConfig.Builder
                        .setAudioFrequencyHz(880)
                        .setAudioSampleRateHz(4000)
                        .setAudioChannelFormat(AudioFormat.CHANNEL_OUT_MONO)
                        .setAudioEncodingPcmFormat(AudioFormat.ENCODING_PCM_16BIT)
                        .setAudioStreamType(AudioManager.STREAM_MUSIC)
                        .setAudioTrackMode(AudioTrack.MODE_STREAM)
                        .create()

                MckitAudioPlayer.instance.logcatAudioTrackState("onClick", MckitAudioPlayerConfig.instance.audioTrack, "After create().")
            }

            R.id.btnStartPlayAudio -> {
//                MckitAudioPlayer.instance.startPlayAudio(
//                        "O",
//                        object : MckitAudioPlayer.OnAudioPlayListener {
//                            override fun onSucceeded() {
//                            }
//
//                            override fun onFailed(failingInfo: String) {
//                            }
//
//                            override fun onCancelled() {
//                            }
//                        }
//                )
            }

            R.id.btnStopAllPlayAudio -> {
//                MckitAudioPlayer.instance.stopPlayAudio()
                MckitAudioPlayer.instance.stopAllAudioTrack()
            }

            R.id.btnStopCurrentPlayAudio -> {
//                MckitAudioPlayer.instance.stopPlayAudio()
                MckitAudioPlayer.instance.stopCurrentAudioTrack()
            }

            R.id.btnPausePlayAudio -> {
                MckitAudioPlayer.instance.pauseAudioTrack()
            }

            R.id.btnResumePlayAudio -> {
                MckitAudioPlayer.instance.resumeAudioTrack()
            }

            R.id.btnReleaseAudioTrack -> {
                MckitAudioPlayer.instance.releaseAudioTrack()
            }

            R.id.btnLogcatAudioTrackState -> {
                MckitAudioPlayer.instance.logcatAudioTrackState("onClick", MckitAudioPlayerConfig.instance.audioTrack, "Manual logcat.")
            }

            R.id.btnInitMorseCodeConverterConfig -> {
                MckitMorseCodeConverterConfig.Builder
                        .setBaseDuration(100)
                        .create()
            }

            R.id.btnLogcatMessageDurationPatternArray -> {
//                MckitMorseCodeConverter.instance.buildMessageStringDurationPatternArray("MORSE  CODE")
                MckitMorseCodeConverter.instance.buildMessageStringDurationPatternList("MORSE  CODE")
            }

            else -> {
            }
        }
    }


    override fun bindPresenter() {
        MscdHomepageDemoPresenter(mscdHomepageDemoIView)
    }

    override fun initView() {
        btnMorseCodeTranslator.setOnClickListener(onClickListener)
        btnMorseCodeLearning.setOnClickListener(onClickListener)
        btnBuildAudioConfig.setOnClickListener(onClickListener)
        btnReleaseAudioTrack.setOnClickListener(onClickListener)
        btnStartPlayAudio.setOnClickListener(onClickListener)
        btnStopAllPlayAudio.setOnClickListener(onClickListener)
        btnStopCurrentPlayAudio.setOnClickListener(onClickListener)
        btnPausePlayAudio.setOnClickListener(onClickListener)
        btnResumePlayAudio.setOnClickListener(onClickListener)
        btnLogcatAudioTrackState.setOnClickListener(onClickListener)
        btnInitMorseCodeConverterConfig.setOnClickListener(onClickListener)
        btnLogcatMessageDurationPatternArray.setOnClickListener(onClickListener)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindPresenter()
        mscdHomepageDemoIPresenter.onStandby()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater?.inflate(R.layout.mscd_fragment_homepage_demo, container, false) ?: rootView
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mscdHomepageDemoIPresenter.onActivityResult(requestCode, resultCode)
    }
}
