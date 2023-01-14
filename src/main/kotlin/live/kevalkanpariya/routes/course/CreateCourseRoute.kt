package live.kevalkanpariya.routes.course

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.course.Course
import live.kevalkanpariya.domain.repository.CourseRepo

fun Route.createCourse(
    repo: CourseRepo
) {
    post(Endpoint.CreateCourse.path) {
        val requestCourse = try {
            call.receive<Course>()
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if(repo.createCourse(requestCourse)) {
            call.respond(
                status = HttpStatusCode.OK,
                message = "Course has been created"
            )
        } else {
            call.respond(
                status = HttpStatusCode.Conflict,
                message = "Course has not been created"
            )
        }
    }
}