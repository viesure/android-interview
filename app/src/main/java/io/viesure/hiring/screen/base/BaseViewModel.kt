package io.viesure.hiring.screen.base

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.viesure.hiring.util.OpenForTesting

@OpenForTesting
abstract class BaseViewModel(application : Application) : ViewModel(), ErrorResolver by ErrorResolverImpl(application){
    val navigation = MutableLiveData<Event<Uri>>()

    fun navigate(uri: Uri){
        navigation.value = Event(uri)
    }
}