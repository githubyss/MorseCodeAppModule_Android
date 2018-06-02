package com.githubyss.mobile.morsecode.app.util.converter

import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils

/**
 * MscdMorseCodeConverter.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdMorseCodeConverter {
    companion object {
        var instance = Holder.INSTANCE
    }

    object Holder {
        val INSTANCE = MscdMorseCodeConverter()
    }


    /** To decide whether call create() to build the config by default variate value in itself when it was not built by user by judging the boolean value hasBuilt when getting instance of MscdMorseCodeConverterConfig. by Ace Yan */
    private val config =
            if (!MscdMorseCodeConverterConfig.instance.hasBuilt)
                MscdMorseCodeConverterConfig.Builder.create()
            else
                MscdMorseCodeConverterConfig.instance


    /**
     * MscdMorseCodeConverter.buildMessageStringDelayPatternArray(message)
     * <Description>
     * <Details>
     *
     * @param message
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    fun buildMessageStringDelayPatternArray(message: String): Array<Long> {
        val charGapDelay = this@MscdMorseCodeConverter.config.charGapDelay
        val wordGapDelay = this@MscdMorseCodeConverter.config.wordGapDelay
        val startDelay = this@MscdMorseCodeConverter.config.startDelay

        val char2DelayPatternArrayMap = this@MscdMorseCodeConverter.config.char2DelayPatternArrayMap

        if (message.isEmpty()) {
            return arrayOf(startDelay)
        }

        /** Declare a Boolean which is used to judge if the last char was white space. by Ace Yan */
        var lastCharWasWhiteSpace = true

        val messageLength = message.length

        /** Init the size of delayPatternArray of message to 1, and will store the startDelay in this position. by Ace Yan */
        var messageDelayPatternArraySize = 1

        /** Traverse message to calculate out the size of delayPatternArray of message. by Ace Yan */
        for (idx in 0 until messageLength) {
            val charAtIdx = message[idx]
            if (Character.isWhitespace(charAtIdx)) {
                if (!lastCharWasWhiteSpace) {
                    /**
                     * Size of delayPatternArray of message plus 1 to store wordGapDelay when the current char is white space and the last char was not white space.
                     * In this case, the current char is the 1st ' ' between words and need to be stored as a wordGapDelay.
                     * For example, "Morse  code", the increased size will store the wordGapDelay of ' ' after char 'e' when current char is the 1st ' '.
                     * by Ace Yan
                     */
                    messageDelayPatternArraySize++
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
                     * Size of delayPatternArray of message plus 1 to store charGapDelay when the current char is not white space and the last char was not white space too.
                     * In this case, the current char is the letter amongst a word and the gap before it need to be stored as a charGapDelay.
                     * For example, "Morse  code", the increased size will store the charGapDelay between 'o' and 'd' when current char is 'd'.
                     * by Ace Yan
                     */
                    messageDelayPatternArraySize++
                } else {
                    /**
                     * Do nothing when the current char is not white space and the last char was white space.
                     * In this case, the current char is the 1st letter of a word and the gap before it was stored as a wordGapDelay.
                     * For example, "Morse  code", do nothing when current char is 'c'.
                     * by Ace Yan
                     */
                }

                /**
                 * Size of delayPatternArray of message plus a size which is depended on the size of delayPatternArray of the current char.
                 * by Ace Yan
                 */
                messageDelayPatternArraySize += (char2DelayPatternArrayMap[charAtIdx]?.size ?: 0)

                lastCharWasWhiteSpace = false
            }
        }

        /** Init delayPatternArray of message. by Ace Yan */
        val messageDelayPatternArray = Array(messageDelayPatternArraySize, { it -> it.toLong() })
        messageDelayPatternArray[0] = startDelay
        lastCharWasWhiteSpace = true

        var positionInDelayPatternArray = 1

        /** Traverse message to calculate out each item in delayPatternArray of message. by Ace Yan */
        for (idx in 0 until messageLength) {
            val charAtIdx = message[idx]
            if (Character.isWhitespace(charAtIdx)) {
                if (!lastCharWasWhiteSpace) {
                    messageDelayPatternArray[positionInDelayPatternArray] = wordGapDelay
                    positionInDelayPatternArray++
                    lastCharWasWhiteSpace = true
                }
            } else {
                if (!lastCharWasWhiteSpace) {
                    messageDelayPatternArray[positionInDelayPatternArray] = charGapDelay
                    positionInDelayPatternArray++
                }

                lastCharWasWhiteSpace = false

                val charDelayPatternArray = char2DelayPatternArrayMap[charAtIdx] ?: emptyArray()
                System.arraycopy(charDelayPatternArray, 0, messageDelayPatternArray, positionInDelayPatternArray, charDelayPatternArray.size)
                positionInDelayPatternArray += (charDelayPatternArray.size)
            }
        }

        ComkitLogcatUtils.`object`(messageDelayPatternArray)

        return messageDelayPatternArray
    }

    /**
     * MscdMorseCodeConverter.buildMessageStringDelayPatternList(message)
     * <Description>
     * <Details>
     *
     * @param message
     * @return List<Long> Readable only.
     * @author Ace Yan
     * @github githubyss
     */
    fun buildMessageStringDelayPatternList(message: String): List<Long> {
        val charGapDelay = this@MscdMorseCodeConverter.config.charGapDelay
        val wordGapDelay = this@MscdMorseCodeConverter.config.wordGapDelay
        val startDelay = this@MscdMorseCodeConverter.config.startDelay

        val char2DelayPatternListMap = this@MscdMorseCodeConverter.config.char2DelayPatternListMap

        if (message.isEmpty()) {
            return arrayListOf(startDelay)
        }

        var lastCharWasWhiteSpace = true

        val messageLength = message.length

        val messageDelayPatternList = ArrayList<Long>()
        messageDelayPatternList.add(startDelay)

        for (idx in 0 until messageLength) {
            val charAtIdx = message[idx]
            if (Character.isWhitespace(charAtIdx)) {
                if (!lastCharWasWhiteSpace) {
                    messageDelayPatternList.add(wordGapDelay)
                    lastCharWasWhiteSpace = true
                }
            } else {
                if (!lastCharWasWhiteSpace) {
                    messageDelayPatternList.add(charGapDelay)
                }

                lastCharWasWhiteSpace = false

                val charDelayPatternList = char2DelayPatternListMap[charAtIdx] ?: emptyList()
                messageDelayPatternList.addAll(charDelayPatternList)
            }
        }

        ComkitLogcatUtils.`object`(messageDelayPatternList)

        return messageDelayPatternList
    }
}
