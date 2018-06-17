package com.githubyss.mobile.morsecode.app.util.player.controller.action

import android.view.View
import com.githubyss.mobile.morsecode.app.util.player.typewriter.MscdTypewriterPlayStrategy
import com.githubyss.mobile.morsecode.app.util.player.typewriter.MscdTypewriterPlayer

/**
 * MscdTypewriterOnAction.kt
 * <Description>
 * <Details>
 *
 * @designPatterns State
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterOnAction : MscdTypewriterAction {
    override fun startPlay(typewriterDataStr: String, typewriterDurationList: List<Int>, typewriterView: View, onTypewriterPlayListener: MscdTypewriterPlayStrategy.OnTypewriterPlayListener) {
        MscdTypewriterPlayer.instance.startPlayTypewriter(typewriterDataStr, typewriterDurationList, typewriterView, onTypewriterPlayListener)
    }

    override fun stopPlay() {
        MscdTypewriterPlayer.instance.stopPlayTypewriter()
    }

    override fun releaseResource() {
    }
}
