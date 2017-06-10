package com.atanana.sicounter.logging

import io.reactivex.Observable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogsWriter(historyObservable: Observable<String>) {
    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass) }

    init {
        historyObservable.subscribe({ item ->
            logger.info(item)
        })
    }
}