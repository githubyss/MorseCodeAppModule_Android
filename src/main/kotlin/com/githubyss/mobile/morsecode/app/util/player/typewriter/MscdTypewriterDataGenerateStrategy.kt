package com.githubyss.mobile.morsecode.app.util.player.typewriter

import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig

/**
 * MscdTypewriterDataGenerateStrategy.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
abstract class MscdTypewriterDataGenerateStrategy {
    interface OnTypewriterDataGenerateListener {
        fun onSucceeded(typewriterDataList: List<Int>)
        fun onFailed(failingInfo: String)
        fun onCancelled()
    }


    abstract fun startGenerateTypewriterData(message: String, onTypewriterDataGenerateListener: OnTypewriterDataGenerateListener)
    abstract fun stopGenerateTypewriterData()


    protected val morseCodeConverterConfig =
            if (!MscdMorseCodeConverterConfig.instance.hasBuilt)
                MscdMorseCodeConverterConfig.Builder
                        .create()
            else
                MscdMorseCodeConverterConfig.instance
}
