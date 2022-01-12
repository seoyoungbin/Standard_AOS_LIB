package com.syb.syblibrary.data.local.util

import androidx.room.TypeConverter
import java.sql.Time
import java.util.*

class Converters {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? = if (value == null) null else Date(value)

    @TypeConverter
    fun dateToLong(value: Date?): Long? = value?.time

    @TypeConverter
    fun timestampToTime(value: Long?): Time? = if(value == null) null else Time(value)

    @TypeConverter
    fun timeToLong(value: Time?): Long? = value?.time

}