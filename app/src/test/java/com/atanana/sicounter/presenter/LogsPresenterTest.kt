package com.atanana.sicounter.presenter

import com.atanana.sicounter.view.ScoresLog
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import rx.Observable

class LogsPresenterTest {
    @Mock
    lateinit var scoresLog: ScoresLog

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun shouldReflectHistoryChangesInView() {
        LogsPresenter(Observable.just("test 1", "test 2", "test 3"), scoresLog)
        verify(scoresLog).append("test 1\n")
        verify(scoresLog).append("test 2\n")
        verify(scoresLog).append("test 3\n")
    }
}