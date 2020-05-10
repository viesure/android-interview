package io.viesure.hiring.screen.articledetail

import android.net.Uri
import android.text.Spanned
import android.text.SpannedString
import java.util.*

data class ArticleDetailViewContent(
    val title: String,
    val description: String,
    val author: Spanned,
    val releaseDate: String,
    val image: Uri?
){
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is ArticleDetailViewContent) return false
        return title == other.title &&
                description == other.description &&
                author.toString() == other.author.toString() &&
                releaseDate == other.releaseDate &&
                image == other.image
    }

    override fun hashCode(): Int {
        return Objects.hash(title, description, author.toString(), releaseDate, image)
    }
}