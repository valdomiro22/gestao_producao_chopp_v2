package com.santos.valdomiro.gestaoproducaochopp.features.barril.data.mapper

import com.google.firebase.Timestamp
import java.time.Instant

fun Timestamp.toInstant(): Instant {
    return Instant.ofEpochSecond(seconds, nanoseconds.toLong())
}

fun Instant.toTimestamp(): Timestamp {
    return Timestamp(epochSecond, nano)
}