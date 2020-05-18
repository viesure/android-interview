package io.viesure.exercise.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Article(
    val id: Int,
    val author: String,
    val description: String,
    val image: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    var dateForSorting: Date?
)