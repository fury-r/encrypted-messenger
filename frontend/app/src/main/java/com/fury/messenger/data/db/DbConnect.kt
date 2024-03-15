package com.fury.messenger.data.db
import org.jetbrains.exposed.sql.*
fun connectToDb(){
    Database.connect("jdbc:sqlite:test.db", driver = "org.sqlite.JDBC")

}