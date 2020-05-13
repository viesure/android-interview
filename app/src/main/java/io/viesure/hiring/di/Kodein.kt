package io.viesure.hiring.di


import org.kodein.di.Kodein

fun Kodein.MainBuilder.initKodein() {
    import(networkConnectivityModule)
    import(viewModelModule)
    import(repositoryModule)
    import(networkApiModule)
    import(useCaseModule)
    import(databaseModule)
    import(coroutineModule)
}
