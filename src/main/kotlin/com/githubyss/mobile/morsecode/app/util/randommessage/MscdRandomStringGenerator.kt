package com.githubyss.mobile.morsecode.app.util.randommessage

import android.os.AsyncTask
import android.os.Bundle
import com.githubyss.mobile.common.kit.util.ComkitLogcatUtils
import com.githubyss.mobile.common.kit.util.ComkitResUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.constant.MscdKeyConstants
import java.util.*

/**
 * MscdRandomStringGenerator.kt
 * <Description>
 * <Details>
 *
 * @designPatterns Singleton, Builder, Strategy
 *
 * @author Ace Yan
 * @github githubyss
 */
class MscdRandomStringGenerator private constructor() {
    companion object {
        var instance = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = MscdRandomStringGenerator()
    }


    interface OnRandomStringGenerateListener {
        fun onSucceeded(randomString: String)
        fun onFailed(failingInfo: String)
        fun onCancelled()
    }


    /** Building the config by default variate value in itself when it was not built by user. by Ace Yan */
    private val config =
            if (!MscdRandomStringGeneratorConfig.instance.hasBuilt)
                MscdRandomStringGeneratorConfig.Builder
                        .create()
            else
                MscdRandomStringGeneratorConfig.instance

    private var randomStringGeneratorAsyncTask: RandomStringGeneratorAsyncTask? = null


    private inner class RandomStringGeneratorAsyncTask(private val onRandomStringGenerateListener: OnRandomStringGenerateListener) : AsyncTask<Bundle, Int, String>() {
        override fun doInBackground(vararg params: Bundle): String {
            if (isCancelled) {
                ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> RandomStringGeneratorAsyncTask.doInBackground() >>> isCancelled")
                return ""
            }

            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> RandomStringGeneratorAsyncTask.doInBackground() >>> Current time is ${System.currentTimeMillis()}.")

            return try {
                val bundle = params[0]
                val charList = bundle.getStringArrayList(MscdKeyConstants.CharSelectingKey.CHAR_LIST)
                val stringLength = bundle.getLong(MscdKeyConstants.CharSelectingKey.STRING_LENGTH)
                val wordSize = bundle.getInt(MscdKeyConstants.CharSelectingKey.WORD_SIZE)
                buildRandomString(charList, stringLength, wordSize)
            } catch (exception: InterruptedException) {
                ComkitLogcatUtils.e(t = exception)
                ""
            }
        }

        override fun onPostExecute(result: String) {
            if (isCancelled) {
                return
            }

            if (result.contains(ComkitResUtils.getString(resId = R.string.mscdFailingInfo))) {
                onRandomStringGenerateListener.onFailed(result)
                return
            }

            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> RandomStringGeneratorAsyncTask.onPostExecute() >>> Current time is ${System.currentTimeMillis()}.")
            onRandomStringGenerateListener.onSucceeded(result)
        }

        override fun onCancelled() {
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> RandomStringGeneratorAsyncTask.onCancelled() >>> Current time is ${System.currentTimeMillis()}.")
            onRandomStringGenerateListener.onCancelled()
        }
    }


    fun startRandomStringGeneratorAsyncTask(charList: List<String>, stringLength: Long, wordSize: Int, onRandomStringGenerateListener: OnRandomStringGenerateListener) {
        val bundle = Bundle()
        bundle.putStringArrayList(MscdKeyConstants.CharSelectingKey.CHAR_LIST, charList as ArrayList<String>)
        bundle.putLong(MscdKeyConstants.CharSelectingKey.STRING_LENGTH, stringLength)
        bundle.putInt(MscdKeyConstants.CharSelectingKey.WORD_SIZE, wordSize)

        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> startRandomStringGeneratorAsyncTask() >>> Set strategy hasCancelled false.")
        MscdRandomStringGenerateStrategy.hasCancelled = false
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> startRandomStringGeneratorAsyncTask() >>> Current time is ${System.currentTimeMillis()}.")

        randomStringGeneratorAsyncTask = RandomStringGeneratorAsyncTask(onRandomStringGenerateListener)
        randomStringGeneratorAsyncTask?.execute(bundle)
    }

    fun cancelRandomStringGeneratorAsyncTask() {
        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> cancelRandomStringGeneratorAsyncTask() >>> Current time is ${System.currentTimeMillis()}.")
        if (randomStringGeneratorAsyncTask?.status == AsyncTask.Status.RUNNING) {
            randomStringGeneratorAsyncTask?.cancel(true)
            MscdRandomStringGenerateStrategy.hasCancelled = true
            ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> cancelRandomStringGeneratorAsyncTask() >>> Set strategy hasCancelled true.")
        }
    }

    private fun buildRandomString(charList: List<String>, stringLength: Long, wordSize: Int): String {
        return config.randomStringGenerateStrategy.buildRandomString(charList, stringLength, wordSize)
    }
}
