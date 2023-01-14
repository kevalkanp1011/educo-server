package live.kevalkanpariya.routes.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.api_response.ApiResponse
import live.kevalkanpariya.domain.model.auth.UserSession
import live.kevalkanpariya.domain.repository.UserDataSource

fun Route.getUserInfoRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate("auth-session") {
        get(Endpoint.GetUserInfo.path) {
            val userSession = call.principal<UserSession>()
            if (userSession == null) {
                app.log.info("INVALID SESSION")
                call.respondRedirect(Endpoint.Unauthorized.path)
            }
            else {
                try {
                    call.respond(
                        message = ApiResponse(
                            success = true,
                            user = userDataSource.getUserInfo(userID = userSession.id)
                        ),
                        status = HttpStatusCode.OK
                    )
                } catch (e: Exception) {
                    app.log.info("GETTING USER INFO ERROR: ${e.message}")
                    call.respondRedirect(Endpoint.Unauthorized.path)
                }
            }
        }
    }
}