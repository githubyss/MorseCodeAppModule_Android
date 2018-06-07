package com.githubyss.mobile.morsecode.app.util.randommessage

import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.morsecode.app.R
import java.io.EOFException
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
     * MscdRegularRandomStringStrategy.buildRandomString(charList, stringLength, wordSize)
     * <Description>
     * <Details>
     *
     * @param charList Chars be used to build the random string.
     * @param stringLength Size of valid chars in target random string.
     * @param wordSize Constant stringLength of one word which built by the algorithm.
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    override fun buildRandomString(charList: List<String>, stringLength: Long, wordSize: Int): String {
        if (charList.isEmpty()
                || stringLength <= 0L
                || wordSize <= 0) {
            return ""
        }

        /** A random seed is built to build random index to select char from the charList. by Ace Yan */
        val randomSeedForCharIdx = Random()

        val randomStringBuilder = StringBuilder()

        return try {
            for (idx in 0 until stringLength) {
                if (MscdRandomStringStrategy.hasCancelled) {
//                    ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> buildRandomString() >>> Cancelled actual randomString length = ${randomStringBuilder.toString().replace(" ", "").length}")
//                    ComkitLogcatUtils.`object`(randomStringBuilder)
                    return randomStringBuilder.toString()
                }

                val charIdx = randomSeedForCharIdx.nextInt(charList.size)

                if ((idx % wordSize) == 0L && idx != 0L) {
                    randomStringBuilder.append(" ")
                }
                randomStringBuilder.append(charList[charIdx])

//            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> buildRandomString() >>> charIdx = $charIdx")
            }

            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> buildRandomString() >>> Succeeded actual randomString length = ${randomStringBuilder.toString().replace(" ", "").length}")
            ComkitLogcatUtils.`object`(randomStringBuilder)

            randomStringBuilder.toString()
        } catch (exception: EOFException) {
            ComkitLogcatUtils.e(exception)
            "${ComkitResUtils.getString(resId = R.string.mscdCharSelectingHintBuildingFailingInfo)} ${exception.javaClass.simpleName}!"
        } catch (exception: OutOfMemoryError) {
            ComkitLogcatUtils.e(exception)
            "${ComkitResUtils.getString(resId = R.string.mscdCharSelectingHintBuildingFailingInfo)} ${exception.javaClass.simpleName}!"
        }
    }
}
