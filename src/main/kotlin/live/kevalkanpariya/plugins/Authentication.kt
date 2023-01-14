package live.kevalkanpariya.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.auth.UserSession

fun Application.configureAuth() {
    install(Authentication) {
        session<UserSession>(name = "auth-session") {
            validate { session ->
                session
            }
            challenge {
                call.respondRedirect(Endpoint.Unauthorized.path)
            }
        }
    }
}