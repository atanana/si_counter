package com.atanana.sicounter.model.log

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class LogNameModelTest {
    lateinit var model: LogNameModel

    lateinit var newPlayersSubject: Subject<String>

    @Before
    fun setUp() {
        newPlayersSubject = PublishSubject.create()
        model = LogNameModel(newPlayersSubject)
    }

    @Test
    fun shouldHasDefaultFilename() {
        assertThat(model.fullFilename).isEqualTo("empty.txt")
    }

    @Test
    fun shouldEmitDefaultFilename() {
        val subscriber = model.filenameObservable.test()
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
        val subscriber = model.filenameObservable.test()

        newPlayersSubject.onNext("test 1")
        newPlayersSubject.onNext("test 2")

        subscriber.assertValues("empty.txt", "test 1.txt", "test 1-test 2.txt")
    }
}