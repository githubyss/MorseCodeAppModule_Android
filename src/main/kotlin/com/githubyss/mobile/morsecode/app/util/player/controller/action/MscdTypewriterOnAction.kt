package com.githubyss.mobile.morsecode.app.util.player.controller.action

import android.view.View
import android.widget.TextView
import com.githubyss.mobile.common.kit.util.uioperate.ComkitTypewriteUtils

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
    override fun startPlay(typewriterData: String, typewriterView: View) {
        ComkitTypewriteUtils.textViewTypewriteByAppending(typewriterView as TextView, typewriterData, 50, 0, true)
    }

    override fun stopPlay() {
    }

    override fun releaseResource() {
    }
}
