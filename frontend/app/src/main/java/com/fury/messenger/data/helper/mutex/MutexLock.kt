package com.fury.messenger.data.helper.mutex

object MutexLock {

    private var dbLock=false

   fun getDbLock():Boolean{
        return this.dbLock
    }

    fun setDbLock(value:Boolean){
        this.dbLock=value
    }
}