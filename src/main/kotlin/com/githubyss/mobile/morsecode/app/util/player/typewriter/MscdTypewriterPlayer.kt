package com.githubyss.mobile.morsecode.app.util.player.typewriter

import android.os.Bundle
import android.view.View
import com.githubyss.mobile.morsecode.app.constant.MscdKeyConstants
import java.util.*

/**
 * MscdTypewriterPlayer.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder, Strategy
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


    private val typewriterPlayStrategyConfig =
            if (!MscdTypewriterPlayStrategyConfig.instance.hasBuilt)
                MscdTypewriterPlayStrategyConfig.Builder
                        .create()
            else
                MscdTypewriterPlayStrategyConfig.instance


    fun startPlayTypewriter(typewriterDataStr: String, typewriterDataList: List<Int>, typewriterView: View, onTypewriterPlayListener: MscdTypewriterPlayStrategy.OnTypewriterPlayListener) {
        val bundle = Bundle()
        bundle.putString(MscdKeyConstants.TypewriterKey.MESSAGE_STR, typewriterDataStr)
        bundle.putIntegerArrayList(MscdKeyConstants.TypewriterKey.DURATION_LIST, typewriterDataList as ArrayList<Int>)

        typewriterPlayStrategyConfig.typewriterPlayStrategy.startPlayTypewriter(bundle, typewriterView, onTypewriterPlayListener)
    }

    fun stopPlayTypewriter() {
        typewriterPlayStrategyConfig.typewriterPlayStrategy.stopPlayTypewriter()
    }
}
