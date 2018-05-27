package com.githubyss.mobile.morsecode.app.homepage

import android.app.Fragment
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.githubyss.mobile.common.kit.base.ComkitBaseActivity
import com.githubyss.mobile.morsecode.app.R

/**
 * MscdHomepageActivity.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
@Route(path = "/morsecode/app/homepage/MscdHomepageActivity")
class MscdHomepageActivity : ComkitBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = ARouter.getInstance().build("/morsecode/app/homepage/MscdHomepageFragment").navigation() as Fragment
        addFragment(fragment, "/morsecode/app/homepage/MscdHomepageFragment", false)
    }

    override fun onResume() {
        super.onResume()

        setToolbarTitle(R.string.mscdHomepageTitle)
        setToolbarNavigationIcon(R.drawable.mscd_ic_arrow_back_white)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mscd_menu_play_mode, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionFlashlight -> {
            }

            R.id.actionSpeaker -> {
            }

            R.id.actionVibrator -> {
            }

            else -> {
            }
        }
        return true
    }
}
