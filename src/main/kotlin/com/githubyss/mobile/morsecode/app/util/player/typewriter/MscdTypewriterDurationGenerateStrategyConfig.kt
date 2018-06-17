package com.githubyss.mobile.morsecode.app.util.player.typewriter

/**
 * MscdTypewriterDurationGenerateStrategyConfig.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterDurationGenerateStrategyConfig private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdTypewriterDurationGenerateStrategyConfig()
    }


    var typewriterDurationGenerateStrategy = MscdTypewriterDurationGeneratePresentStrategy() as MscdTypewriterDurationGenerateStrategy
        private set

    var hasBuilt = false
        private set


    object Builder {
        private var typewriterDurationGenerateStrategy = MscdTypewriterDurationGeneratePresentStrategy() as MscdTypewriterDurationGenerateStrategy

        fun setStrategy(strategy: MscdTypewriterDurationGenerateStrategy): Builder {
            this@Builder.typewriterDurationGenerateStrategy = strategy
            return this@Builder
        }

        fun create(): MscdTypewriterDurationGenerateStrategyConfig {
            this@Builder.applyConfig(instance)
            return instance
        }

        private fun applyConfig(config: MscdTypewriterDurationGenerateStrategyConfig) {
            config.typewriterDurationGenerateStrategy = this@Builder.typewriterDurationGenerateStrategy

            config.hasBuilt = true
        }
    }
}
