package com.githubyss.mobile.morsecode.app.base

import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.githubyss.mobile.common.kit.base.ComkitBaseActivity
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.app.constant.MscdStatusConstants
import com.githubyss.mobile.morsecode.app.global.MscdPlayModeGlobalInfo
import com.githubyss.mobile.morsecode.app.util.player.controller.MscdPlayerController

/**
 * MscdBaseActivity.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
abstract class MscdBaseActivity : ComkitBaseActivity() {
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
        val itemAudio = menu?.findItem(R.id.itemAudio)
        val itemFlashlight = menu?.findItem(R.id.itemFlashlight)
        val itemVibrator = menu?.findItem(R.id.itemVibrator)
        val itemTypewriter = menu?.findItem(R.id.itemTypewriter)

        itemAudio?.setIcon(
                if (MscdPlayModeGlobalInfo.audioStatus == MscdStatusConstants.PlayModeStatus.AUDIO_ON) R.drawable.mscd_ic_action_audio_on_white
                else R.drawable.mscd_ic_action_audio_off_white)
        itemFlashlight?.setIcon(
                if (MscdPlayModeGlobalInfo.flashlightStatus == MscdStatusConstants.PlayModeStatus.FLASHLIGHT_ON) R.drawable.mscd_ic_action_flashlight_on_white
                else R.drawable.mscd_ic_action_flashlight_off_white)
        itemVibrator?.setIcon(
                if (MscdPlayModeGlobalInfo.vibratorStatus == MscdStatusConstants.PlayModeStatus.VIBRATION_ON) R.drawable.mscd_ic_action_vibrator_on_white
                else R.drawable.mscd_ic_action_vibrator_off_white)
        itemTypewriter?.setIcon(
                if (MscdPlayModeGlobalInfo.typewriterStatus == MscdStatusConstants.PlayModeStatus.TYPEWRITER_ON) R.drawable.mscd_ic_typewriter_on_white
                else R.drawable.mscd_ic_typewriter_off_white)
    }


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
            R.id.itemAudio -> {
                when (MscdPlayModeGlobalInfo.audioStatus) {
                    MscdStatusConstants.PlayModeStatus.AUDIO_ON -> {
                        item.setIcon(R.drawable.mscd_ic_action_audio_off_white)
                        MscdPlayerController.instance.audioOff()
                        MscdPlayModeGlobalInfo.audioStatus = MscdStatusConstants.PlayModeStatus.AUDIO_OFF
                    }

                    MscdStatusConstants.PlayModeStatus.AUDIO_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_action_audio_on_white)
                        MscdPlayerController.instance.audioOn()
                        MscdPlayModeGlobalInfo.audioStatus = MscdStatusConstants.PlayModeStatus.AUDIO_ON
                    }

                    else -> {
                        return false
                    }
                }
            }

            R.id.itemFlashlight -> {
                when (MscdPlayModeGlobalInfo.flashlightStatus) {
                    MscdStatusConstants.PlayModeStatus.FLASHLIGHT_ON -> {
                        item.setIcon(R.drawable.mscd_ic_action_flashlight_off_white)
                        MscdPlayerController.instance.flashlightOff()
                        MscdPlayModeGlobalInfo.flashlightStatus = MscdStatusConstants.PlayModeStatus.FLASHLIGHT_OFF
                    }

                    MscdStatusConstants.PlayModeStatus.FLASHLIGHT_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_action_flashlight_on_white)
                        MscdPlayerController.instance.flashlightOn()
                        MscdPlayModeGlobalInfo.flashlightStatus = MscdStatusConstants.PlayModeStatus.FLASHLIGHT_ON
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
                        MscdPlayerController.instance.vibratorOff()
                        MscdPlayModeGlobalInfo.vibratorStatus = MscdStatusConstants.PlayModeStatus.VIBRATION_OFF
                    }

                    MscdStatusConstants.PlayModeStatus.VIBRATION_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_action_vibrator_on_white)
                        MscdPlayerController.instance.vibratorOn()
                        MscdPlayModeGlobalInfo.vibratorStatus = MscdStatusConstants.PlayModeStatus.VIBRATION_ON
                    }

                    else -> {
                        return false
                    }
                }
            }

            R.id.itemTypewriter -> {
                when (MscdPlayModeGlobalInfo.typewriterStatus) {
                    MscdStatusConstants.PlayModeStatus.TYPEWRITER_ON -> {
                        item.setIcon(R.drawable.mscd_ic_typewriter_off_white)
                        MscdPlayerController.instance.typewriterOff()
                        MscdPlayModeGlobalInfo.typewriterStatus = MscdStatusConstants.PlayModeStatus.TYPEWRITER_OFF
                    }

                    MscdStatusConstants.PlayModeStatus.TYPEWRITER_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_typewriter_on_white)
                        MscdPlayerController.instance.typewriterOn()
                        MscdPlayModeGlobalInfo.typewriterStatus = MscdStatusConstants.PlayModeStatus.TYPEWRITER_ON
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
}
