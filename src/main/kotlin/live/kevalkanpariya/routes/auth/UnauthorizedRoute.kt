package live.kevalkanpariya.routes.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.api_response.ApiResponse

fun Route.unauthorizedRoute() {
    authenticate("auth-session") {
        get(Endpoint.Unauthorized.path) {
            call.respond(
                message = ApiResponse(success = true),
                status = HttpStatusCode.OK
            )
        }
    }
}