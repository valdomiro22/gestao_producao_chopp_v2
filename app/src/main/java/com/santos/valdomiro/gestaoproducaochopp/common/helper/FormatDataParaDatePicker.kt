package com.santos.valdomiro.gestaoproducaochopp.common.helper

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

object DataParaDatePicker {

    fun LocalDate?.toEpochMillis(): Long? {
        return this?.atStartOfDay(ZoneOffset.UTC) // 👈 CORRIGIDO
            ?.toInstant()
            ?.toEpochMilli()
    }

    fun Long?.toLocalDate(): LocalDate? {
        return this?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneOffset.UTC) // 👈 CORRIGIDO
                .toLocalDate()
        }
    }

    fun LocalDate?.formatToBrazilian(): String {
        if (this == null) return "Selecione uma data"
        return DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("pt", "BR"))
            .format(this)
    }
}