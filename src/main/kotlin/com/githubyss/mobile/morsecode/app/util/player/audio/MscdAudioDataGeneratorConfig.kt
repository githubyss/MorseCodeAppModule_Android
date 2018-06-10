package com.githubyss.mobile.morsecode.app.util.player.audio

/**
 * MscdAudioDataGeneratorConfig.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdAudioDataGeneratorConfig private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdAudioDataGeneratorConfig()
    }


    var audioDataGenerateStrategy = MscdAudioDataGenerateSineWaveStrategy() as MscdAudioDataGenerateStrategy
        private set

    var hasBuilt = false
        private set


    object Builder {
        private var audioDataGenerateStrategy = MscdAudioDataGenerateSineWaveStrategy() as MscdAudioDataGenerateStrategy

        fun setStrategy(strategy: MscdAudioDataGenerateStrategy): Builder {
            this@Builder.audioDataGenerateStrategy = strategy
            return this@Builder
        }

        fun create(): MscdAudioDataGeneratorConfig {
            this@Builder.applyConfig(instance)
            return instance
        }

        private fun applyConfig(config: MscdAudioDataGeneratorConfig) {
            config.audioDataGenerateStrategy = this@Builder.audioDataGenerateStrategy

            config.hasBuilt = true
        }
    }
}
