package com.githubyss.mobile.morsecode.app.base

import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.githubyss.mobile.common.ui.basemvp.ComuiBaseActivity
import com.githubyss.mobile.morsecode.app.R
import com.githubyss.mobile.morsecode.kit.constant.MckitStatusConstants
import com.githubyss.mobile.morsecode.kit.controller.MckitPlayerController
import com.githubyss.mobile.morsecode.kit.global.MckitPlayModeGlobalInfo

/**
 * MscdBaseActivity.kt
 * <Description>
 * <Details>
 *
 * @author Ace Yan
 * @github githubyss
 */
abstract class MscdBaseActivity : ComuiBaseActivity() {
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
                if (MckitPlayModeGlobalInfo.audioStatus == MckitStatusConstants.PlayModeStatus.AUDIO_ON) R.drawable.mscd_ic_action_audio_on_white
                else R.drawable.mscd_ic_action_audio_off_white)
        itemFlashlight?.setIcon(
                if (MckitPlayModeGlobalInfo.flashlightStatus == MckitStatusConstants.PlayModeStatus.FLASHLIGHT_ON) R.drawable.mscd_ic_action_flashlight_on_white
                else R.drawable.mscd_ic_action_flashlight_off_white)
        itemVibrator?.setIcon(
                if (MckitPlayModeGlobalInfo.vibratorStatus == MckitStatusConstants.PlayModeStatus.VIBRATION_ON) R.drawable.mscd_ic_action_vibrator_on_white
                else R.drawable.mscd_ic_action_vibrator_off_white)
        itemTypewriter?.setIcon(
                if (MckitPlayModeGlobalInfo.typewriterStatus == MckitStatusConstants.PlayModeStatus.TYPEWRITER_ON) R.drawable.mscd_ic_typewriter_on_white
                else R.drawable.mscd_ic_typewriter_off_white)
    }


    override fun onResume() {
        super.onResume()

        setToolbarNavigationIcon(R.drawable.mscd_ic_arrow_back_white)
        setToolbarNavigationOnClickListener(
                object : OnComuiBaseToolbarNavigationClickListener {
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
                when (MckitPlayModeGlobalInfo.audioStatus) {
                    MckitStatusConstants.PlayModeStatus.AUDIO_ON -> {
                        item.setIcon(R.drawable.mscd_ic_action_audio_off_white)
                        MckitPlayerController.instance.audioOff()
                        MckitPlayModeGlobalInfo.audioStatus = MckitStatusConstants.PlayModeStatus.AUDIO_OFF
                    }

                    MckitStatusConstants.PlayModeStatus.AUDIO_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_action_audio_on_white)
                        MckitPlayerController.instance.audioOn()
                        MckitPlayModeGlobalInfo.audioStatus = MckitStatusConstants.PlayModeStatus.AUDIO_ON
                    }

                    else -> {
                        return false
                    }
                }
            }

            R.id.itemFlashlight -> {
                when (MckitPlayModeGlobalInfo.flashlightStatus) {
                    MckitStatusConstants.PlayModeStatus.FLASHLIGHT_ON -> {
                        item.setIcon(R.drawable.mscd_ic_action_flashlight_off_white)
                        MckitPlayerController.instance.flashlightOff()
                        MckitPlayModeGlobalInfo.flashlightStatus = MckitStatusConstants.PlayModeStatus.FLASHLIGHT_OFF
                    }

                    MckitStatusConstants.PlayModeStatus.FLASHLIGHT_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_action_flashlight_on_white)
                        MckitPlayerController.instance.flashlightOn()
                        MckitPlayModeGlobalInfo.flashlightStatus = MckitStatusConstants.PlayModeStatus.FLASHLIGHT_ON
                    }

                    else -> {
                        return false
                    }
                }
            }

            R.id.itemVibrator -> {
                when (MckitPlayModeGlobalInfo.vibratorStatus) {
                    MckitStatusConstants.PlayModeStatus.VIBRATION_ON -> {
                        item.setIcon(R.drawable.mscd_ic_action_vibrator_off_white)
                        MckitPlayerController.instance.vibratorOff()
                        MckitPlayModeGlobalInfo.vibratorStatus = MckitStatusConstants.PlayModeStatus.VIBRATION_OFF
                    }

                    MckitStatusConstants.PlayModeStatus.VIBRATION_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_action_vibrator_on_white)
                        MckitPlayerController.instance.vibratorOn()
                        MckitPlayModeGlobalInfo.vibratorStatus = MckitStatusConstants.PlayModeStatus.VIBRATION_ON
                    }

                    else -> {
                        return false
                    }
                }
            }

            R.id.itemTypewriter -> {
                when (MckitPlayModeGlobalInfo.typewriterStatus) {
                    MckitStatusConstants.PlayModeStatus.TYPEWRITER_ON -> {
                        item.setIcon(R.drawable.mscd_ic_typewriter_off_white)
                        MckitPlayerController.instance.typewriterOff()
                        MckitPlayModeGlobalInfo.typewriterStatus = MckitStatusConstants.PlayModeStatus.TYPEWRITER_OFF
                    }

                    MckitStatusConstants.PlayModeStatus.TYPEWRITER_OFF -> {
                        item.setIcon(R.drawable.mscd_ic_typewriter_on_white)
                        MckitPlayerController.instance.typewriterOn()
                        MckitPlayModeGlobalInfo.typewriterStatus = MckitStatusConstants.PlayModeStatus.TYPEWRITER_ON
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
