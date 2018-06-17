package com.githubyss.mobile.morsecode.app.util.player.typewriter

/**
 * MscdTypewriterDataGenerator.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterDataGenerator private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdTypewriterDataGenerator()
    }


    private val typewriterDataGenerateStrategyConfig =
            if (!MscdTypewriterDataGenerateStrategyConfig.instance.hasBuilt)
                MscdTypewriterDataGenerateStrategyConfig.Builder
                        .create()
            else
                MscdTypewriterDataGenerateStrategyConfig.instance


    fun startGenerateTypewriteData(message: String, onTypewriterDataGenerateListener: MscdTypewriterDataGenerateStrategy.OnTypewriterDataGenerateListener) {
        typewriterDataGenerateStrategyConfig.typewriterDataGenerateStrategy.startGenerateTypewriterData(message, onTypewriterDataGenerateListener)
    }
}
