package com.santos.valdomiro.gestaoproducaochopp.common.helper

import com.google.firebase.Timestamp
import java.time.Instant

fun Timestamp.toInstant(): Instant {
    return Instant.ofEpochSecond(seconds, nanoseconds.toLong())
}

fun Instant.toTimestamp(): Timestamp {
    return Timestamp(epochSecond, nano)
}