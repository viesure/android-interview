package io.viesure.hiring.datasource.network.dto

import android.net.Uri
import java.util.*

data class ArticleDto(
    val id: String,
    val title: String?,
    val description: String?,
    val author: String?,
    val release_date: Date?,
    val image: String?
)