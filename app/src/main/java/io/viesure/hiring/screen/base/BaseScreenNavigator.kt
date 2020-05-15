package io.viesure.hiring.screen.base

import android.app.Activity
import android.net.Uri

abstract class BaseScreenNavigator {
    companion object{
        protected const val SCHEME = "viesure"
        fun buildUri(screen: String, pathSegments : List<String>? = null) : Uri{
            val builder = Uri.Builder().scheme(SCHEME).authority(screen)
            pathSegments?.forEach {
                builder.appendPath(it)
            }
            return builder.build()
        }
    }

    abstract val screen: String
    abstract fun open(activity: Activity, uri: Uri, addToBackStack: Boolean = true)
}