package com.githubyss.mobile.morsecode.app.util.randommessage

import android.os.Bundle

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
abstract class MscdRandomStringGenerateStrategy {
    interface OnRandomStringGenerateListener {
        fun onSucceeded(randomString: String)
        fun onFailed(failingInfo: String)
        fun onCancelled()
    }


    abstract fun startGenerateRandomString(bundle: Bundle, onRandomStringGenerateListener: OnRandomStringGenerateListener)
    abstract fun stopGenerateRandomString()
}
