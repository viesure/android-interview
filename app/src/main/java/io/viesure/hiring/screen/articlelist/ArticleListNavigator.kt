package io.viesure.hiring.screen.articlelist

import android.app.Activity
import android.net.Uri
import io.viesure.hiring.MainActivity
import io.viesure.hiring.screen.base.BaseScreenNavigator

class ArticleListNavigator : BaseScreenNavigator() {
    override val screen = SCREEN

    companion object{
        const val SCREEN = "article_list"
        fun uri() = buildUri(SCREEN)
    }

    override fun open(activity: Activity, uri: Uri, addToBackStack: Boolean) {
        when(activity){
            is MainActivity -> activity.openFragment(ArticleListFragment(), addToBackStack)
            //as navigation becomes more complex more logic can come here
        }
    }
}