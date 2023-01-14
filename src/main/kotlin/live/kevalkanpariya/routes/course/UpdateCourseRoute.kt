package live.kevalkanpariya.routes.course

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.course.Course
import live.kevalkanpariya.domain.repository.CourseRepo

fun Route.updateCourse(
    repo: CourseRepo
) {
    post(Endpoint.UpdateCourse.path) {
        val requestCourse = try {
            call.receive<Course>()
        } catch (e: ContentTransformationException)
        {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if (repo.updateCourseById(requestCourse)) {
            call.respond(
                status = HttpStatusCode.OK,
                message = "course has been updated"
            )
        } else
        {
            call.respond(
                status = HttpStatusCode.Conflict,
                message = "course has not been updated"
            )
        }
    }
}