package com.atanana.sicounter.view

import android.test.AndroidTestCase
import com.atanana.sicounter.R
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.data.ScoreActionType.MINUS
import com.atanana.sicounter.data.ScoreActionType.PLUS
import org.junit.Test
import rx.observers.TestSubscriber

class PlayerControlTest : AndroidTestCase() {
    @Test
    fun testScoreActions() {
        val playerControl = PlayerControl(context, null)
        playerControl.update(Score("test", 0), 123)
        val subscriber = TestSubscriber<ScoreAction>()
        playerControl.scoreActions.subscribe(subscriber)

        playerControl.findViewById(R.id.add_score).performClick()
        playerControl.findViewById(R.id.subtract_score).performClick()

        subscriber.assertNoErrors()
        subscriber.assertReceivedOnNext(mutableListOf(ScoreAction(PLUS, 123), ScoreAction(MINUS, 123)))
    }
}