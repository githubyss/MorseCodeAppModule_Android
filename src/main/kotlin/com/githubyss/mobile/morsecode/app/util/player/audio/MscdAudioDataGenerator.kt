package com.githubyss.mobile.morsecode.app.util.player.audio

/**
 * MscdAudioDataGenerator.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdAudioDataGenerator private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdAudioDataGenerator()
    }


    /** Building the audioDataGeneratorStrategyConfig by default variate value in itself when it was not built by user. by Ace Yan */
    private val audioDataGeneratorStrategyConfig =
            if (!MscdAudioDataGenerateStrategyConfig.instance.hasBuilt)
                MscdAudioDataGenerateStrategyConfig.Builder
                        .create()
            else
                MscdAudioDataGenerateStrategyConfig.instance


    fun startGenerateAudioData(durationPatternList: List<Int>, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorStrategyConfig.audioDataGenerateStrategy.startGenerateAudioData(durationPatternList, onAudioDataGenerateListener)
    }

    fun startGenerateAudioData(durationPatternArray: Array<Int>, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorStrategyConfig.audioDataGenerateStrategy.startGenerateAudioData(durationPatternArray, onAudioDataGenerateListener)
    }

    fun startGenerateAudioData(audioDurationMillis: Int, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorStrategyConfig.audioDataGenerateStrategy.startGenerateAudioData(audioDurationMillis, onAudioDataGenerateListener)
    }

    fun startGenerateAudioData(message: String, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorStrategyConfig.audioDataGenerateStrategy.startGenerateAudioData(message, onAudioDataGenerateListener)
    }

    fun stopGenerateAudioData() {
        audioDataGeneratorStrategyConfig.audioDataGenerateStrategy.stopGenerateAudioData()
    }
}
