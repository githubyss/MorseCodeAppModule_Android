package com.githubyss.mobile.morsecode.app.util.player.controller.action

import android.view.View
import com.githubyss.mobile.morsecode.app.util.player.typewriter.MscdTypewriterPlayStrategy

/**
 * MscdTypewriterActionAction.kt
 * <Description>
 * <Details>
 *
 * @designPatterns State
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdTypewriterAction {
    fun startPlay(typewriterDataStr: String, typewriterDurationList: List<Int>, typewriterView: View, onTypewriterPlayListener: MscdTypewriterPlayStrategy.OnTypewriterPlayListener)
    fun stopPlay()
    fun releaseResource()
}
