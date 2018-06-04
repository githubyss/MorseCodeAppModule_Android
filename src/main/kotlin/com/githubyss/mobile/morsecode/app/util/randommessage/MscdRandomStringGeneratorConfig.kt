package com.githubyss.mobile.morsecode.app.util.randommessage

/**
 * MscdRandomStringGeneratorConfig.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdRandomStringGeneratorConfig private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdRandomStringGeneratorConfig()
    }


    var randomStringStrategy = MscdRegularRandomStringStrategy() as MscdRandomStringStrategy
        private set

    var hasBuilt = false
        private set


    object Builder {
        private var randomStringStrategy = MscdRegularRandomStringStrategy() as MscdRandomStringStrategy

        fun setStrategy(strategy: MscdRandomStringStrategy): Builder {
            this@Builder.randomStringStrategy = strategy
            return this@Builder
        }

        fun create(): MscdRandomStringGeneratorConfig {
            this@Builder.applyConfig(instance)
            return instance
        }

        private fun applyConfig(config: MscdRandomStringGeneratorConfig) {
            config.randomStringStrategy = this@Builder.randomStringStrategy

            config.hasBuilt = true
        }
    }
}
