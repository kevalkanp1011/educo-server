package live.kevalkanpariya.routes.course

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.request.CourseId
import live.kevalkanpariya.domain.repository.CourseRepo

fun Route.deleteCourse(
    repo: CourseRepo
) {
    delete(Endpoint.DeleteCourse.path) {
        val courseId = call.receive<CourseId>()
        if (repo.deleteCourseById(courseId.id)) {
            call.respond(
                status = HttpStatusCode.OK,
                message = "Course has been deleted"
            )
        } else {
            call.respond(
                status = HttpStatusCode.Conflict,
                message = "Course has not been deleted"
            )
        }
    }
}