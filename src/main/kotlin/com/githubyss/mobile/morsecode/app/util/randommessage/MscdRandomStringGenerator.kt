package com.githubyss.mobile.morsecode.app.util.randommessage

import android.os.Bundle
import com.githubyss.mobile.morsecode.app.constant.MscdKeyConstants
import java.util.*

/**
 * MscdRandomStringGenerator.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder, Strategy
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdRandomStringGenerator private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdRandomStringGenerator()
    }


    /** Building the config by default variate value in itself when it was not built by user. by Ace Yan */
    private val randomStringGeneratorConfig =
            if (!MscdRandomStringGeneratorConfig.instance.hasBuilt)
                MscdRandomStringGeneratorConfig.Builder
                        .create()
            else
                MscdRandomStringGeneratorConfig.instance


    fun startGenerateRandomString(charList: List<String>, stringLength: Long, wordSize: Int, onRandomStringGenerateListener: MscdRandomStringGenerateStrategy.OnRandomStringGenerateListener) {
        val bundle = Bundle()
        bundle.putStringArrayList(MscdKeyConstants.CharSelectingKey.CHAR_LIST, charList as ArrayList<String>)
        bundle.putLong(MscdKeyConstants.CharSelectingKey.STRING_LENGTH, stringLength)
        bundle.putInt(MscdKeyConstants.CharSelectingKey.WORD_SIZE, wordSize)

        randomStringGeneratorConfig.randomStringGenerateStrategy.startGenerateRandomString(bundle, onRandomStringGenerateListener)
    }

    fun stopGenerateRandomString() {
        randomStringGeneratorConfig.randomStringGenerateStrategy.stopGenerateRandomString()
    }
}
