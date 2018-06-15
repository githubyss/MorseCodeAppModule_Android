package com.githubyss.mobile.morsecode.app.util.player.typewriter

import android.view.View

/**
 * MscdTypewriterPlayer.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder, Strategy
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterPlayer private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdTypewriterPlayer()
    }


    private val typewriterPlayStrategyConfig =
            if (!MscdTypewriterPlayStrategyConfig.instance.hasBuilt)
                MscdTypewriterPlayStrategyConfig.Builder
                        .create()
            else
                MscdTypewriterPlayStrategyConfig.instance


    fun startPlayTypewriter(typewriterDataStr: String, typewriterView: View, onTypewriterPlayListener: MscdTypewriterPlayStrategy.OnTypewriterPlayListener) {
        typewriterPlayStrategyConfig.typewriterPlayStrategy.startPlayTypewriter(typewriterDataStr, typewriterView, onTypewriterPlayListener)
    }

    fun stopPlayTypewriter() {
        typewriterPlayStrategyConfig.typewriterPlayStrategy.stopPlayTypewriter()
    }
}
