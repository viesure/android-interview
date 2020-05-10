package io.viesure.hiring.di

import android.app.Application
import io.viesure.hiring.ViesureApplication
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

var baseKodein = Kodein {
    import(networkConnectivityModule)
    import(viewModelModule)
    import(repositoryModule)
    import(networkApiModule)
    import(useCaseModule)
    import(databaseModule)
    import(coroutineModule)

    bind<Application>() with singleton { ViesureApplication.application }
}

var appKodein = baseKodein
