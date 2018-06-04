package com.githubyss.mobile.morsecode.app.util.randommessage

/**
 * MscdRandomStringStrategy.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Strategy
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdRandomStringStrategy {
    companion object {
        var hasCancelled = false
    }

    fun buildRandomString(charList: List<String>, stringLength: Long, wordSize: Int): String
}
