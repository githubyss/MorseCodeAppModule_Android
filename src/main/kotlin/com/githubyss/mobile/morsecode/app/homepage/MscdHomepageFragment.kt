package com.githubyss.mobile.morsecode.app.homepage

import android.content.Intent
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.githubyss.mobile.common.kit.base.ComkitBaseFragment
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverter
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioConfig
import com.githubyss.mobile.morsecode.app.util.player.audio.MscdAudioPlayer
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

            R.id.btnBuildAudioConfig -> {
                MscdAudioPlayer.instance.logcatAudioTrackState("onClick", MscdAudioConfig.instance.audioTrack, "Before create().")

                MscdAudioConfig.Builder
                        .setAudioFrequencyInHz(880)
                        .setAudioSampleRateInHz(44100)
                        .setAudioChannelFormat(AudioFormat.CHANNEL_OUT_MONO)
                        .setAudioEncodingPcmFormat(AudioFormat.ENCODING_PCM_16BIT)
                        .setAudioStreamType(AudioManager.STREAM_MUSIC)
                        .setAudioPlayMode(AudioTrack.MODE_STREAM)
                        .create()

                MscdAudioPlayer.instance.logcatAudioTrackState("onClick", MscdAudioConfig.instance.audioTrack, "After create().")
            }

            R.id.btnStartPlayAudio -> {
                MscdAudioPlayer.instance.startAudioPlayAsyncTask("MORSE  CODE")
            }

            R.id.btnStopAllPlayAudio -> {
//                MscdAudioPlayer.instance.cancelAudioPlayAsyncTask()
                MscdAudioPlayer.instance.stopAllPlayAudio()
            }

            R.id.btnStopCurrentPlayAudio -> {
//                MscdAudioPlayer.instance.cancelAudioPlayAsyncTask()
                MscdAudioPlayer.instance.stopCurrentPlayAudio()
            }

            R.id.btnPausePlayAudio -> {
                MscdAudioPlayer.instance.pausePlayAudio()
            }

            R.id.btnResumePlayAudio -> {
                MscdAudioPlayer.instance.resumePlayAudio()
            }

            R.id.btnReleaseAudioTrack -> {
                MscdAudioPlayer.instance.releaseAudioTrack()
            }

            R.id.btnLogcatAudioTrackState -> {
                MscdAudioPlayer.instance.logcatAudioTrackState("onClick", MscdAudioConfig.instance.audioTrack, "Manual logcat.")
            }
        }
    }


    override fun bindPresenter() {
        MscdHomepagePresenter(this@MscdHomepageFragment.mscdHomepageIView)
    }

    override fun initView() {
        btnMorseCodeTranslator.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnMorseCodeLearning.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnBuildAudioConfig.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnReleaseAudioTrack.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnStartPlayAudio.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnStopAllPlayAudio.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnStopCurrentPlayAudio.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnPausePlayAudio.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnResumePlayAudio.setOnClickListener(this@MscdHomepageFragment.onClickListener)
        btnLogcatAudioTrackState.setOnClickListener(this@MscdHomepageFragment.onClickListener)
    }

    private fun logcatMessageDelayPatternArray(message: String) {
        MscdMorseCodeConverter.instance.buildMessageStringDelayPatternArray(message)
        MscdMorseCodeConverter.instance.buildMessageStringDelayPatternList(message)
    }

    private fun initMorseCodeConverterConfig(delay: Long) {
        MscdMorseCodeConverterConfig.Builder
                .setBaseDelay(delay)
                .create()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindPresenter()
        this@MscdHomepageFragment.mscdHomepageIPresenter.onStandby()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.mscd_fragment_homepage, container, false) ?: this@MscdHomepageFragment.rootView
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
