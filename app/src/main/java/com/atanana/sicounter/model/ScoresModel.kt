package com.atanana.sicounter.model

import com.atanana.sicounter.data.Score
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.Subject

open class ScoresModel(private val newPlayersNames: Observable<String>) {
    private val playerScores: MutableMap<Int, Score> = hashMapOf()
    private val new: Subject<Pair<Score, Int>, Pair<Score, Int>> = PublishSubject()
    open val newPlayers: Observable<Pair<Score, Int>>
        get() = new

    init {
        newPlayersNames.subscribe({ newPlayer ->
            val newScore = Score(newPlayer, 0)
            val newId = playerScores.size
            playerScores.put(newId, newScore)
            new.onNext(Pair(newScore, newId))
        })
    }
}