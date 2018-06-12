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


    var randomStringGenerateStrategy = MscdRandomStringGenerateRegularStrategy() as MscdRandomStringGenerateStrategy
        private set

    var hasBuilt = false
        private set


    object Builder {
        private var randomStringGenerateStrategy = MscdRandomStringGenerateRegularStrategy() as MscdRandomStringGenerateStrategy

        fun setStrategy(strategy: MscdRandomStringGenerateStrategy): Builder {
            this@Builder.randomStringGenerateStrategy = strategy
            return this@Builder
        }

        fun create(): MscdRandomStringGeneratorConfig {
            this@Builder.applyConfig(instance)
            return instance
        }

        private fun applyConfig(config: MscdRandomStringGeneratorConfig) {
            config.randomStringGenerateStrategy = this@Builder.randomStringGenerateStrategy

            config.hasBuilt = true
        }
    }
}
