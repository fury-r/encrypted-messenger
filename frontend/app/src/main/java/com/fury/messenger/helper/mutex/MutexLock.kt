package com.fury.messenger.helper.mutex

object MutexLock {

    private var dbLock = false

    fun getDbLock(): Boolean {
        return dbLock
    }

    fun setDbLock(value: Boolean) {
        dbLock = value
    }
}