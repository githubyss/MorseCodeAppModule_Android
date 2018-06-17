package com.githubyss.mobile.morsecode.app.util.player.controller.action

import android.view.View
import com.githubyss.mobile.morsecode.app.util.player.typewriter.MscdTypewriterPlayStrategy

/**
 * MscdTypewriterOffAction.kt
 * <Description>
 * <Details>
 *
 * @designPatterns State
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterOffAction : MscdTypewriterAction {
    override fun startPlay(typewriterDataStr: String, typewriterDurationList: List<Int>, typewriterView: View, onTypewriterPlayListener: MscdTypewriterPlayStrategy.OnTypewriterPlayListener) {
    }

    override fun stopPlay() {
    }

    override fun releaseResource() {
    }
}
