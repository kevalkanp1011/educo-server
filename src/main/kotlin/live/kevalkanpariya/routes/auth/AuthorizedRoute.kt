package live.kevalkanpariya.routes.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.api_response.ApiResponse
import org.litote.kmongo.coroutine.CoroutineDatabase

fun Route.authorizedRoute() {
    authenticate("auth-session") {
        get(Endpoint.Authorized.path) {

            call.respond(
                message = ApiResponse(success = true),
                status = HttpStatusCode.OK
            )
        }
    }
}