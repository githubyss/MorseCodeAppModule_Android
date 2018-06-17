package com.githubyss.mobile.morsecode.app.util.player.typewriter

/**
 * MscdTypewriterPlayerConfig.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterPlayerConfig private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdTypewriterPlayerConfig()
    }


//    enum class TypewriteTimingEnum(val timing: String) {
//        AHEAD("ahead"), PRESENT("present"), POSTPONE("postpone")
//    }


    var startIdx = 0
        private set

//    var typewriteTiming = MscdTypewriterPlayerConfig.TypewriteTimingEnum.PRESENT
//        private set

    var canAutoScrollBottom = true
        private set

    var hasBuilt = false
        private set


    object Builder {
        private var startIdx = 0
        //        private var typewriteTiming = MscdTypewriterPlayerConfig.TypewriteTimingEnum.PRESENT
        private var canAutoScrollBottom = true

        fun setStartIdx(startIdx: Int): Builder {
            var idx = startIdx
            if (idx <= 0) {
                idx = 0
            }

            this@Builder.startIdx = idx
            return this@Builder
        }

//        fun setTypewriteTiming(typewriteTiming: TypewriteTimingEnum): Builder {
//            this@Builder.typewriteTiming = typewriteTiming
//            return this@Builder
//        }

        fun setCanAutoScrollBottom(canAutoScrollBottom: Boolean): Builder {
            this@Builder.canAutoScrollBottom = canAutoScrollBottom
            return this@Builder
        }

        fun create(): MscdTypewriterPlayerConfig {
            this@Builder.applyConfig(instance)
            return instance
        }

        private fun applyConfig(config: MscdTypewriterPlayerConfig) {
            config.startIdx = this@Builder.startIdx
//            config.typewriteTiming = this@Builder.typewriteTiming
            config.canAutoScrollBottom = this@Builder.canAutoScrollBottom

            config.hasBuilt = true
        }
    }
}
