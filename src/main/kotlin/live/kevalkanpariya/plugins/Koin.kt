package live.kevalkanpariya.plugins

import io.ktor.server.application.*
import live.kevalkanpariya.di.getKoinModule
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin){
        modules(getKoinModule())
    }
}