package com.atanana.sicounter.model.log

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.ReplaySubject

const val DEFAULT_FILE_NAME = "empty"
const val EXTENSION = ".txt"

class LogNameModel(private val newPlayersNameProvider: Observable<String>) {
    private var _filename = DEFAULT_FILE_NAME

    fun connect(): Disposable =
        newPlayersNameProvider.subscribe {
            if (_filename == DEFAULT_FILE_NAME) {
                _filename = it
            } else {
                _filename += "-$it"
            }
        }

    val fullFilename: String
        get() = _filename + EXTENSION
}