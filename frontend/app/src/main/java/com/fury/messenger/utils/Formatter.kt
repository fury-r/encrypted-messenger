package com.fury.messenger.utils

fun formateMilliSeccond(milliseconds: Long): String {
    var finalTimerString = ""
    var secondsString = ""

    val hours = (milliseconds / (1000 * 60 * 60)).toInt()
    val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
    val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()

    if (hours > 0) {
        finalTimerString = "$hours:"
    }

    secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        "" + seconds
    }
    finalTimerString = "$finalTimerString$minutes:$secondsString"


    return finalTimerString
}