package com.githubyss.mobile.morsecode.app.util.randommessage

import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import java.util.*

/**
 * MscdRegularRandomStringStrategy.kt
 * <Description> Build a random string with regular interval.
 * <Details>
 *
 * @designPatterns Strategy

 * @author Ace Yan
 * @github githubyss
 */
class MscdRegularRandomStringStrategy : MscdRandomStringStrategy {
    /**
     * MscdRegularRandomStringStrategy.buildRandomString(charList, size, count)
     * <Description>
     * <Details>
     *
     * @param charList Chars be used to build the random string.
     * @param size Size of valid chars in target random string.
     * @param count Constant char count of one word which built by the algorithm.
     * @return
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

        val randomStringBuilder = StringBuilder()

        for (idx in 0 until size) {
            val charIdx = randomSeedForCharIdx.nextInt(charList.size)

            if ((idx % count) == 0L) {
                randomStringBuilder.append(" ")
            }
            randomStringBuilder.append(charList[charIdx])

            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> buildRandomString() >>> charIdx = $charIdx")
        }

        ComkitLogcatUtils.`object`(randomStringBuilder)

        return randomStringBuilder.toString()
    }
}
