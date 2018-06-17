package com.githubyss.mobile.morsecode.app.constant

/**
 * MscdKeyConstants.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
object MscdKeyConstants {
    object MorseCodeConverterConfigKey {
        val TRAINING_MESSAGE = "trainingMessage"
        val BASE_DURATION = "baseDuration"
    }

    object AudioConfigKey {
        val AUDIO_FREQUENCY = "audioFrequency"
        val AUDIO_SAMPLE_RATE = "audioSampleRate"
        val AUDIO_CHANNEL_FORMAT = "audioChannelFormat"
        val AUDIO_ENCODING_PCM_FORMAT = "audioEncodingPcmFormat"
    }

    object RandomStringGeneratorConfigKey {
        val RANDOM_STRING_GENERATE_STRATEGY = "randomStringGenerateStrategy"
    }

    object CharSelectingKey {
        val CHAR_LIST = "charList"
        val STRING_LENGTH = "stringLength"
        val WORD_SIZE = "wordSize"
    }

    object TypewriterKey {
        val MESSAGE_STR = "messageStr"
        val DURATION_LIST = "durationList"
    }
}
