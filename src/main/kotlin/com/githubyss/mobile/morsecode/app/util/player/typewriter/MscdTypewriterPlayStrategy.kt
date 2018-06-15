package com.githubyss.mobile.morsecode.app.util.player.typewriter

/**
 * MscdTypewriterPlayStrategy.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
abstract class MscdTypewriterPlayStrategy {
    interface OnTypewriterListener {
        fun onSucceeded()
        fun onFailed(failingInfo: String)
        fun onCancelled()
    }


    abstract fun startPlayTypewriter(typewriterDataStr: String, onTypewriterListener: OnTypewriterListener)
    abstract fun stopPlayTypewriter()
}
