package com.atanana.sicounter.model.log

const val DEFAULT_FILE_NAME = "empty"
const val EXTENSION = ".txt"

class LogNameModel {
    private var _filename = DEFAULT_FILE_NAME

    fun onPlayerAdded(name: String) {
        if (_filename == DEFAULT_FILE_NAME) {
            _filename = name
        } else {
            _filename += "-$name"
        }
    }

    val fullFilename: String
        get() = _filename + EXTENSION
}