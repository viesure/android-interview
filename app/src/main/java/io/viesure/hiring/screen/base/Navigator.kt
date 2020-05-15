package io.viesure.hiring.screen.base

import android.app.Activity
import android.net.Uri
import io.viesure.hiring.util.OpenForTesting

//as navigation becomes more complex more logic can be added
@OpenForTesting
class Navigator(private val screenNavigators: List<BaseScreenNavigator>){
    fun open(activity: Activity, uri: Uri, addToBackStack : Boolean = true){
        screenNavigators.firstOrNull { it.screen == uri.host}?.open(activity, uri, addToBackStack)
    }
}