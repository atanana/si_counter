package com.atanana.sicounter.presenter

import android.view.ViewGroup
import com.atanana.sicounter.PlayerControlMatcher
import com.atanana.sicounter.data.Score
import com.atanana.sicounter.model.ScoresModel
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.argThat
import rx.lang.kotlin.PublishSubject

class ScoresPresenterTest {
    @Test
    fun newPlayers() {
        val scoresModel = Mockito.mock(ScoresModel::class.java)
        val newPlayers = PublishSubject<Score>()
        `when`(scoresModel.newPlayers).thenReturn(newPlayers)
        val container = Mockito.mock(ViewGroup::class.java)
        ScoresPresenter(scoresModel, container)

        newPlayers.onNext(Score("test", 0))
        Mockito.verify(container).addView(argThat(PlayerControlMatcher("test", 0)))
    }
}