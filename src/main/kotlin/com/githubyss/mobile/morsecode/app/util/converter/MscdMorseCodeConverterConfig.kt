package com.githubyss.mobile.morsecode.app.util.converter

import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitTimeUtils
import com.githubyss.mobile.morsecode.app.constant.MscdEncodeConstants

/**
 * MscdMorseCodeConverterConfig.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder
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


    /** Basic duration to be used to init ditdah duration, gap duration and other durations to build char duration patterns, units is (ms). by Ace Yan */
    private var baseDuration = 50

    var ditDuration = baseDuration
        private set

    var dahDuration = baseDuration * 3
        private set

    var ditDahGapDuration = baseDuration
        private set

    var charGapDuration = baseDuration * 3
        private set

    var wordGapDuration = baseDuration * 7
        private set

    var startDuration = baseDuration * 10
        private set

    /** Char-DitdahString map. by Ace Yan */
    var char2DitdahStringMap = HashMap<Char, String>()
        private set

    /** Char-DurationPatternArray map. by Ace Yan */
    var char2DurationPatternArrayMap = HashMap<Char, Array<Int>>()
        private set

    /** Char-DurationPatternList map. by Ace Yan */
    var char2DurationPatternListMap = HashMap<Char, List<Int>>()
        private set

    var char2DurationMap = HashMap<Char, Int>()
        private set

    var hasBuilt = false
        private set


    object Builder {
        private var beginTime = 0L
        private var endTime = 0L

        private var baseDuration = 50

        fun setBaseDuration(duration: Int): Builder {
            var baseDuration = duration
            if (baseDuration <= 0) {
                baseDuration = 50
            }

            this@Builder.baseDuration = baseDuration
            return this@Builder
        }

        fun create(): MscdMorseCodeConverterConfig {
            this@Builder.applyConfig(instance)
            return instance
        }

        private fun applyConfig(config: MscdMorseCodeConverterConfig) {
            config.baseDuration = this@Builder.baseDuration
            config.ditDuration = this@Builder.baseDuration
            config.dahDuration = this@Builder.baseDuration * 3
            config.ditDahGapDuration = this@Builder.baseDuration
            config.charGapDuration = this@Builder.baseDuration * 3
            config.wordGapDuration = this@Builder.baseDuration * 7
            config.startDuration = this@Builder.baseDuration * 10

            if (config.char2DitdahStringMap.isEmpty()) {
                config.char2DitdahStringMap = this@Builder.buildChar2DitdahStringMap()
            }
            config.char2DurationPatternArrayMap = this@Builder.buildChar2DurationPatternArrayMap(config)
            config.char2DurationPatternListMap = this@Builder.buildChar2DurationPatternListMap(config)
            config.char2DurationMap = this@Builder.buildChar2DurationMap(config)

            config.hasBuilt = true
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
         * Builder.buildChar2DurationPatternArrayMap(config)
         * <Description>
         * <Details>
         *
         * @param config
         * @return
         * @author Ace Yan
         * @github githubyss
         */
        private fun buildChar2DurationPatternArrayMap(config: MscdMorseCodeConverterConfig): HashMap<Char, Array<Int>> {
            val ditDuration = config.ditDuration
            val dahDuration = config.dahDuration
            val ditDahGapDuration = config.ditDahGapDuration

            val char2DitdahStringMap = config.char2DitdahStringMap

            val char2DurationPatternArrayMap = LinkedHashMap<Char, Array<Int>>()

            beginTime = ComkitTimeUtils.currentTimeMillis()

            /** Traverse char-ditdahString map to build char-durationPatternArray map. by Ace Yan */
            for (entry in char2DitdahStringMap.entries) {
                val charKey = entry.key
                val charDitdahString = entry.value

                val charDitdahStringLength = charDitdahString.length

                /**
                 * Init durationPatternArray for each char in char-ditdahString map.
                 * This is because there is a ditdah gap duration between every dit duration or dah duration in a char ditdahString value.
                 * But the gap duration after the last dit or dah duration is the char duration,
                 * and the one after the last char of one word is the word duration,
                 * so there is no need to add a ditdah gap duration in these positions.
                 * This is the computational formula: [charDurationPatternArraySize = charDitdahStringLength * 2 - 1].
                 * by Ace Yan
                 */
                val charDurationPatternArraySize = charDitdahStringLength * 2 - 1
                val charDurationPatternArray = Array(charDurationPatternArraySize, { it -> it })

                /** Traverse ditdahString value to get durationPatternArray. by Ace Yan */
                for (idx in 0 until charDitdahStringLength) {
                    /** Insert dit or dah duration in position of [idx * 2] in durationPatternArray. by Ace Yan */
                    if (charDitdahString[idx] == MscdEncodeConstants.Codes.DIT) {
                        charDurationPatternArray[idx * 2] = ditDuration
                    } else if (charDitdahString[idx] == MscdEncodeConstants.Codes.DAH) {
                        charDurationPatternArray[idx * 2] = dahDuration
                    }

                    /** Insert ditdah gap duration in position of [idx * 2 + 1] in durationPatternArray unless idx is the last index of ditdahString. by Ace Yan */
                    if (idx != (charDitdahStringLength - 1)) {
                        charDurationPatternArray[idx * 2 + 1] = ditDahGapDuration
                    }
                }

                char2DurationPatternArrayMap.put(charKey, charDurationPatternArray)
            }

            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverterConfig.buildChar2DurationPatternArrayMap() >>> Elapsed time = ${endTime - beginTime} ms.")
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverterConfig.buildChar2DurationPatternArrayMap() >>> char2DurationPatternArrayMapSize = ${char2DurationPatternArrayMap.size}")

            ComkitLogcatUtils.`object`(char2DurationPatternArrayMap)

            return char2DurationPatternArrayMap
        }

        /**
         * Builder.buildChar2DurationPatternListMap(config)
         * <Description>
         * <Details>
         *
         * @param config
         * @return HashMap<Char, List<Long>> Value of the map is readable only.
         * @author Ace Yan
         * @github githubyss
         */
        private fun buildChar2DurationPatternListMap(config: MscdMorseCodeConverterConfig): HashMap<Char, List<Int>> {
            val ditDuration = config.ditDuration
            val dahDuration = config.dahDuration
            val ditDahGapDuration = config.ditDahGapDuration

            val char2DitdahStringMap = config.char2DitdahStringMap

            val char2DurationPatternListMap = LinkedHashMap<Char, List<Int>>()

            beginTime = ComkitTimeUtils.currentTimeMillis()

            for (entry in char2DitdahStringMap.entries) {
                val charKey = entry.key
                val charDitdahString = entry.value

                val charDitdahStringLength = charDitdahString.length

                val charDurationPatternList = ArrayList<Int>()

                for (idx in 0 until charDitdahStringLength) {
                    if (charDitdahString[idx] == MscdEncodeConstants.Codes.DIT) {
                        charDurationPatternList.add(ditDuration)
                    } else if (charDitdahString[idx] == MscdEncodeConstants.Codes.DAH) {
                        charDurationPatternList.add(dahDuration)
                    }

                    if (idx != (charDitdahStringLength - 1)) {
                        charDurationPatternList.add(ditDahGapDuration)
                    }
                }

                char2DurationPatternListMap.put(charKey, charDurationPatternList)
            }

            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverterConfig.buildChar2DurationPatternListMap() >>> Elapsed time = ${endTime - beginTime} ms.")
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverterConfig.buildChar2DurationPatternListMap() >>> char2DurationPatternListMapSize = ${char2DurationPatternListMap.size}")

            ComkitLogcatUtils.`object`(char2DurationPatternListMap)

            return char2DurationPatternListMap
        }

        /**
         * Builder.buildChar2DurationMap(config)
         * <Description>
         * <Details>
         *
         * @param config
         * @return
         * @author Ace Yan
         * @github githubyss
         */
        private fun buildChar2DurationMap(config: MscdMorseCodeConverterConfig): HashMap<Char, Int> {
            val char2DurationPatternListMap = config.char2DurationPatternListMap

            val char2DurationMap = LinkedHashMap<Char, Int>()

            beginTime = ComkitTimeUtils.currentTimeMillis()

            for (entry in char2DurationPatternListMap) {
                val charKey = entry.key
                val charDurationPatternList = entry.value

                val charDuration = charDurationPatternList.sum()

                char2DurationMap.put(charKey, charDuration)
            }

            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverterConfig.buildChar2DurationMap() >>> Elapsed time = ${endTime - beginTime} ms.")
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> MscdMorseCodeConverterConfig.buildChar2DurationMap() >>> char2DurationMap = ${char2DurationMap.size}")

            ComkitLogcatUtils.`object`(char2DurationPatternListMap)

            return char2DurationMap
        }
    }
}
