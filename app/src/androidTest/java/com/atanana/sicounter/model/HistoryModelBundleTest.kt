package com.atanana.sicounter.model

import android.os.Bundle
import android.test.AndroidTestCase
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType.MINUS
import com.atanana.sicounter.data.action.ScoreActionType.PLUS
import com.atanana.sicounter.presenter.ScoreHistoryFormatter
import rx.observers.TestSubscriber

class HistoryModelBundleTest : AndroidTestCase() {
    fun testSaveInformation() {
        val formatter = ScoreHistoryFormatter(context)
        val model = HistoryModel(formatter)
        model.onPlayerAdded("test 1")
        model.onPlayerAdded("test 2")
        model.onScoreAction(ScoreAction(PLUS, 10, 0), "test 1")
        model.onScoreAction(ScoreAction(MINUS, 20, 1), "test 2")

        val bundle = android.os.Bundle()
        model.save(bundle)

        assertEquals(arrayListOf(
                formatter.formatNewPlayer("test 1"),
                formatter.formatNewPlayer("test 2"),
                formatter.formatScoreAction(com.atanana.sicounter.data.action.ScoreAction(com.atanana.sicounter.data.action.ScoreActionType.PLUS, 10, 0), "test 1"),
                formatter.formatScoreAction(com.atanana.sicounter.data.action.ScoreAction(com.atanana.sicounter.data.action.ScoreActionType.MINUS, 20, 1), "test 2")
        ), bundle.getStringArrayList(KEY_HISTORY))
    }

    fun testRestoreInformation() {
        val model = HistoryModel(ScoreHistoryFormatter(context))
        val historySubscriber = TestSubscriber<String>()
        model.historyChangesObservable.subscribe(historySubscriber)

        val bundle = Bundle()
        bundle.putSerializable(KEY_SCORES, hashMapOf(
                Pair(0, Score("test 1", 40)),
                Pair(1, Score("test 2", -50))
        ))
        bundle.putStringArrayList(KEY_HISTORY, arrayListOf("test 1 history", "test 2 history"))
        model.restore(bundle)

        historySubscriber.assertNoErrors()
        historySubscriber.assertReceivedOnNext(listOf("test 1 history", "test 2 history"))
    }
}