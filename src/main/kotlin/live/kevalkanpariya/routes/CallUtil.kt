package live.kevalkanpariya.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import live.kevalkanpariya.plugins.userId

val ApplicationCall.userId: String
    get() = principal<JWTPrincipal>()?.userId.toString()