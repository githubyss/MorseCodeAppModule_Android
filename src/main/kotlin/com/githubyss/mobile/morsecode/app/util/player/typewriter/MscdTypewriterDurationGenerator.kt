package com.githubyss.mobile.morsecode.app.util.player.typewriter

/**
 * MscdTypewriterDurationGenerator.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterDurationGenerator private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdTypewriterDurationGenerator()
    }


    private val typewriterDurationGenerateStrategyConfig =
            if (!MscdTypewriterDurationGenerateStrategyConfig.instance.hasBuilt)
                MscdTypewriterDurationGenerateStrategyConfig.Builder
                        .create()
            else
                MscdTypewriterDurationGenerateStrategyConfig.instance


    fun startGenerateTypewriteDuration(message: String, onTypewriterDurationGenerateListener: MscdTypewriterDurationGenerateStrategy.OnTypewriterDurationGenerateListener) {
        typewriterDurationGenerateStrategyConfig.typewriterDurationGenerateStrategy.startGenerateTypewriterDuration(message, onTypewriterDurationGenerateListener)
    }

    fun stopGenerateTypewriteDuration() {
        typewriterDurationGenerateStrategyConfig.typewriterDurationGenerateStrategy.stopGenerateTypewriterDuration()
    }
}
