package com.atanana.sicounter

import android.widget.TextView
import com.atanana.sicounter.view.PlayerControl
import org.mockito.ArgumentMatcher

class PlayerControlMatcher(private val name: String, private val score: Int) : ArgumentMatcher<PlayerControl> {
    override fun matches(item: Any?): Boolean {
        return when (item) {
            is PlayerControl -> {
                val playerName = item.findViewById(R.id.player_name) as TextView
                val playerScore = item.findViewById(R.id.player_score) as TextView
                playerName.text.toString() == name && playerScore.text.toString().toInt() == score
            }
            else -> false
        }
    }
}