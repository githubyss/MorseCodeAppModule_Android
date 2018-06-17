package com.githubyss.mobile.morsecode.app.util.player.typewriter

import android.os.Bundle
import android.view.View

/**
 * MscdTypewriterPlayStrategy.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Strategy
 *
 * @author Ace Yan
 * @github githubyss
 */
abstract class MscdTypewriterPlayStrategy {
    interface OnTypewriterPlayListener {
        fun onSucceeded()
        fun onFailed(failingInfo: String)
        fun onCancelled()
    }


    abstract fun startPlayTypewriter(bundle: Bundle, typewriterView: View, onTypewriterPlayListener: OnTypewriterPlayListener)
    abstract fun stopPlayTypewriter()


    protected val typewriterPlayerConfig =
            if (!MscdTypewriterPlayerConfig.instance.hasBuilt)
                MscdTypewriterPlayerConfig.Builder
                        .create()
            else
                MscdTypewriterPlayerConfig.instance
}
