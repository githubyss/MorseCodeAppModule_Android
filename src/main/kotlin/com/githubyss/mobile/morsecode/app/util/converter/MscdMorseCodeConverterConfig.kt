package com.githubyss.mobile.morsecode.app.util.converter

import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.morsecode.app.constant.MscdEncodeConstants

/**
 * MscdMorseCodeConverterConfig.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdMorseCodeConverterConfig private constructor() {
    /** Inner static singleton mode. by Ace Yan */
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdMorseCodeConverterConfig()
    }


    /** Basic delay to be used to init ditdah delay, gap delay and other delays to build char delay patterns, units is (ms). by Ace Yan */
    private var baseDelay = 50L

    var ditDelay = baseDelay
        private set

    var dahDelay = baseDelay * 3
        private set

    var ditDahGapDelay = baseDelay
        private set

    var charGapDelay = baseDelay * 3
        private set

    var wordGapDelay = baseDelay * 7
        private set

    var startDelay = baseDelay * 10
        private set

    /** Char-ditdahString map. by Ace Yan */
    var char2DitdahStringMap = HashMap<Char, String>()
        private set

    /** Char-delayPatternArray map. by Ace Yan */
    var char2DelayPatternArrayMap = HashMap<Char, Array<Long>>()
        private set

    /** Char-delayPatternList map. by Ace Yan */
    var char2DelayPatternListMap = HashMap<Char, List<Long>>()
        private set

    var beBuilt = false
        private set


    object Builder {
        private var baseDelay = 50L

        fun setBaseDelay(delay: Long): Builder {
            var baseDelay = delay
            if (baseDelay <= 0L) {
                baseDelay = 50L
            }

            this@Builder.baseDelay = baseDelay
            return this@Builder
        }

        fun create(): MscdMorseCodeConverterConfig {
            this@Builder.applyConfig(instance)

            return instance
        }

        private fun applyConfig(config: MscdMorseCodeConverterConfig) {
            config.baseDelay = this@Builder.baseDelay
            config.ditDelay = this@Builder.baseDelay
            config.dahDelay = this@Builder.baseDelay * 3
            config.ditDahGapDelay = this@Builder.baseDelay
            config.charGapDelay = this@Builder.baseDelay * 3
            config.wordGapDelay = this@Builder.baseDelay * 7
            config.startDelay = this@Builder.baseDelay * 10

            if (config.char2DitdahStringMap.isEmpty()) {
                config.char2DitdahStringMap = this@Builder.buildChar2DitdahStringMap()
            }
            config.char2DelayPatternArrayMap = this@Builder.buildChar2DelayPatternArrayMap(config)
            config.char2DelayPatternListMap = this@Builder.buildChar2DelayPatternListMap(config)

            config.beBuilt = true
        }

        /**
         * Builder.buildChar2DitdahStringMap()
         * <Description> Build a LinkedHashMap to map char and dit-dah.
         * <Details>
         *
         * @param
         * @return
         * @author Ace Yan
         * @github githubyss
         */
        private fun buildChar2DitdahStringMap(): HashMap<Char, String> {
            val char2DitdahStringMap = LinkedHashMap<Char, String>()

            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_A, MscdEncodeConstants.Ditdahs.ALPHABET_A)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_B, MscdEncodeConstants.Ditdahs.ALPHABET_B)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_C, MscdEncodeConstants.Ditdahs.ALPHABET_C)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_D, MscdEncodeConstants.Ditdahs.ALPHABET_D)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_E, MscdEncodeConstants.Ditdahs.ALPHABET_E)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_F, MscdEncodeConstants.Ditdahs.ALPHABET_F)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_G, MscdEncodeConstants.Ditdahs.ALPHABET_G)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_H, MscdEncodeConstants.Ditdahs.ALPHABET_H)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_I, MscdEncodeConstants.Ditdahs.ALPHABET_I)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_J, MscdEncodeConstants.Ditdahs.ALPHABET_J)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_K, MscdEncodeConstants.Ditdahs.ALPHABET_K)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_L, MscdEncodeConstants.Ditdahs.ALPHABET_L)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_M, MscdEncodeConstants.Ditdahs.ALPHABET_M)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_N, MscdEncodeConstants.Ditdahs.ALPHABET_N)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_O, MscdEncodeConstants.Ditdahs.ALPHABET_O)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_P, MscdEncodeConstants.Ditdahs.ALPHABET_P)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_Q, MscdEncodeConstants.Ditdahs.ALPHABET_Q)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_R, MscdEncodeConstants.Ditdahs.ALPHABET_R)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_S, MscdEncodeConstants.Ditdahs.ALPHABET_S)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_T, MscdEncodeConstants.Ditdahs.ALPHABET_T)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_U, MscdEncodeConstants.Ditdahs.ALPHABET_U)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_V, MscdEncodeConstants.Ditdahs.ALPHABET_V)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_W, MscdEncodeConstants.Ditdahs.ALPHABET_W)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_X, MscdEncodeConstants.Ditdahs.ALPHABET_X)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_Y, MscdEncodeConstants.Ditdahs.ALPHABET_Y)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.ALPHABET_Z, MscdEncodeConstants.Ditdahs.ALPHABET_Z)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_0, MscdEncodeConstants.Ditdahs.NUMBER_0)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_1, MscdEncodeConstants.Ditdahs.NUMBER_1)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_2, MscdEncodeConstants.Ditdahs.NUMBER_2)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_3, MscdEncodeConstants.Ditdahs.NUMBER_3)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_4, MscdEncodeConstants.Ditdahs.NUMBER_4)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_5, MscdEncodeConstants.Ditdahs.NUMBER_5)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_6, MscdEncodeConstants.Ditdahs.NUMBER_6)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_7, MscdEncodeConstants.Ditdahs.NUMBER_7)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_8, MscdEncodeConstants.Ditdahs.NUMBER_8)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.NUMBER_9, MscdEncodeConstants.Ditdahs.NUMBER_9)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_DOT, MscdEncodeConstants.Ditdahs.SIGN_DOT)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_COLON, MscdEncodeConstants.Ditdahs.SIGN_COLON)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_COMMA, MscdEncodeConstants.Ditdahs.SIGN_COMMA)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_SEMICOLON, MscdEncodeConstants.Ditdahs.SIGN_SEMICOLON)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_QUESTION, MscdEncodeConstants.Ditdahs.SIGN_QUESTION)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_EQUAL, MscdEncodeConstants.Ditdahs.SIGN_EQUAL)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_SINGLE_QUOTATION, MscdEncodeConstants.Ditdahs.SIGN_SINGLE_QUOTATION)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_SLASH, MscdEncodeConstants.Ditdahs.SIGN_SLASH)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_EXCLAMATION, MscdEncodeConstants.Ditdahs.SIGN_EXCLAMATION)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_DASH, MscdEncodeConstants.Ditdahs.SIGN_DASH)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_UNDERSCORE, MscdEncodeConstants.Ditdahs.SIGN_UNDERSCORE)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_MARK_DOUBLE_QUOTATION, MscdEncodeConstants.Ditdahs.SIGN_MARK_DOUBLE_QUOTATION)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_LEFT_PARENTHESIS, MscdEncodeConstants.Ditdahs.SIGN_LEFT_PARENTHESIS)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_RIGHT_PARENTHESIS, MscdEncodeConstants.Ditdahs.SIGN_RIGHT_PARENTHESIS)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_DOLLAR, MscdEncodeConstants.Ditdahs.SIGN_DOLLAR)
//        char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_AMPERSAND, MscdEncodeConstants.Ditdahs.SIGN_AMPERSAND)
            char2DitdahStringMap.put(MscdEncodeConstants.Chars.SIGN_AT, MscdEncodeConstants.Ditdahs.SIGN_AT)

            ComkitLogcatUtils.`object`(char2DitdahStringMap)

            return char2DitdahStringMap
        }

        /**
         * Builder.buildChar2DelayPatternArrayMap(config)
         * <Description>
         * <Details>
         *
         * @param config
         * @return
         * @author Ace Yan
         * @github githubyss
         */
        private fun buildChar2DelayPatternArrayMap(config: MscdMorseCodeConverterConfig): HashMap<Char, Array<Long>> {
            val ditDelay = config.ditDelay
            val dahDelay = config.dahDelay
            val ditDahGapDelay = config.ditDahGapDelay

            val char2DitdahStringMap = config.char2DitdahStringMap

            val char2DelayPatternArrayMap = LinkedHashMap<Char, Array<Long>>()

            /** Traverse char-ditdahString map to build char-delayPatternArray map. by Ace Yan */
            for (entry in char2DitdahStringMap.entries) {
                val charKey = entry.key
                val charDitdahString = entry.value

                val charDitdahStringLength = charDitdahString.length

                /**
                 * Init delayPatternArray for each char in char-ditdahString map.
                 * This is because there is a ditdah gap delay between every dit delay or dah delay in a char ditdahString value.
                 * But the gap delay after the last dit or dah delay is the char delay,
                 * and the one after the last char of one word is the word delay,
                 * so there is no need to add a ditdah gap delay in these positions.
                 * This is the computational formula: [charDelayPatternArraySize = charDitdahStringLength * 2 - 1].
                 * by Ace Yan
                 */
                val charDelayPatternArraySize = charDitdahStringLength * 2 - 1
                val charDelayPatternArray = Array(charDelayPatternArraySize, { it -> it.toLong() })

                /** Traverse ditdahString value to get delayPatternArray. by Ace Yan */
                for (idx in 0 until charDitdahStringLength) {
                    /** Insert dit or dah delay in position of [idx * 2] in delayPatternArray. by Ace Yan */
                    if (charDitdahString[idx] == MscdEncodeConstants.Codes.DIT) {
                        charDelayPatternArray[idx * 2] = ditDelay
                    } else if (charDitdahString[idx] == MscdEncodeConstants.Codes.DAH) {
                        charDelayPatternArray[idx * 2] = dahDelay
                    }

                    /** Insert ditdah gap delay in position of [idx * 2 + 1] in delayPatternArray unless idx is the last index of ditdahString. by Ace Yan */
                    if (idx != (charDitdahStringLength - 1)) {
                        charDelayPatternArray[idx * 2 + 1] = ditDahGapDelay
                    }
                }

                char2DelayPatternArrayMap.put(charKey, charDelayPatternArray)
            }

            ComkitLogcatUtils.`object`(char2DelayPatternArrayMap)

            return char2DelayPatternArrayMap
        }

        /**
         * Builder.buildChar2DelayPatternListMap(config)
         * <Description>
         * <Details>
         *
         * @param config
         * @return HashMap<Char, List<Long>> Value of the map is readable only.
         * @author Ace Yan
         * @github githubyss
         */
        private fun buildChar2DelayPatternListMap(config: MscdMorseCodeConverterConfig): HashMap<Char, List<Long>> {
            val ditDelay = config.ditDelay
            val dahDelay = config.dahDelay
            val ditDahGapDelay = config.ditDahGapDelay

            val char2DitdahStringMap = config.char2DitdahStringMap

            val char2DelayPatternListMap = LinkedHashMap<Char, List<Long>>()

            for (entry in char2DitdahStringMap.entries) {
                val charKey = entry.key
                val charDitdahString = entry.value

                val charDitdahStringLength = charDitdahString.length

                val charDelayPatternList = ArrayList<Long>()

                for (idx in 0 until charDitdahStringLength) {
                    if (charDitdahString[idx] == MscdEncodeConstants.Codes.DIT) {
                        charDelayPatternList.add(ditDelay)
                    } else if (charDitdahString[idx] == MscdEncodeConstants.Codes.DAH) {
                        charDelayPatternList.add(dahDelay)
                    }

                    if (idx != (charDitdahStringLength - 1)) {
                        charDelayPatternList.add(ditDahGapDelay)
                    }
                }

                char2DelayPatternListMap.put(charKey, charDelayPatternList)
            }

            ComkitLogcatUtils.`object`(char2DelayPatternListMap)

            return char2DelayPatternListMap
        }
    }
}
