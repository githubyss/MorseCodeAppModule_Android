package com.githubyss.mobile.morsecode.app.util.player.typewriter

/**
 * MscdTypewriterDataGenerateStrategyConfig.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterDataGenerateStrategyConfig private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdTypewriterDataGenerateStrategyConfig()
    }


    var typewriterDataGenerateStrategy = MscdTypewriterDataGeneratePresentStrategy() as MscdTypewriterDataGenerateStrategy
        private set

    var hasBuilt = false
        private set


    object Builder {
        private var typewriterDataGenerateStrategy = MscdTypewriterDataGeneratePresentStrategy() as MscdTypewriterDataGenerateStrategy

        fun setStrategy(strategy: MscdTypewriterDataGenerateStrategy): Builder {
            this@Builder.typewriterDataGenerateStrategy = strategy
            return this@Builder
        }

        fun create(): MscdTypewriterDataGenerateStrategyConfig {
            this@Builder.applyConfig(instance)
            return instance
        }

        private fun applyConfig(config: MscdTypewriterDataGenerateStrategyConfig) {
            config.typewriterDataGenerateStrategy = this@Builder.typewriterDataGenerateStrategy

            config.hasBuilt = true
        }
    }
}
