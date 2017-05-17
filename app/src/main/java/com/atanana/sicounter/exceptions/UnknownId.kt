package com.atanana.sicounter.exceptions

class UnknownId(val id: Int) : SiCounterException("Unknown id $id!")