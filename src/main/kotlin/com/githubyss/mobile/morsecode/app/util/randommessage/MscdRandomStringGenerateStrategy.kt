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

    /**
     * MscdRandomStringGenerateStrategy.buildRandomString(charList, stringLength, wordSize)
     * <Description>
     * <Details>
     *
     * @param charList Chars be used to build the random string.
     * @param stringLength Size of valid chars in target random string.
     * @param wordSize Constant / Maximal stringLength of one word which built by the algorithm.
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun buildRandomString(charList: List<String>, stringLength: Long, wordSize: Int): String
}
