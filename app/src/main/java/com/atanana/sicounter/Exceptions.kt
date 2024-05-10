package com.atanana.sicounter

open class SiCounterException(message: String) : RuntimeException(message)

class UnknownId(val id: Int) : SiCounterException("Unknown id $id!")

class MissingId : SiCounterException("Id is missing!")

inline fun safeThrow(block: () -> Throwable) {
    if (BuildConfig.DEBUG) {
        throw block()
    }
}
