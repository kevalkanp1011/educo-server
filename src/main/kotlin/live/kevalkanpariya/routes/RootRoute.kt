package live.kevalkanpariya.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.model.Endpoint

fun Route.rootRoute() {
    get(Endpoint.Root.path) {
        call.respondText("Welcome to EduCo API 2.0!")
    }
}