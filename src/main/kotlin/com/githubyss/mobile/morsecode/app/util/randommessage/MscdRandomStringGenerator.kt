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


    /** Building the config by default variate value in itself when it was not built by user. by Ace Yan */
    private val config =
            if (!MscdRandomStringGeneratorConfig.instance.hasBuilt)
                MscdRandomStringGeneratorConfig.Builder.create()
            else
                MscdRandomStringGeneratorConfig.instance

    private var randomStringGeneratorAsyncTask: RandomStringGeneratorAsyncTask? = null


    interface OnRandomStringGenerateListener {
        fun onSucceeded(randomString: String)
        fun onFailed(failingInfo: String)
        fun onCancelled()
    }

    private inner class RandomStringGeneratorAsyncTask(private val onRandomStringGenerateListener: OnRandomStringGenerateListener) : AsyncTask<Bundle, Int, String>() {
        override fun doInBackground(vararg params: Bundle): String {
            if (isCancelled) {
                ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> doInBackground() >>> isCancelled")
                return ""
            }

            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> doInBackground() >>> Current time is ${System.currentTimeMillis()}.")

            val bundle = params[0]
            val charList = bundle.getStringArrayList(MscdKeyConstants.CharSelectingKey.CHAR_LIST)
            val stringLength = bundle.getLong(MscdKeyConstants.CharSelectingKey.STRING_LENGTH)
            val wordSize = bundle.getInt(MscdKeyConstants.CharSelectingKey.WORD_SIZE)

            return this@MscdRandomStringGenerator.buildRandomString(charList, stringLength, wordSize)
        }

        override fun onPostExecute(result: String) {
            if (isCancelled) {
                return
            }

            if (result.contains(ComkitResUtils.getString(resId = R.string.mscdCharSelectingHintBuildingFailingInfo))) {
                onRandomStringGenerateListener.onFailed(result)
                return
            }

            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> onPostExecute() >>> Current time is ${System.currentTimeMillis()}.")
            onRandomStringGenerateListener.onSucceeded(result)
        }

        override fun onCancelled() {
            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> onCancelled() >>> Current time is ${System.currentTimeMillis()}.")
            onRandomStringGenerateListener.onCancelled()
        }
    }


    fun startRandomStringGeneratorAsyncTask(charList: List<String>, stringLength: Long, wordSize: Int, onRandomStringGenerateListener: OnRandomStringGenerateListener) {
        val bundle = Bundle()
        bundle.putStringArrayList(MscdKeyConstants.CharSelectingKey.CHAR_LIST, charList as ArrayList<String>)
        bundle.putLong(MscdKeyConstants.CharSelectingKey.STRING_LENGTH, stringLength)
        bundle.putInt(MscdKeyConstants.CharSelectingKey.WORD_SIZE, wordSize)

        ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> startRandomStringGeneratorAsyncTask() >>> Set strategy hasCancelled false.")
        MscdRandomStringGenerateStrategy.hasCancelled = false
        ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> startRandomStringGeneratorAsyncTask() >>> Current time is ${System.currentTimeMillis()}.")

        this@MscdRandomStringGenerator.randomStringGeneratorAsyncTask = RandomStringGeneratorAsyncTask(onRandomStringGenerateListener)
        this@MscdRandomStringGenerator.randomStringGeneratorAsyncTask?.execute(bundle)
    }

    fun cancelRandomStringGeneratorAsyncTask() {
        ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> cancelRandomStringGeneratorAsyncTask() >>> Current time is ${System.currentTimeMillis()}.")
        if (this@MscdRandomStringGenerator.randomStringGeneratorAsyncTask?.status == AsyncTask.Status.RUNNING) {
            this@MscdRandomStringGenerator.randomStringGeneratorAsyncTask?.cancel(true)
            MscdRandomStringGenerateStrategy.hasCancelled = true
            ComkitLogcatUtils.d("~~~Ace Yan~~~ >>> cancelRandomStringGeneratorAsyncTask() >>> Set strategy hasCancelled true.")
        }
    }

    private fun buildRandomString(charList: List<String>, stringLength: Long, wordSize: Int): String {
        if (!this@MscdRandomStringGenerator.config.hasBuilt) {
            MscdRandomStringGeneratorConfig.Builder.create()
        }

        return this@MscdRandomStringGenerator.config.randomStringGenerateStrategy.buildRandomString(charList, stringLength, wordSize)
    }
}
