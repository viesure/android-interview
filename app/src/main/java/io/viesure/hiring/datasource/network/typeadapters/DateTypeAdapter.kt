package io.viesure.hiring.datasource.network.typeadapters

import android.net.Uri
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.*

class DateTypeAdapter() {
    private val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    @ToJson
    fun toJson(date: Date) = formatter.format(date)

    @FromJson
    fun fromJson(dateString: String) = try {
        formatter.parse(dateString)
    } catch (e: Exception) {
        throw JsonDataException("Invalid date format")
    }
}