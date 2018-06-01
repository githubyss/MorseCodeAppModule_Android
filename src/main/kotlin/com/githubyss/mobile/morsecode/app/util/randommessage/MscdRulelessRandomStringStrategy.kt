package com.githubyss.mobile.morsecode.app.util.randommessage

import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import java.util.*

/**
 * MscdRulelessRandomStringStrategy.kt
 * <Description> Build a random string with ruleless interval.
 * <Details>
 *
 * @designPatterns Strategy
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdRulelessRandomStringStrategy : MscdRandomStringStrategy {
    /**
     * MscdRulelessRandomStringStrategy.buildRandomString(charList, size, count)
     * <Description>
     * <Details>
     *
     * @param charList Chars be used to build the random string.
     * @param size Size of valid chars in target random string.
     * @param count Maximal char count of one word which built by the algorithm.
     * @return String
     * @author Ace Yan
     * @github githubyss
     */
    override fun buildRandomString(charList: List<String>, size: Long, count: Int): String {
        if (charList.isEmpty()
                || size <= 0L
                || count <= 0) {
            return ""
        }

        /** A random seed is built to build random index to select char from the charList. by Ace Yan */
        val randomSeedForCharIdx = Random()

        /** A random seed is built to build random count to build word. by Ace Yan */
        val randomSeedForCharCountInOneWord = Random()

        val randomStringBuilder = StringBuilder()

        var randomCount = count.toLong()
        var randomCountCalculated = randomCount
        for (idx in 0 until size) {
            val charIdx = randomSeedForCharIdx.nextInt(charList.size)

            if ((idx % randomCountCalculated) == 0L) {
                randomStringBuilder.append(" ")
                randomCount = randomSeedForCharCountInOneWord.nextInt(count).toLong() + 1
                randomCountCalculated = randomCount + idx
            }
            randomStringBuilder.append(charList[charIdx])

            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> buildRandomString() >>> charIdx = $charIdx, randomCount = $randomCount, randomCountCalculated = $randomCountCalculated")
        }

        ComkitLogcatUtils.`object`(randomStringBuilder)

        return randomStringBuilder.toString()
    }
}
