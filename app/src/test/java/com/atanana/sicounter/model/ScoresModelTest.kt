package com.atanana.sicounter.model

import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.action.ScoreAction
import com.atanana.sicounter.data.action.ScoreActionType.MINUS
import com.atanana.sicounter.data.action.ScoreActionType.PLUS
import io.reactivex.Observable.just
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ScoresModelTest {
    @Mock
    lateinit var historyModel: HistoryModel

    private lateinit var newPlayers: Subject<String>

    private lateinit var model: ScoresModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        newPlayers = PublishSubject.create()
        model = ScoresModel(newPlayers, historyModel)
    }

    @Test
    fun shouldNotifyAboutNewPlayersScores() {
        val subscriber = model.newPlayersObservable.test()

        newPlayers.onNext("test 1")
        newPlayers.onNext("test 2")

        subscriber.assertValues(Pair(Score("test 1", 0), 0), Pair(Score("test 2", 0), 1))
    }

    @Test
    fun shouldNotifyAboutNewPlayersHistory() {
        newPlayers.onNext("test 1")
        newPlayers.onNext("test 2")

        verify(historyModel).onPlayerAdded("test 1")
        verify(historyModel).onPlayerAdded("test 2")
    }

    @Test
    fun shouldAddNewPlayersToScores() {
        newPlayers.onNext("test 1")
        newPlayers.onNext("test 2")

        assertThat(model.scores).containsExactly(Score("test 1", 0), Score("test 2", 0))
    }

    @Test
    fun shouldNotifyAboutScoresUpdate() {
        model = ScoresModel(just("test 1", "test 2"), historyModel)

        val subscriber = model.updatedPlayersObservable.test()

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 20, 1)
        ))

        subscriber.assertValues(
                Pair(Score("test 1", 10), 0),
                Pair(Score("test 1", 30), 0),
                Pair(Score("test 2", -20), 1)
        )
    }

    @Test
    fun shouldNotifyAboutUpdateScoresHistory() {
        model = ScoresModel(just("test 1", "test 2"), historyModel)

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 20, 1)
        ))

        verify(historyModel).onScoreAction(ScoreAction(PLUS, 10, 0), "test 1")
        verify(historyModel).onScoreAction(ScoreAction(PLUS, 20, 0), "test 1")
        verify(historyModel).onScoreAction(ScoreAction(MINUS, 20, 1), "test 2")
    }

    @Test
    fun shouldUpdateScores() {
        newPlayers.onNext("test 1")
        newPlayers.onNext("test 2")

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 20, 1)
        ))

        assertThat(model.scores).containsExactly(Score("test 1", 30), Score("test 2", -20))
    }

    @Test
    fun shouldNotifyAboutScoresReset() {
        model = ScoresModel(just("test 1", "test 2"), historyModel)

        val subscriber = model.updatedPlayersObservable.test()

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 20, 1)
        ))
        model.reset()

        assertThat(subscriber.values()).endsWith(Pair(Score("test 1", 0), 0), Pair(Score("test 2", 0), 1))
    }

    @Test
    fun shouldNotifyAboutResetScoresHistory() {
        model.reset()
        verify(historyModel).reset()
    }

    @Test
    fun shouldResetScores() {
        newPlayers.onNext("test 1")
        newPlayers.onNext("test 2")

        model.subscribeToScoreActions(just(
                ScoreAction(PLUS, 10, 0),
                ScoreAction(PLUS, 20, 0),
                ScoreAction(MINUS, 20, 1)
        ))
        model.reset()

        assertThat(model.scores).containsExactly(Score("test 1", 0), Score("test 2", 0))
    }
}