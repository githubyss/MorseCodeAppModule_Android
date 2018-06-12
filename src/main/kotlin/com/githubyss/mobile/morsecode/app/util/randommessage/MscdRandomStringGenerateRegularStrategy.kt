package com.githubyss.mobile.morsecode.app.util.randommessage

import android.os.AsyncTask
import android.os.Bundle
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.common.kit.util.ComkitTimeUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.constant.MscdKeyConstants
import java.io.EOFException
import java.util.*

/**
 * MscdRandomStringGenerateRegularStrategy.kt
 * <Description> Build a random string with regular interval.
 * <Details>
 *
 * @designPatterns Strategy

 * @author Ace Yan
 * @github githubyss
 */
class MscdRandomStringGenerateRegularStrategy : MscdRandomStringGenerateStrategy() {
    private var randomStringGenerateAsyncTask: MscdRandomStringGenerateRegularStrategy.RandomStringGenerateAsyncTask? = null

    private var beginTime = 0L
    private var endTime = 0L

    private var exceptionInfo = ""


    private inner class RandomStringGenerateAsyncTask(private val onRandomStringGenerateListener: OnRandomStringGenerateListener) : AsyncTask<Bundle, Int, String>() {
        override fun doInBackground(vararg params: Bundle?): String {
            if (isCancelled) {
                ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> RandomStringGenerateAsyncTask.doInBackground() >>> isCancelled")
                return ""
            }

            beginTime = ComkitTimeUtils.currentTimeMillis()

            return try {
                val bundle = params[0]
                val charList = bundle?.getStringArrayList(MscdKeyConstants.CharSelectingKey.CHAR_LIST) ?: emptyList<String>()
                val stringLength = bundle?.getLong(MscdKeyConstants.CharSelectingKey.STRING_LENGTH) ?: 0
                val wordSize = bundle?.getInt(MscdKeyConstants.CharSelectingKey.WORD_SIZE) ?: 0
                buildRandomString(charList, stringLength, wordSize)
            } catch (exception: InterruptedException) {
                ComkitLogcatUtils.e(t = exception)
                ""
            }
        }

        override fun onPostExecute(result: String?) {
            if (isCancelled) {
                return
            }

            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> RandomStringGenerateAsyncTask.onPostExecute() >>> Elapsed time = ${endTime - beginTime} ms.")
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> RandomStringGenerateAsyncTask.onPostExecute() >>> Actual randomStringLength = ${result.toString().replace(" ", "").length}")
            ComkitLogcatUtils.`object`(result)

            if (result?.isEmpty() != false
                    || exceptionInfo.contains(ComkitResUtils.getString(resId = R.string.mscdFailingInfo))) {
                onRandomStringGenerateListener.onFailed(exceptionInfo)
                return
            }

            onRandomStringGenerateListener.onSucceeded(result)
        }

        override fun onCancelled() {
            endTime = ComkitTimeUtils.currentTimeMillis()
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> RandomStringGeneratorAsyncTask.onCancelled() >>> Elapsed time = ${endTime - beginTime} ms.")

            onRandomStringGenerateListener.onCancelled()
        }
    }


    override fun startGenerateRandomString(bundle: Bundle, onRandomStringGenerateListener: OnRandomStringGenerateListener) {
        randomStringGenerateAsyncTask = RandomStringGenerateAsyncTask(onRandomStringGenerateListener)
        randomStringGenerateAsyncTask?.execute(bundle)
    }

    override fun stopGenerateRandomString() {
        if (randomStringGenerateAsyncTask?.status == AsyncTask.Status.RUNNING) {
            randomStringGenerateAsyncTask?.cancel(true)
            randomStringGenerateAsyncTask = null
        }
    }


    /**
     * MscdRandomStringGenerateRegularStrategy.buildRandomString(charList, stringLength, wordSize)
     * <Description>
     * <Details>
     *
     * @param charList
     * @param stringLength
     * @param wordSize Constant stringLength of one word which built by the algorithm.
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun buildRandomString(charList: List<String>, stringLength: Long, wordSize: Int): String {
        if (charList.isEmpty()
                || stringLength <= 0L
                || wordSize <= 0) {
            return ""
        }

        /** A random seed is built to build random index to select char from the charList. by Ace Yan */
        val randomSeedForCharIdx = Random()

        val randomStringBuilder = StringBuilder()

        return try {
            for (idx in 0 until stringLength) {
                if (randomStringGenerateAsyncTask?.isCancelled != false) {
                    return ""
                }

                val charIdx = randomSeedForCharIdx.nextInt(charList.size)

                if ((idx % wordSize) == 0L && idx != 0L) {
                    randomStringBuilder.append(" ")
                }
                randomStringBuilder.append(charList[charIdx])

                ComkitLogcatUtils.v(msg = "~~~Ace Yan~~~ >>> buildRandomString() >>> charIdx = $charIdx")
            }

            randomStringBuilder.toString()
        } catch (exception: EOFException) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            ""
        } catch (exception: OutOfMemoryError) {
            ComkitLogcatUtils.e(t = exception)
            exceptionInfo = "${ComkitResUtils.getString(resId = R.string.mscdFailingInfo)} ${exception.javaClass.simpleName}!"
            ""
        }
    }
}
