package com.githubyss.mobile.morsecode.app.util.player.controller.action

import android.view.View

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
    fun startPlay(typewriterData: String, typewriterView: View)
    fun stopPlay()
    fun releaseResource()
}
