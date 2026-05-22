package com.santos.valdomiro.gestaoproducaochopp.common.helper

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Instant.formatarData(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        .withZone(ZoneId.systemDefault())

    return formatter.format(this)
}

/** Fromata Instant para horas com segundos */
fun Instant.formatarHorario(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
        .withZone(ZoneId.systemDefault())

    return formatter.format(this)
}

/** Fromata Instant para horas com segundos */
fun Instant.formatarHorarioComSegundos(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    return formatter.format(this)
}

/** Fromata Instant para horas com segundos e milisegundos */
fun Instant.formatarHorarioComMilissegundos(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS")
        .withZone(ZoneId.systemDefault())

    return formatter.format(this)
}

/** Fromata Instant para horas: data horas */
fun Instant.formatDataComHorario(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
    return formatter.format(this)
}