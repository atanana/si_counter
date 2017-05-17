package com.atanana.sicounter.model.log

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber
import rx.subjects.Subject

class LogNameModelTest {
    lateinit var model: LogNameModel

    lateinit var newPlayersSubject: Subject<String, String>

    @Before
    fun setUp() {
        newPlayersSubject = PublishSubject<String>()
        model = LogNameModel(newPlayersSubject)
    }

    @Test
    fun shouldHasDefaultFilename() {
        assertThat(model.fullFilename).isEqualTo("empty.txt")
    }

    @Test
    fun shouldEmitDefaultFilename() {
        val subscriber = TestSubscriber<String>()
        model.filenameObservable.subscribe(subscriber)
        subscriber.assertValues("empty.txt")
    }

    @Test
    fun shouldUpdateFilename() {
        newPlayersSubject.onNext("test 1")
        assertThat(model.fullFilename).isEqualTo("test 1.txt")

        newPlayersSubject.onNext("test 2")
        assertThat(model.fullFilename).isEqualTo("test 1-test 2.txt")
    }

    @Test
    fun shouldNotifyAboutFilenameChanges() {
        val subscriber = TestSubscriber<String>()
        model.filenameObservable.subscribe(subscriber)

        newPlayersSubject.onNext("test 1")
        newPlayersSubject.onNext("test 2")

        subscriber.assertValues("empty.txt", "test 1.txt", "test 1-test 2.txt")
    }
}