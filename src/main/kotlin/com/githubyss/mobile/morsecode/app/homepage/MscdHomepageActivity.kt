package com.githubyss.mobile.morsecode.app.homepage

import android.app.Fragment
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.githubyss.mobile.common.kit.logcat.ComkitLogcatUtils
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.base.MscdBaseActivity
import com.githubyss.mobile.morsecode.app.config.McappPageRouterConfig

/**
 * MscdHomepageActivity
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
@Route(path = McappPageRouterConfig.HOMEPAGE_ACTIVITY)
class MscdHomepageActivity : MscdBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addFragment(MscdHomepageFragment(), MscdHomepageFragment.TAG, false)
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle(R.string.mscdHomepageTitle)
        setToolbarOnLongClickListener(
                object : OnComuiBaseToolbarLongClickListener {
                    override fun onLongClick(v: View): Boolean {
                        val fragment = ARouter.getInstance().build("/morsecode/app/homepage/demo/MscdHomepageDemoFragment").navigation() as Fragment
                        replaceFragment(fragment, "MscdHomepageDemoFragment", true)

                        ComkitLogcatUtils.d(msg = "~~~Ace Yan~~~ >>> onLongClick() >>> ")
                        return true
                    }
                }
        )
    }
}
