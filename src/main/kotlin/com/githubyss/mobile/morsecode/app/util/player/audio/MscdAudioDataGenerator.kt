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


    fun startGenerateAudioData(delayPatternList: List<Int>, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorConfig.audioDataGenerateStrategy.startGenerateAudioData(delayPatternList, onAudioDataGenerateListener)
    }

    fun startGenerateAudioData(audioDurationInMs: Int, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorConfig.audioDataGenerateStrategy.startGenerateAudioData(audioDurationInMs, onAudioDataGenerateListener)
    }

    fun startGenerateAudioData(message: String, onAudioDataGenerateListener: MscdAudioDataGenerateStrategy.OnAudioDataGenerateListener) {
        audioDataGeneratorConfig.audioDataGenerateStrategy.startGenerateAudioData(message, onAudioDataGenerateListener)
    }

    fun stopGenerateAudioData() {
        audioDataGeneratorConfig.audioDataGenerateStrategy.stopGenerateAudioData()
    }
}
