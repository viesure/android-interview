package io.viesure.hiring.datasource.database.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "articles")
data class ArticleDbEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "word") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "releaseDate") val releaseDate: Long?,
    @ColumnInfo(name = "image") val image: String?
)