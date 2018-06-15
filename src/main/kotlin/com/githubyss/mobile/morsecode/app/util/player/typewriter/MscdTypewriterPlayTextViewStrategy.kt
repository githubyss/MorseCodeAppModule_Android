package com.githubyss.mobile.morsecode.app.util.player.typewriter

import android.os.AsyncTask
import android.view.View
import android.widget.TextView
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.common.kit.util.ComkitTimeUtils
import com.githubyss.mobile.morsecode.app.R

/**
 * MscdTypewriterPlayTextViewStrategy.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Strategy
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdTypewriterPlayTextViewStrategy : MscdTypewriterPlayStrategy() {
    private var typewriterPlayAsyncTask: TypewriterPlayAsyncTask? = null

    private var beginTime = 0L
    private var endTime = 0L

    private var exceptionInfo = String()


    private inner class TypewriterPlayAsyncTask(private val typewriterView: TextView, private val onTypewriterPlayListener: OnTypewriterPlayListener) : AsyncTask<String, Int, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            if (isCancelled) {
                ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterPlayAsyncTask.doInBackground() >>> isCancelled.")
            }

            beginTime = ComkitTimeUtils.currentTimeMillis()

            return try {
                val typewriterDataStr = params[0] ?: String()
                startTypewrite(typewriterDataStr, typewriterView, typewriterPlayerConfig)
                true
            } catch (exception: InterruptedException) {
                ComkitLogcatUtils.e(t = exception)
                exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
                false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            if (isCancelled) {
                return
            }

            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterPlayAsyncTask.onPostExecute() >>> Elapsed time = ${endTime - beginTime} ms.")

            if (result != false) {
                onTypewriterPlayListener.onSucceeded()
            } else {
                onTypewriterPlayListener.onFailed(exceptionInfo)
            }
        }

        override fun onCancelled() {
            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterPlayAsyncTask.onPostExecute() >>> Elapsed time = ${endTime - beginTime} ms.")

            onTypewriterPlayListener.onCancelled()
        }
    }


    override fun startPlayTypewriter(typewriterDataStr: String, typewriterView: View, onTypewriterPlayListener: OnTypewriterPlayListener) {
        typewriterPlayAsyncTask = TypewriterPlayAsyncTask(typewriterView as TextView, onTypewriterPlayListener)
        typewriterPlayAsyncTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, typewriterDataStr)
    }

    override fun stopPlayTypewriter() {
        if (typewriterPlayAsyncTask?.status == AsyncTask.Status.RUNNING) {
            typewriterPlayAsyncTask?.cancel(true)
            typewriterPlayAsyncTask = null
        }
    }


    private fun startTypewrite(typewriterDataStr: String, typewriterView: TextView, typewriterPlayerConfig: MscdTypewriterPlayerConfig) {
        val startIdx = typewriterPlayerConfig.startIdx
        val canAutoScrollBottom = typewriterPlayerConfig.canAutoScrollBottom

        val typewriterDataStrBuilder = StringBuilder(typewriterDataStr)

        try {
            for (idx in startIdx until typewriterDataStr.length) {
                if (typewriterPlayAsyncTask?.isCancelled != false) {
                    return
                }

                val char = typewriterDataStrBuilder[idx]

                typewriterView.post {
                    typewriterView.append(char.toString())
                    if (canAutoScrollBottom) {
                        textViewAutoScrollBottom(typewriterView)
                    }
                }

                Thread.sleep(50)
            }
        } catch (exception: Exception) {
            ComkitLogcatUtils.e(t = exception)
        }
    }

    private fun textViewAutoScrollBottom(textView: TextView) {
        val offsetHeight = textView.lineCount * textView.lineHeight
        val actualHeight = textView.height - (textView.paddingTop + textView.paddingBottom)
        if (offsetHeight > actualHeight) {
            textView.scrollTo(0, offsetHeight - actualHeight)
        }
    }
}
