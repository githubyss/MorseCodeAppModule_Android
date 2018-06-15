package com.githubyss.mobile.morsecode.app.util.player.typewriter

/**
 * MscdTypewriterPlayStrategyConfig.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterPlayStrategyConfig private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdTypewriterPlayStrategyConfig()
    }


    var typewriterPlayStrategy = MscdTypewriterPlayTextViewStrategy() as MscdTypewriterPlayStrategy
        private set

    var hasBuilt = false
        private set


    object Builder {
        private var typewriterPlayStrategy = MscdTypewriterPlayTextViewStrategy() as MscdTypewriterPlayStrategy

        fun setStrategy(strategy: MscdTypewriterPlayStrategy): Builder {
            this@Builder.typewriterPlayStrategy = strategy
            return this@Builder
        }

        fun create(): MscdTypewriterPlayStrategyConfig {
            this@Builder.applyConfig(instance)
            return instance
        }

        private fun applyConfig(config: MscdTypewriterPlayStrategyConfig) {
            config.typewriterPlayStrategy = this@Builder.typewriterPlayStrategy

            config.hasBuilt = true
        }
    }
}
