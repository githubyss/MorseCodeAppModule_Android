package com.githubyss.mobile.morsecode.app.util.player.typewriter

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.common.kit.util.ComkitTimeUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.constant.MscdKeyConstants

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


    private inner class TypewriterPlayAsyncTask(private val typewriterView: TextView, private val onTypewriterPlayListener: OnTypewriterPlayListener) : AsyncTask<Bundle, Int, Boolean>() {
        override fun doInBackground(vararg params: Bundle?): Boolean? {
            if (isCancelled) {
                ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterPlayAsyncTask.doInBackground() >>> isCancelled.")
            }

            beginTime = ComkitTimeUtils.currentTimeMillis()

            return try {
                val bundle = params[0]
                val message = bundle?.getString(MscdKeyConstants.TypewriterKey.MESSAGE_STR) ?: String()
                val data = bundle?.getIntegerArrayList(MscdKeyConstants.TypewriterKey.DURATION_LIST) ?: emptyList<Int>()
                startTypewrite(message, data, typewriterView, typewriterPlayerConfig)
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


    override fun startPlayTypewriter(bundle: Bundle, typewriterView: View, onTypewriterPlayListener: OnTypewriterPlayListener) {
        typewriterPlayAsyncTask = TypewriterPlayAsyncTask(typewriterView as TextView, onTypewriterPlayListener)
        typewriterPlayAsyncTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bundle)
    }

    override fun stopPlayTypewriter() {
        if (typewriterPlayAsyncTask?.status == AsyncTask.Status.RUNNING) {
            typewriterPlayAsyncTask?.cancel(true)
            typewriterPlayAsyncTask = null
        }
    }


    private fun startTypewrite(typewriterDataStr: String, typewriterDataDurationList: List<Int>, typewriterView: TextView, typewriterPlayerConfig: MscdTypewriterPlayerConfig) {
        if (typewriterDataStr.isEmpty()) {
            return
        }

        val startIdx = typewriterPlayerConfig.startIdx
        val canAutoScrollBottom = typewriterPlayerConfig.canAutoScrollBottom

        try {
            Thread.sleep(typewriterDataDurationList[0].toLong())
            for (idx in startIdx until typewriterDataStr.length) {
                if (typewriterPlayAsyncTask?.isCancelled != false) {
                    return
                }

                val char = typewriterDataStr[idx]

                textViewPostByAppending(typewriterView, char, canAutoScrollBottom)

                if (!Character.isWhitespace(char)) {
                    Thread.sleep(typewriterDataDurationList[idx + 1].toLong())
                }
            }
        } catch (exception: Exception) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
        }
    }

    private fun textViewPostByAppending(textView: TextView, char: Char, canAutoScrollBottom: Boolean) {
        textView.post {
            textView.append(char.toString())
            if (canAutoScrollBottom) {
                textViewAutoScrollBottom(textView)
            }
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
