package com.atanana.sicounter.model

import com.atanana.sicounter.data.Score
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.Subject

open class ScoresModel(private val newPlayersNames: Observable<String>) {
    private val playerScores: MutableList<Score> = arrayListOf()
    private val new: Subject<Score, Score> = PublishSubject()
    open val newPlayers: Observable<Score>
        get() = new

    init {
        newPlayersNames.subscribe({ newPlayer ->
            val newScore = Score(newPlayer, 0)
            playerScores.add(newScore)
            new.onNext(newScore)
        })
    }
}