package com.githubyss.mobile.morsecode.app.util.randommessage

/**
 * MscdRandomStringGenerator.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Strategy
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

    lateinit var mscdRandomStringStrategy: MscdRandomStringStrategy

    fun buildRandomString(selectedCharList: List<String>, targetedStringLength: Long, count: Int): String {
        return mscdRandomStringStrategy.buildRandomString(selectedCharList, targetedStringLength, count)
    }
}
