package live.kevalkanpariya.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import live.kevalkanpariya.data.models.Lesson
import live.kevalkanpariya.data.repository.aws.FileStorage
import live.kevalkanpariya.data.requests.LessonRequest
import live.kevalkanpariya.data.responses.BasicApiResponse
import live.kevalkanpariya.service.CourseService
import live.kevalkanpariya.service.LessonService
import live.kevalkanpariya.util.QueryParams
import live.kevalkanpariya.util.QueryParams.PARAM_COURSE_ID
import live.kevalkanpariya.util.QueryParams.PARAM_LESSON_ID
import live.kevalkanpariya.util.toFile

fun Route.getLesson(lessonService: LessonService) {
    authenticate("auth-eduCo") {
        get("/api/user/course/lesson") {
            val lessonId = call.parameters[PARAM_LESSON_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val lesson = lessonService.getLesson(lessonId)

            if (lesson == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(successful = true, data = lesson)
            )
        }
    }
}

fun Route.getLessonsForCourse(lessonService: LessonService) {
    authenticate("auth-eduCo") {
        get("/api/user/course/lessons") {
            val courseId = call.parameters[PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val lessons = lessonService.getLessonsForCourse(courseId)

            if (lessons == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(successful = true, data = lessons)
            )
        }
    }
}

fun Route.createLesson(app: Application, fileStorage: FileStorage, lessonService: LessonService) {
    authenticate("auth-eduCo") {
        post("/api/user/course/create_lesson") {
            val courseId = call.parameters[PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val multipart = call.receiveMultipart()
            var createLessonRequest: LessonRequest? = null
            var lessonThumbnailUrl: String? = null
            var videoUrl: String? = null

            multipart.forEachPart {partData ->
                when(partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "lesson_data") {
                            createLessonRequest = Json.decodeFromString<LessonRequest>(partData.value)
                        }
                    }
                    is PartData.FileItem -> {
                        if (partData.name == "lesson_thumbnail") {
                            lessonThumbnailUrl = fileStorage.saveToAWSBucket(
                                file = partData.toFile(),
                                bucketName = "prof-pictures",
                            )
                            app.log.info("profile Picture Successfully Saved $lessonThumbnailUrl")
                        }
                        if (partData.name == "lesson_videoUrl") {
                            videoUrl = fileStorage.saveToAWSBucket(
                                file = partData.toFile(),
                                bucketName = "prof-pictures",
                            )
                            app.log.info("Video Successfully Saved $videoUrl")
                        }

                    }
                    else -> Unit
                }
                partData.dispose
            }

            createLessonRequest?.let {
                val updateAcknowledged = lessonService.createLesson(
                    courseId = courseId,
                    lessonThumbnailUrl = if (lessonThumbnailUrl == null) {
                        null
                    } else {
                        lessonThumbnailUrl
                    },
                    videoUrl = if (videoUrl == null) {
                        null
                    } else {
                        videoUrl
                    },
                    createLessonRequest = it,
                    app
                )
                if (updateAcknowledged) {
                    app.log.info("Lesson Successfully Created : $updateAcknowledged")
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true,
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }


        }
    }
}

fun Route.updateLessonForCourse(lessonService: LessonService, fileStorage: FileStorage, app: Application) {
    authenticate("auth-eduCo") {
        post("/api/user/course/update_lesson") {
            val lessonId = call.parameters[PARAM_LESSON_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val multipart = call.receiveMultipart()
            var updateLessonRequest: LessonRequest? = null
            var lessonThumbnailUrl: String? = null
            var videoUrl: String? = null

            multipart.forEachPart {partData ->
                when(partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "lesson_data") {
                            updateLessonRequest = Json.decodeFromString<LessonRequest>(partData.value)
                        }
                    }
                    is PartData.FileItem -> {
                        if (partData.name == "lesson_thumbnail") {
                            lessonThumbnailUrl = fileStorage.saveToAWSBucket(
                                file = partData.toFile(),
                                bucketName = "prof-pictures",
                            )
                            app.log.info("Thumbnail Successfully Saved $lessonThumbnailUrl")
                        }
                        if (partData.name == "lesson_videoUrl") {
                            videoUrl = fileStorage.saveToAWSBucket(
                                file = partData.toFile(),
                                bucketName = "prof-pictures",
                            )
                            app.log.info("Video Successfully Saved $videoUrl")
                        }

                    }
                    else -> Unit
                }
                partData.dispose
            }

            updateLessonRequest?.let {
                val updateAcknowledged = lessonService.updateLessonForCourse(
                    lessonId = lessonId,
                    lessonThumbnailUrl = if (lessonThumbnailUrl == null) {
                        null
                    } else {
                        lessonThumbnailUrl
                    },
                    videoUrl = if (videoUrl == null) {
                        null
                    } else {
                        videoUrl
                    },
                    lesson = it,
                    app = app
                )
                if (updateAcknowledged) {
                    app.log.info("Lesson Successfully Created : $updateAcknowledged")
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true,
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

        }
    }
}

fun Route.deleteLesson(lessonService: LessonService) {
    authenticate("auth-eduCo") {
        delete("/api/user/course/delete_lesson") {
            val lessonId = call.parameters[PARAM_LESSON_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val isDeleted = lessonService.deleteLesson(lessonId)
            if (!isDeleted) {
                call.respond(HttpStatusCode.Conflict)
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(successful = isDeleted)
            )

        }
    }
}

fun Route.deleteAllLessons(lessonService: LessonService) {
    authenticate("auth-eduCo") {
        delete("/api/user/course/delete_all_lessons") {
            val courseId = call.parameters[PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val isDeleted = lessonService.deleteAllLesson(courseId)
            if (isDeleted)
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(successful = true)
                )
            else
                call.respond(
                    HttpStatusCode.InternalServerError,
                    BasicApiResponse<Unit>(successful = false)
                )
            return@delete
        }
    }
}
