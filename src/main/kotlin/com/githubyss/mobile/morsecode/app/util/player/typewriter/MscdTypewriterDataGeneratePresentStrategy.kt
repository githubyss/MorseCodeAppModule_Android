package com.githubyss.mobile.morsecode.app.util.player.typewriter

import android.os.AsyncTask
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.common.kit.util.ComkitTimeUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.util.converter.MscdMorseCodeConverterConfig
import java.io.EOFException

/**
 * MscdTypewriterDataGeneratePresentStrategy.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterDataGeneratePresentStrategy : MscdTypewriterDataGenerateStrategy() {
    private var typewriterDataGenerateAsyncTask: TypewriterDataGenerateAsyncTask? = null

    private var beginTime = 0L
    private var endTime = 0L

    private var exceptionInfo = ""


    private inner class TypewriterDataGenerateAsyncTask(private val onTypewriterDataGenerateListener: OnTypewriterDataGenerateListener) : AsyncTask<String, Int, List<Int>>() {
        override fun doInBackground(vararg params: String?): List<Int> {
            if (isCancelled) {
                ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterDataGenerateAsyncTask.doInBackground() >>> isCancelled")
                return emptyList()
            }

            beginTime = ComkitTimeUtils.currentTimeMillis()

            return try {
                buildTypewriterDataList(params[0] ?: String(), morseCodeConverterConfig)
            } catch (exception: InterruptedException) {
                ComkitLogcatUtils.e(t = exception)
                emptyList()
            }
        }

        override fun onPostExecute(result: List<Int>?) {
            if (isCancelled) {
                return
            }

            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterDataGenerateAsyncTask.onPostExecute() >>> Elapsed time = ${endTime - beginTime} ms.")
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterDataGenerateAsyncTask.onPostExecute() >>> audioDataSize = ${result?.size}")
            ComkitLogcatUtils.`object`(result)

            if (result?.isEmpty() != false
                    || exceptionInfo.contains(ComkitResUtils.getString(resId = R.string.mscdFailingInfo))) {
                onTypewriterDataGenerateListener.onFailed(exceptionInfo)
                return
            }

            onTypewriterDataGenerateListener.onSucceeded(result)
        }

        override fun onCancelled() {
            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterDataGenerateAsyncTask.onCancelled() >>> Elapsed time = ${endTime - beginTime} ms.")

            onTypewriterDataGenerateListener.onCancelled()
        }
    }


    override fun startGenerateTypewriterData(message: String, onTypewriterDataGenerateListener: OnTypewriterDataGenerateListener) {
        typewriterDataGenerateAsyncTask = TypewriterDataGenerateAsyncTask(onTypewriterDataGenerateListener)
        typewriterDataGenerateAsyncTask?.execute(message)
    }

    override fun stopGenerateTypewriterData() {
        if (typewriterDataGenerateAsyncTask?.status == AsyncTask.Status.RUNNING) {
            typewriterDataGenerateAsyncTask?.cancel(true)
            typewriterDataGenerateAsyncTask = null
        }
    }


    /**
     * MscdTypewriterDataGeneratePresentStrategy.buildTypewriterDataList(message)
     * <Description>
     * <Details>
     *
     * @param message
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun buildTypewriterDataList(message: String, morseCodeConverterConfig: MscdMorseCodeConverterConfig): List<Int> {
        val charGapDuration = morseCodeConverterConfig.charGapDuration
        val wordGapDuration = morseCodeConverterConfig.wordGapDuration
        val startDuration = morseCodeConverterConfig.startDuration

        val char2DurationMap = morseCodeConverterConfig.char2DurationMap

        if (message.isEmpty()) {
            return arrayListOf(startDuration)
        }

        var lastCharWasWhiteSpace = true

        val messageDurationList = ArrayList<Int>()
        messageDurationList.add(startDuration)

        return try {
            (0 until message.length)
                    .asSequence()
                    .map { message[it] }
                    .forEach {
                        if (typewriterDataGenerateAsyncTask?.isCancelled != false) {
                            return emptyList()
                        }

                        if (Character.isWhitespace(it)) {
                            if (!lastCharWasWhiteSpace) {
                                messageDurationList[messageDurationList.size - 1] = (messageDurationList.last() + wordGapDuration)
                                lastCharWasWhiteSpace = true
                            }
                        } else {
                            if (!lastCharWasWhiteSpace) {
                                messageDurationList[messageDurationList.size - 1] = (messageDurationList.last() + charGapDuration)
                            }

                            lastCharWasWhiteSpace = false

                            messageDurationList.add(char2DurationMap[it] ?: 0)
                        }
                    }

            messageDurationList
        } catch (exception: EOFException) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            emptyList()
        } catch (exception: OutOfMemoryError) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            emptyList()
        }
    }
}
