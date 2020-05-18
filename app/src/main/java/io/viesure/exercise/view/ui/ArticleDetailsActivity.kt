package io.viesure.exercise.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import io.viesure.exercise.R
import io.viesure.exercise.internal.*
import kotlinx.android.synthetic.main.activity_article_details.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ArticleDetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        val url: String = intent.getStringExtra(ARTICLE_IMAGE) ?: ""
        Glide.with(article_detail_image)
            .load(url)
            .centerCrop()
            .into(article_detail_image)

        article_detail_title.text = intent.getStringExtra(ARTICLE_TITLE)
        val dateInString: String = intent.getStringExtra(ARTICLE_RELEASE_DATE) ?: ""
        article_detail_release_date.text = createCorrectDateFormat(dateInString)
        article_detail_description.text = intent.getStringExtra(ARTICLE_DESCRIPTION)
        article_detail_author.text = "Author: ${intent.getStringExtra(ARTICLE_AUTHOR)}" ?: ""
    }

    private fun createCorrectDateFormat(dateInString: String): String {
        val originalFormat: DateFormat = SimpleDateFormat("M/d/yyyy", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat(ARTICLE_RELEASE_DATE_FORMAT)
        val desiredDateFormat: Date = originalFormat.parse(dateInString)
        return targetFormat.format(desiredDateFormat)
    }
}
