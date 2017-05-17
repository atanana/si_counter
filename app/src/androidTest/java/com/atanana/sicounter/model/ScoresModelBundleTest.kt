package com.atanana.sicounter.model

import android.os.Bundle
import android.test.AndroidTestCase
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType.MINUS
import com.atanana.sicounter.data.action.ScoreActionType.PLUS
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import org.hamcrest.Matchers
import org.junit.Assert.assertThat
import rx.Observable.empty
import rx.Observable.just
import rx.observers.TestSubscriber
import java.util.*

class ScoresModelBundleTest : AndroidTestCase() {
    fun testSaveInformation() {
        val formatter = ScoreHistoryFormatter(context)
        val model = ScoresModel(just("test 1", "test 2"), formatter)
        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(MINUS, 20, 1)
        ))

        val bundle = Bundle()
        model.save(bundle)

        assertEquals(arrayListOf(
                formatter.formatNewPlayer("test 1"),
                formatter.formatNewPlayer("test 2"),
                formatter.formatScoreAction(ScoreAction(PLUS, 10, 0), "test 1"),
                formatter.formatScoreAction(ScoreAction(MINUS, 20, 1), "test 2")
        ), bundle.getStringArrayList(KEY_HISTORY))
        @Suppress("UNCHECKED_CAST")
        assertEquals(hashMapOf(
                Pair(0, Score("test 1", 10)),
                Pair(1, Score("test 2", -20))
        ), bundle.getSerializable(KEY_SCORES) as HashMap<Int, Score>)
    }

    fun testRestoreInformation() {
        val model = ScoresModel(empty(), ScoreHistoryFormatter(context))
        val playersSubscriber = TestSubscriber<Pair<Score, Int>>()
        model.newPlayersObservable.subscribe(playersSubscriber)
        val historySubscriber = TestSubscriber<String>()
        model.historyChangesObservable.subscribe(historySubscriber)

        val bundle = Bundle()
        bundle.putSerializable(KEY_SCORES, hashMapOf(
                Pair(0, Score("test 1", 40)),
                Pair(1, Score("test 2", -50))
        ))
        bundle.putStringArrayList(KEY_HISTORY, arrayListOf("test 1 history", "test 2 history"))
        model.restore(bundle)

        playersSubscriber.assertNoErrors()
        assertThat(playersSubscriber.onNextEvents, Matchers.containsInAnyOrder(
                Pair(Score("test 1", 40), 0),
                Pair(Score("test 2", -50), 1))
        )
        historySubscriber.assertNoErrors()
        historySubscriber.assertReceivedOnNext(listOf("test 1 history", "test 2 history"))
    }
}