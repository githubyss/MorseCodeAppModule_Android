package com.githubyss.mobile.morsecode.app.util.player.typewriter

import android.os.AsyncTask
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.common.kit.util.ComkitTimeUtils
import com.githubyss.mobile.morsecode.app.R

class MscdTypewriterPlayTextViewStrategy : MscdTypewriterPlayStrategy() {
    private var typewriterPlayAsyncTask: TypewriterPlayAsyncTask? = null

    private var beginTime = 0L
    private var endTime = 0L

    private var exceptionInfo = String()


    private inner class TypewriterPlayAsyncTask(private val onTypewriterListener: OnTypewriterListener) : AsyncTask<String, Int, Boolean>() {
        override fun doInBackground(vararg params: String?): Boolean {
            if (isCancelled) {
                ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterPlayAsyncTask.doInBackground() >>> isCancelled.")
            }

            beginTime = ComkitTimeUtils.currentTimeMillis()

            return try {
                startTypewrite()
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
                onTypewriterListener.onSucceeded()
            } else {
                onTypewriterListener.onFailed(exceptionInfo)
            }
        }

        override fun onCancelled() {
            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> TypewriterPlayAsyncTask.onPostExecute() >>> Elapsed time = ${endTime - beginTime} ms.")

            onTypewriterListener.onCancelled()
        }
    }


    override fun startPlayTypewriter(typewriterDataStr: String, onTypewriterListener: OnTypewriterListener) {
        typewriterPlayAsyncTask = TypewriterPlayAsyncTask(onTypewriterListener)
        typewriterPlayAsyncTask?.execute(typewriterDataStr)
    }

    override fun stopPlayTypewriter() {
        if (typewriterPlayAsyncTask?.status == AsyncTask.Status.RUNNING) {
            typewriterPlayAsyncTask?.cancel(true)
            typewriterPlayAsyncTask = null
        }
    }


    private fun startTypewrite() {

    }
}
