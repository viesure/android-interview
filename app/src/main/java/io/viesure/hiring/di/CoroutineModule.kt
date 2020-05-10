package io.viesure.hiring.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import kotlin.coroutines.CoroutineContext

val coroutineModule = Kodein.Module(DiConfig.MODULE_NAME_COROUTINE) {
    bind<CoroutineDispatcher>(DiConfig.TAG_DEFAULT_DISPATCHER) with provider { Dispatchers.Default }

    bind<CoroutineDispatcher>(DiConfig.TAG_IO_DISPATCHER) with provider { Dispatchers.IO }
}