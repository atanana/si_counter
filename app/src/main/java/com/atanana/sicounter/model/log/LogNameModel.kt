package com.atanana.sicounter.model.log

import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject

const val DEFAULT_FILE_NAME = "empty"
const val EXTENSION = ".txt"

class LogNameModel(newPlayersNameProvider: Observable<String>) {
    private var _filename = DEFAULT_FILE_NAME

    private val filenameSubject: ReplaySubject<String> = ReplaySubject.createWithSize(1)
    val filenameObservable: Observable<String> = filenameSubject

    init {
        notifyFilenameChanged()

        newPlayersNameProvider.subscribe {
            if (_filename == DEFAULT_FILE_NAME) {
                _filename = it
            } else {
                _filename += "-" + it
            }

            notifyFilenameChanged()
        }
    }

    private fun notifyFilenameChanged() {
        filenameSubject.onNext(fullFilename)
    }

    val fullFilename get() = _filename + EXTENSION
}