package com.atanana.sicounter.model

import com.atanana.sicounter.data.Score
import com.atanana.sicounter.data.ScoreAction
import com.atanana.sicounter.exceptions.UnknownId
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.Subject

open class ScoresModel(private val newPlayersNames: Observable<String>) {
    private val playerScores: MutableMap<Int, Score> = hashMapOf()
    private val new: Subject<Pair<Score, Int>, Pair<Score, Int>> = PublishSubject()
    private val updated: Subject<Pair<Score, Int>, Pair<Score, Int>> = PublishSubject()
    open val newPlayers: Observable<Pair<Score, Int>>
        get() = new

    open val updatedPlayers: Observable<Pair<Score, Int>>
        get() = updated

    init {
        newPlayersNames.subscribe({ newPlayer ->
            val newScore = Score(newPlayer, 0)
            val newId = playerScores.size
            playerScores.put(newId, newScore)
            new.onNext(Pair(newScore, newId))
        })
    }

    fun subscribeToScoreActions(actions: Observable<ScoreAction>) {
        actions.subscribe({ action ->
            val oldScore = playerScores[action.id] ?: throw UnknownId(action.id)
            val newScore = oldScore.copy(score = oldScore.score + action.absolutePrice)
            playerScores[action.id] = newScore
            updated.onNext(Pair(newScore, action.id))
        })
    }

    fun playerNameById(id: Int): String {
        return playerScores[id]?.name ?: throw UnknownId(id)
    }
}