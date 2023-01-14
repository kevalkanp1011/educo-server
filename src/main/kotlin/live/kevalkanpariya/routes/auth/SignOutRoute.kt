package live.kevalkanpariya.routes.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.api_response.ApiResponse
import live.kevalkanpariya.domain.model.auth.UserSession

fun Route.signOutRoute() {
    authenticate("auth-session") {
        get(Endpoint.SignOut.path) {
            call.sessions.clear<UserSession>()
            call.respond(
                message = ApiResponse(success = true),
                status = HttpStatusCode.OK
            )
        }
    }
}