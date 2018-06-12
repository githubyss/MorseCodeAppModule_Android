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


    /** Building the audioDataGeneratorConfig by default variate value in itself when it was not built by user. by Ace Yan */
    private val audioDataGeneratorConfig =
            if (!MscdAudioDataGeneratorConfig.instance.hasBuilt)
                MscdAudioDataGeneratorConfig.Builder
                        .create()
            else
                MscdAudioDataGeneratorConfig.instance


    fun startGenerateAudioData(durationPatternList: List<Int>, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorConfig.audioDataGenerateStrategy.startGenerateAudioData(durationPatternList, onAudioDataGenerateListener)
    }

    fun startGenerateAudioData(durationPatternArray: Array<Int>, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorConfig.audioDataGenerateStrategy.startGenerateAudioData(durationPatternArray, onAudioDataGenerateListener)
    }

    fun startGenerateAudioData(audioDurationMillis: Int, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorConfig.audioDataGenerateStrategy.startGenerateAudioData(audioDurationMillis, onAudioDataGenerateListener)
    }

    fun startGenerateAudioData(message: String, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorConfig.audioDataGenerateStrategy.startGenerateAudioData(message, onAudioDataGenerateListener)
    }

    fun stopGenerateAudioData() {
        audioDataGeneratorConfig.audioDataGenerateStrategy.stopGenerateAudioData()
    }
}
