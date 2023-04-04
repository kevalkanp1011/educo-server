package live.kevalkanpariya.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.rootRoute() {
    get("/") {

        call.respondText("Welcome to EduCo API 4.0")
    }
}