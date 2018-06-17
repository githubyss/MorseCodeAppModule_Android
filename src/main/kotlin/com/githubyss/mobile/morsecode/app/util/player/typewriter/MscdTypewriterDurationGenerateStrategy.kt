package com.githubyss.mobile.morsecode.app.util.player.typewriter

import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig

/**
 * MscdTypewriterDurationGenerateStrategy.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
abstract class MscdTypewriterDurationGenerateStrategy {
    interface OnTypewriterDurationGenerateListener {
        fun onSucceeded(typewriterDurationList: List<Int>)
        fun onFailed(failingInfo: String)
        fun onCancelled()
    }


    abstract fun startGenerateTypewriterDuration(message: String, onTypewriterDurationGenerateListener: OnTypewriterDurationGenerateListener)
    abstract fun stopGenerateTypewriterDuration()


    protected val morseCodeConverterConfig =
            if (!MscdMorseCodeConverterConfig.instance.hasBuilt)
                MscdMorseCodeConverterConfig.Builder
                        .create()
            else
                MscdMorseCodeConverterConfig.instance
}
