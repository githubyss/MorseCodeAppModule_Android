package com.githubyss.mobile.morsecode.app.base

import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.githubyss.mobile.common.kit.base.ComkitBaseActivity
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.constant.MscdStatusConstants
import com.githubyss.mobile.morsecode.app.global.MscdPlayModeGlobalInfo

/**
 * MscdBaseActivity.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
abstract class MscdBaseActivity : ComkitBaseActivity() {
    override fun onResume() {
        super.onResume()

        setToolbarNavigationIcon(R.drawable.mscd_ic_arrow_back_white)
        setToolbarNavigationOnClickListener(
                object : OnComkitBaseToolbarNavigationClickListener {
                    override fun onClick(v: View) {
                        onBackPressed()
                    }
                }
        )

        val menu = getMenu()
        refreshMenuItem(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mscd_menu_play_mode, menu)
        refreshMenuItem(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.itemFlashlight -> {
                when (MscdPlayModeGlobalInfo.flashlightStatus) {
                    MscdStatusConstants.PlayModeStatus.FLASHLIGHT_ON -> {
                        item.setIcon(R.drawable.mscd_ic_action_flashlight_off_white)
                        MscdPlayModeGlobalInfo.flashlightStatus = MscdStatusConstants.PlayModeStatus.FLASHLIGHT_OFF
                    }

                    MscdStatusConstants.PlayModeStatus.FLASHLIGHT_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_action_flashlight_on_white)
                        MscdPlayModeGlobalInfo.flashlightStatus = MscdStatusConstants.PlayModeStatus.FLASHLIGHT_ON
                    }

                    else -> {
                        return false
                    }
                }
            }

            R.id.itemAudio -> {
                when (MscdPlayModeGlobalInfo.audioStatus) {
                    MscdStatusConstants.PlayModeStatus.AUDIO_ON -> {
                        item.setIcon(R.drawable.mscd_ic_action_audio_off_white)
                        MscdPlayModeGlobalInfo.audioStatus = MscdStatusConstants.PlayModeStatus.AUDIO_OFF
                    }

                    MscdStatusConstants.PlayModeStatus.AUDIO_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_action_audio_on_white)
                        MscdPlayModeGlobalInfo.audioStatus = MscdStatusConstants.PlayModeStatus.AUDIO_ON
                    }

                    else -> {
                        return false
                    }
                }
            }

            R.id.itemVibrator -> {
                when (MscdPlayModeGlobalInfo.vibratorStatus) {
                    MscdStatusConstants.PlayModeStatus.VIBRATION_ON -> {
                        item.setIcon(R.drawable.mscd_ic_action_vibrator_off_white)
                        MscdPlayModeGlobalInfo.vibratorStatus = MscdStatusConstants.PlayModeStatus.VIBRATION_OFF
                    }

                    MscdStatusConstants.PlayModeStatus.VIBRATION_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_action_vibrator_on_white)
                        MscdPlayModeGlobalInfo.vibratorStatus = MscdStatusConstants.PlayModeStatus.VIBRATION_ON
                    }

                    else -> {
                        return false
                    }
                }
            }

            else -> {
                return false
            }
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * MscdBaseActivity.refreshMenuItem(menu)
     * <Description> Refresh menu item to show the latest status of action of play mode.
     * <Details> Must be called in both onResume() and onCreateOptionsMenu() to guarantee the correct status.
     *
     * @param menu
     * @return
     * @author Ace Yan
     * @github githubyss
     */
    private fun refreshMenuItem(menu: Menu?) {
        val itemFlashlight = menu?.findItem(R.id.itemFlashlight)
        val itemAudio = menu?.findItem(R.id.itemAudio)
        val itemVibrator = menu?.findItem(R.id.itemVibrator)

        itemFlashlight?.setIcon(
                if (MscdPlayModeGlobalInfo.flashlightStatus == MscdStatusConstants.PlayModeStatus.FLASHLIGHT_ON) R.drawable.mscd_ic_action_flashlight_on_white
                else R.drawable.mscd_ic_action_flashlight_off_white)
        itemAudio?.setIcon(
                if (MscdPlayModeGlobalInfo.audioStatus == MscdStatusConstants.PlayModeStatus.AUDIO_ON) R.drawable.mscd_ic_action_audio_on_white
                else R.drawable.mscd_ic_action_audio_off_white)
        itemVibrator?.setIcon(
                if (MscdPlayModeGlobalInfo.vibratorStatus == MscdStatusConstants.PlayModeStatus.VIBRATION_ON) R.drawable.mscd_ic_action_vibrator_on_white
                else R.drawable.mscd_ic_action_vibrator_off_white)
    }
}
