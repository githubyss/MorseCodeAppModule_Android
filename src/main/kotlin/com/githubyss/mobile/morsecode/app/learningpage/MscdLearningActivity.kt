package com.githubyss.mobile.morsecode.app.learningpage

import android.app.Fragment
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.base.MscdBaseActivity

/**
 * MscdLearningActivity.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
@Route(path = "/morsecode/app/learningpage/MscdLearningActivity")
class MscdLearningActivity : MscdBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = ARouter.getInstance().build("/morsecode/app/learningpage/charselectingpage/MscdCharsSelectingFragment").navigation() as Fragment
        addFragment(fragment, "MscdCharsSelectingFragment", false)
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle(R.string.mscdLearningTitle)
    }
}
