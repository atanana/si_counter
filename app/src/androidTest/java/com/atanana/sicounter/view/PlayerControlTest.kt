package com.atanana.sicounter.view

import android.test.AndroidTestCase
import android.widget.TextView
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.data.ScoreActionType.MINUS
import com.atanana.sicounter.data.ScoreActionType.PLUS
import com.atanana.sicounter.view.player_control.PlayerControl
import org.junit.Before
import rx.observers.TestSubscriber

class PlayerControlTest : AndroidTestCase() {
    lateinit var playerControl: PlayerControl

    @Before
    override fun setUp() {
        playerControl = PlayerControl(context, null)
    }

    fun testLayout() {
        playerControl.update(Score("test", 30), 123)
        val playerName = playerControl.findViewById(R.id.player_name) as TextView
        assertEquals("test", playerName.text.toString())

        val playerScore = playerControl.findViewById(R.id.player_score) as TextView
        assertEquals(30, playerScore.text.toString().toInt())
    }

    fun testScoreActions() {
        playerControl.update(Score("test", 0), 123)
        val subscriber = TestSubscriber<ScoreAction>()
        playerControl.scoreActions.subscribe(subscriber)

        playerControl.findViewById(R.id.add_score).performClick()
        playerControl.findViewById(R.id.subtract_score).performClick()

        subscriber.assertNoErrors()
        subscriber.assertReceivedOnNext(mutableListOf(ScoreAction(PLUS, 123), ScoreAction(MINUS, 123)))
    }

    fun testScoreActionsAfterPartialUpdate() {
        playerControl.update(Score("test", 0), 123)
        playerControl.update(Score("test", 0))
        val subscriber = TestSubscriber<ScoreAction>()
        playerControl.scoreActions.subscribe(subscriber)

        playerControl.findViewById(R.id.add_score).performClick()

        subscriber.assertNoErrors()
        subscriber.assertReceivedOnNext(mutableListOf(ScoreAction(PLUS, 123)))
    }
}