package live.kevalkanpariya.routes.course

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.request.CourseId
import live.kevalkanpariya.domain.repository.CourseRepo

fun Route.getCourse(
    repo: CourseRepo
) {

    get(Endpoint.GetCourse.path) {
        val courseId = call.receive<CourseId>()
        val course = repo.getCourseById(courseId.id)
        course?.let {
            call.respond(
                status = HttpStatusCode.OK,
                message = it
            )
        }?: call.respond(
            HttpStatusCode.OK,
            message = "No Such Course is available"
        )
    }
}