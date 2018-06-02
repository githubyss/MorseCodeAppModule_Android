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
class MscdRandomStringGeneratorConfig {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdRandomStringGeneratorConfig()
    }


    var mscdRandomStringStrategy = MscdRegularRandomStringStrategy() as MscdRandomStringStrategy
        private set

    var hasBuilt = false
        private set


    object Builder {
        private var mscdRandomStringStrategy = MscdRegularRandomStringStrategy() as MscdRandomStringStrategy

        fun setStrategy(strategy: MscdRandomStringStrategy): Builder {
            this@Builder.mscdRandomStringStrategy = strategy
            return this@Builder
        }

        fun create(): MscdRandomStringGeneratorConfig {
            this@Builder.applyConfig(instance)
            return instance
        }

        private fun applyConfig(config: MscdRandomStringGeneratorConfig) {
            config.mscdRandomStringStrategy = this@Builder.mscdRandomStringStrategy

            config.hasBuilt = true
        }
    }
}
