package io.viesure.hiring.model

import android.net.Uri
import java.util.*

data class Article(
    val id: String,
    val title: String,
    val description: String?,
    val author: String?,
    val releaseDate: Date?,
    val image: Uri?
)