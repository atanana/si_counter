package com.atanana.sicounter.view.player_control

import android.content.Context

class DefaultPlayerControlFabric(val context: Context) : PlayerControlFabric {
    override fun build(): PlayerControl {
        return PlayerControl(context, null)
    }
}