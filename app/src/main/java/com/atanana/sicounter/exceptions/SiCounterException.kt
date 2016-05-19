package com.atanana.sicounter.exceptions

class SiCounterException(detailMessage: String?, throwable: Throwable?) : RuntimeException(detailMessage, throwable) {
    constructor(message: String) : this(message, null)
}