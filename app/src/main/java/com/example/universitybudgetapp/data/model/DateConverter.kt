// 파일: com/example/universitybudgetapp/data/model/DateConverter.kt
package com.example.universitybudgetapp.data.model

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateConverter {
    private val fmt = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String =
        date.format(fmt)

    @TypeConverter
    fun toLocalDate(value: String): LocalDate =
        LocalDate.parse(value, fmt)
}
