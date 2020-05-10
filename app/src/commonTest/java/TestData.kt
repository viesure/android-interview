import android.net.Uri
import io.viesure.hiring.datasource.database.mappers.toArticleDbEntity
import io.viesure.hiring.datasource.network.dto.ArticleDto
import io.viesure.hiring.datasource.network.mappers.toArticle
import java.util.*

val newArticleDto = ArticleDto("1", "title1", "description1", "author1", Date(3), null)
val oldArticleDto = ArticleDto("2", "title2", "description2", "author2", Date(2), null)
val oldestArticleDto = ArticleDto("3", "title3", "description3", "author3", Date(1), "http://fake.com/image")
val emptyArticleDto = ArticleDto("4", null, null, null, null, null)

val newArticle = newArticleDto.toArticle()
val oldArticle = oldArticleDto.toArticle()
val oldestArticle = oldestArticleDto.toArticle()
val emptyArticle = emptyArticleDto.toArticle()

val newArticleDbEntity = newArticle.toArticleDbEntity()
val oldArticleDbEntity = oldArticle.toArticleDbEntity()
val oldestArticleDbEntity = oldestArticle.toArticleDbEntity()
val emptyArticleDbEntity = emptyArticle.toArticleDbEntity()