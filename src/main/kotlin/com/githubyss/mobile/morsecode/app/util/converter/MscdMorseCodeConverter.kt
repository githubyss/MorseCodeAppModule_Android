package com.githubyss.mobile.morsecode.app.util.converter

import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitTimeUtils

/**
 * MscdMorseCodeConverter.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdMorseCodeConverter private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    object Holder {
        val INSTANCE = MscdMorseCodeConverter()
    }


    /** To decide whether call create() to build the config by default variate value in itself when it was not built by user by judging the boolean value hasBuilt when getting instance of MscdMorseCodeConverterConfig. by Ace Yan */
    private val morseCodeConverterConfig =
            if (!MscdMorseCodeConverterConfig.instance.hasBuilt)
                MscdMorseCodeConverterConfig.Builder
                        .create()
            else
                MscdMorseCodeConverterConfig.instance

    private var beginTime = 0L
    private var endTime = 0L


    /**
     * MscdMorseCodeConverter.buildMessageStringDurationPatternArray(message)
     * <Description>
     * <Details>
     *
     * @param message
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun buildMessageStringDurationPatternArray(message: String): Array<Int> {
        val charGapDuration = morseCodeConverterConfig.charGapDuration
        val wordGapDuration = morseCodeConverterConfig.wordGapDuration
        val startDuration = morseCodeConverterConfig.startDuration

        val char2DurationPatternArrayMap = morseCodeConverterConfig.char2DurationPatternArrayMap

        beginTime = ComkitTimeUtils.currentTimeMillis()

        if (message.isEmpty()) {
            return arrayOf(startDuration)
        }

        /** Declare a Boolean which is used to judge if the last char was white space. by Ace Yan */
        var lastCharWasWhiteSpace = true

        val messageLength = message.length

        /** Init the size of durationPatternArray of message to 1, and will store the startDuration in this position. by Ace Yan */
        var messageDurationPatternArraySize = 1

        /** Traverse message to calculate out the size of durationPatternArray of message. by Ace Yan */
        (0 until messageLength)
                .asSequence()
                .map { message[it] }
                .forEach {
                    if (Character.isWhitespace(it)) {
                        if (!lastCharWasWhiteSpace) {
                            /**
                             * Size of durationPatternArray of message plus 1 to store wordGapDuration when the current char is white space and the last char was not white space.
                             * In this case, the current char is the 1st ' ' between words and need to be stored as a wordGapDuration.
                             * For example, "Morse  code", the increased size will store the wordGapDuration of ' ' after char 'e' when current char is the 1st ' '.
                             * by Ace Yan
                             */
                            messageDurationPatternArraySize++
                            lastCharWasWhiteSpace = true
                        } else {
                            /**
                             * Do nothing when the current char is white space and the last char was white space too.
                             * In this case, the current char is not the 1st ' ' between words and will be ignored.
                             * For example, "Morse  code", do nothing when current char is the 2ed ' '.
                             * by Ace Yan
                             */
                        }
                    } else {
                        if (!lastCharWasWhiteSpace) {
                            /**
                             * Size of durationPatternArray of message plus 1 to store charGapDuration when the current char is not white space and the last char was not white space too.
                             * In this case, the current char is the letter amongst a word and the gap before it need to be stored as a charGapDuration.
                             * For example, "Morse  code", the increased size will store the charGapDuration between 'o' and 'd' when current char is 'd'.
                             * by Ace Yan
                             */
                            messageDurationPatternArraySize++
                        } else {
                            /**
                             * Do nothing when the current char is not white space and the last char was white space.
                             * In this case, the current char is the 1st letter of a word and the gap before it was stored as a wordGapDuration.
                             * For example, "Morse  code", do nothing when current char is 'c'.
                             * by Ace Yan
                             */
                        }

                        /**
                         * Size of durationPatternArray of message plus a size which is depended on the size of durationPatternArray of the current char.
                         * by Ace Yan
                         */
                        messageDurationPatternArraySize += (char2DurationPatternArrayMap[it]?.size ?: 0)

                        lastCharWasWhiteSpace = false
                    }
                }

        /** Init durationPatternArray of message. by Ace Yan */
        val messageDurationPatternArray = Array(messageDurationPatternArraySize, { it -> it })
        messageDurationPatternArray[0] = startDuration
        lastCharWasWhiteSpace = true

        var positionInDurationPatternArray = 1

        /** Traverse message to calculate out each item in durationPatternArray of message. by Ace Yan */
        (0 until messageLength)
                .asSequence()
                .map { message[it] }
                .forEach {
                    if (Character.isWhitespace(it)) {
                        if (!lastCharWasWhiteSpace) {
                            messageDurationPatternArray[positionInDurationPatternArray] = wordGapDuration
                            positionInDurationPatternArray++
                            lastCharWasWhiteSpace = true
                        }
                    } else {
                        if (!lastCharWasWhiteSpace) {
                            messageDurationPatternArray[positionInDurationPatternArray] = charGapDuration
                            positionInDurationPatternArray++
                        }

                        lastCharWasWhiteSpace = false

                        val charDurationPatternArray = char2DurationPatternArrayMap[it] ?: emptyArray()
                        System.arraycopy(charDurationPatternArray, 0, messageDurationPatternArray, positionInDurationPatternArray, charDurationPatternArray.size)
                        positionInDurationPatternArray += (charDurationPatternArray.size)
                    }
                }

        endTime = ComkitTimeUtils.currentTimeMillis()
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverter.buildMessageStringDurationPatternArray() >>> Elapsed time = ${endTime - beginTime} ms.")
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverter.buildMessageStringDurationPatternArray() >>> messageDurationPatternArraySize = ${messageDurationPatternArray.size}")

        ComkitLogcatUtils.`object`(messageDurationPatternArray)

        return messageDurationPatternArray
    }

    /**
     * MscdMorseCodeConverter.buildMessageStringDurationPatternList(message)
     * <Description>
     * <Details>
     *
     * @param message
     * @return List<Long> Readable only.
     * @author Ace Yan
     * @github githubyss
     */
    fun buildMessageStringDurationPatternList(message: String): List<Int> {
        val charGapDuration = morseCodeConverterConfig.charGapDuration
        val wordGapDuration = morseCodeConverterConfig.wordGapDuration
        val startDuration = morseCodeConverterConfig.startDuration

        val char2DurationPatternListMap = morseCodeConverterConfig.char2DurationPatternListMap

        beginTime = ComkitTimeUtils.currentTimeMillis()

        if (message.isEmpty()) {
            return arrayListOf(startDuration)
        }

        var lastCharWasWhiteSpace = true

        val messageLength = message.length

        val messageDurationPatternList = ArrayList<Int>()
        messageDurationPatternList.add(startDuration)

        (0 until messageLength)
                .asSequence()
                .map { message[it] }
                .forEach {
                    if (Character.isWhitespace(it)) {
                        if (!lastCharWasWhiteSpace) {
                            messageDurationPatternList.add(wordGapDuration)
                            lastCharWasWhiteSpace = true
                        }
                    } else {
                        if (!lastCharWasWhiteSpace) {
                            messageDurationPatternList.add(charGapDuration)
                        }

                        lastCharWasWhiteSpace = false

                        val charDurationPatternList = char2DurationPatternListMap[it] ?: emptyList()
                        messageDurationPatternList.addAll(charDurationPatternList)
                    }
                }

        endTime = ComkitTimeUtils.currentTimeMillis()
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverter.buildMessageStringDurationPatternList() >>> Elapsed time = ${endTime - beginTime} ms.")
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverter.buildMessageStringDurationPatternList() >>> messageDurationPatternListSize = ${messageDurationPatternList.size}")

        ComkitLogcatUtils.`object`(messageDurationPatternList)

        return messageDurationPatternList
    }
}
