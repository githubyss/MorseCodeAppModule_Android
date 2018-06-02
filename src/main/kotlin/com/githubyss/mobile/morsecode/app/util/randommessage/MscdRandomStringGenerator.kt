package com.githubyss.mobile.morsecode.app.util.randommessage

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
class MscdRandomStringGenerator {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdRandomStringGenerator()
    }


    /** Building the config by default variate value in itself when it was not built by user. by Ace Yan */
    private val config =
            if (!MscdRandomStringGeneratorConfig.instance.hasBuilt)
                MscdRandomStringGeneratorConfig.Builder.create()
            else
                MscdRandomStringGeneratorConfig.instance


    fun buildRandomString(selectedCharList: List<String>, targetedStringLength: Long, count: Int): String {
        if (!this@MscdRandomStringGenerator.config.hasBuilt) {
            MscdRandomStringGeneratorConfig.Builder.create()
        }

        return this@MscdRandomStringGenerator.config.mscdRandomStringStrategy.buildRandomString(selectedCharList, targetedStringLength, count)
    }
}
