package com.githubyss.mobile.morsecode.app.util.player.controller.action

/**
 * MscdPlayerAction.ktt
 * <Description>
 * <Details>
 *
 * @designPatterns State
 *
 * @author Ace Yan
 * @github githubyss
 */
interface MscdPlayerAction {
    fun startPlay(message: String, baseDelay: Long)
    fun stopPlay()
    fun releaseResource()
}
