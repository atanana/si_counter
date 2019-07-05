package com.atanana.sicounter.model

import android.os.Bundle
import android.test.AndroidTestCase
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType.MINUS
import com.atanana.sicounter.data.action.ScoreActionType.PLUS
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import org.hamcrest.Matchers
import org.junit.Assert.assertThat
import java.util.*

class ScoresModelBundleTest : AndroidTestCase() {
    fun testSaveInformation() {
        val model = ScoresModel(just("test 1", "test 2"), HistoryModel(ScoreHistoryFormatter(context)))
        model.onScoreAction(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(MINUS, 20, 1)
        ))

        val bundle = Bundle()
        model.save(bundle)

        @Suppress("UNCHECKED_CAST")
        assertEquals(hashMapOf(
                Pair(0, Score("test 1", 10)),
                Pair(1, Score("test 2", -20))
        ), bundle.getSerializable(KEY_SCORES) as TreeMap<Int, Score>)
    }

    fun testRestoreInformation() {
        val model = ScoresModel(empty(), HistoryModel(ScoreHistoryFormatter(context)))
        val playersSubscriber = model.newPlayersObservable.test()

        val treeMap = TreeMap<Int, Score>()
        treeMap[0] = Score("test 1", 40)
        treeMap[1] = Score("test 2", -50)
        val bundle = Bundle()
        bundle.putSerializable(KEY_SCORES, treeMap)
        bundle.putStringArrayList(KEY_HISTORY, arrayListOf("test 1 history", "test 2 history"))
        model.restore(bundle)

        playersSubscriber.assertNoErrors()
        assertThat(playersSubscriber.values(), Matchers.containsInAnyOrder(
                Pair(Score("test 1", 40), 0),
                Pair(Score("test 2", -50), 1))
        )
    }
}