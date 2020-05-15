package io.viesure.hiring.screen.articledetail

import android.app.Activity
import android.net.Uri
import io.viesure.hiring.MainActivity
import io.viesure.hiring.screen.base.BaseScreenNavigator

class ArticleDetailNavigator : BaseScreenNavigator() {
    override val screen = SCREEN

    companion object {
        const val SCREEN = "article_detail"

        fun uri(articleId: String) = buildUri(SCREEN, listOf(articleId))
    }

    override fun open(activity: Activity, uri: Uri, addToBackStack: Boolean) {
        when (activity) {
            is MainActivity -> activity.openFragment(ArticleDetailFragment.newInstance(uri.pathSegments.getOrNull(0) ?: ""), addToBackStack)
            //as navigation becomes more complex more logic can come here
        }
    }
}