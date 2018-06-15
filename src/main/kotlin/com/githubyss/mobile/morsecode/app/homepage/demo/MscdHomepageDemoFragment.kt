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
import com.githubyss.mobile.common.kit.base.ComkitBaseFragment
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverter
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayer
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayerConfig
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
class MscdHomepageDemoFragment : ComkitBaseFragment() {
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
                MscdAudioPlayer.instance.logcatAudioTrackState("onClick", MscdAudioPlayerConfig.instance.audioTrack, "Before create().")

                MscdAudioPlayerConfig.Builder
                        .setAudioFrequencyHz(880)
                        .setAudioSampleRateHz(4000)
                        .setAudioChannelFormat(AudioFormat.CHANNEL_OUT_MONO)
                        .setAudioEncodingPcmFormat(AudioFormat.ENCODING_PCM_16BIT)
                        .setAudioStreamType(AudioManager.STREAM_MUSIC)
                        .setAudioTrackMode(AudioTrack.MODE_STREAM)
                        .create()

                MscdAudioPlayer.instance.logcatAudioTrackState("onClick", MscdAudioPlayerConfig.instance.audioTrack, "After create().")
            }

            R.id.btnStartPlayAudio -> {
//                MscdAudioPlayer.instance.startPlayAudio(
//                        "O",
//                        object : MscdAudioPlayer.OnAudioPlayListener {
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
//                MscdAudioPlayer.instance.stopPlayAudio()
                MscdAudioPlayer.instance.stopAllAudioTrack()
            }

            R.id.btnStopCurrentPlayAudio -> {
//                MscdAudioPlayer.instance.stopPlayAudio()
                MscdAudioPlayer.instance.stopCurrentAudioTrack()
            }

            R.id.btnPausePlayAudio -> {
                MscdAudioPlayer.instance.pauseAudioTrack()
            }

            R.id.btnResumePlayAudio -> {
                MscdAudioPlayer.instance.resumeAudioTrack()
            }

            R.id.btnReleaseAudioTrack -> {
                MscdAudioPlayer.instance.releaseAudioTrack()
            }

            R.id.btnLogcatAudioTrackState -> {
                MscdAudioPlayer.instance.logcatAudioTrackState("onClick", MscdAudioPlayerConfig.instance.audioTrack, "Manual logcat.")
            }

            R.id.btnInitMorseCodeConverterConfig -> {
                MscdMorseCodeConverterConfig.Builder
                        .setBaseDuration(100)
                        .create()
            }

            R.id.btnLogcatMessageDurationPatternArray -> {
//                MscdMorseCodeConverter.instance.buildMessageStringDurationPatternArray("MORSE  CODE")
                MscdMorseCodeConverter.instance.buildMessageStringDurationPatternList("MORSE  CODE")
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
