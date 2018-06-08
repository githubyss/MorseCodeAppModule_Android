package com.githubyss.mobile.morsecode.app.util.randommessage

/**
 * MscdRandomStringGenerateStrategy.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Strategy
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdRandomStringGenerateStrategy {
    companion object {
        var hasCancelled = false
    }

    fun buildRandomString(charList: List<String>, stringLength: Long, wordSize: Int): String
}
