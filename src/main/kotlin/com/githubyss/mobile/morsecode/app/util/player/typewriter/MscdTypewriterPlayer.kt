package com.githubyss.mobile.morsecode.app.util.player.typewriter

/**
 * MscdTypewriterPlayer.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterPlayer private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdTypewriterPlayer()
    }


    private val typewriterConfig =
            if (!MscdTypewriterPlayerConfig.instance.hasBuilt)
                MscdTypewriterPlayerConfig.Builder
                        .create()
            else
                MscdTypewriterPlayerConfig.instance


    fun startPlayTypewriter(typewriterDataStr: String, onTypewriterListener: MscdTypewriterPlayStrategy.OnTypewriterListener) {
        typewriterConfig.typewriterPlayStrategy.startPlayTypewriter(typewriterDataStr, onTypewriterListener)
    }

    fun stopPlayTypewriter() {
        typewriterConfig.typewriterPlayStrategy.stopPlayTypewriter()
    }
}
