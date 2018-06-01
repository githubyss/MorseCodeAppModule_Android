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
    fun buildRandomString(charList: List<String>, size: Long, count: Int): String
}
