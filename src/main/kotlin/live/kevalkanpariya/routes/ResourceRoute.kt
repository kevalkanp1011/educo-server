package live.kevalkanpariya.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import live.kevalkanpariya.data.repository.aws.FileStorage
import live.kevalkanpariya.data.responses.BasicApiResponse
import live.kevalkanpariya.service.ResourceService
import live.kevalkanpariya.util.QueryParams
import live.kevalkanpariya.util.toFile
import java.net.URL
import java.net.URLConnection

fun Route.getResourcesForCourse(resourceService: ResourceService, app: Application) {
    authenticate("auth-eduCo") {
        get("/api/user/course/resources") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }


            val resources = resourceService.getResourcesForCourse(courseId, app)

            resources?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = resources
                    )
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.NotFound,
                    message = "resources are not available"
                )
                return@get
            }
        }
    }
}
fun Route.createResource(fileStorage: FileStorage, resourceService: ResourceService, app: Application) {
    authenticate("auth-eduCo"){
        post("/api/user/course/create_resource") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val multipart = call.receiveMultipart()
            var resource: String? = null

            multipart.forEachPart { partData ->
                when(partData) {
                    is PartData.FileItem -> {
                        resource = fileStorage.saveToAWSBucket(
                            partData.toFile(),
                            "prof-pictures"
                        )
                    }

                    else -> Unit
                }
                partData.dispose
            }

            resource?.let {resource ->
            val isCreated = resourceService.createResource(courseId, resource, app)
                if (isCreated)
                    call.respond(
                        HttpStatusCode.OK, message = BasicApiResponse<Unit>(successful = true)
                    )

            }?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, message = BasicApiResponse<Unit>(successful = false))
                return@post
            }
        }
    }
}



fun Route.updateResource(fileStorage: FileStorage, resourceService: ResourceService, app: Application) {
    authenticate("auth-eduCo"){
        post("/api/user/course/update_resource") {
            val resourceId = call.parameters[QueryParams.PARAM_RESOURCE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val multipart = call.receiveMultipart()
            var resourceUrl: String? = null

            multipart.forEachPart { partData ->
                when(partData) {
                    is PartData.FileItem -> {
                        resourceUrl = fileStorage.saveToAWSBucket(
                            partData.toFile(),
                            "prof-pictures"
                        )
                    }

                    else -> Unit
                }
                partData.dispose
            }

            resourceUrl?.let { resourceUrl ->
                val isUpdated = resourceService.updateResource(resourceId, resourceUrl, app)
                if (isUpdated)
                    call.respond(
                        HttpStatusCode.OK, message = BasicApiResponse<Unit>(successful = true)
                    )
                else
                    call.respond(HttpStatusCode.InternalServerError)

            }?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, message = BasicApiResponse<Unit>(successful = false))
                return@post
            }
        }
    }
}

fun Route.deleteResource(resourceService: ResourceService) {
    authenticate("auth-eduCo") {
        delete("/api/user/course/delete_resource") {
            val resourceId = call.parameters[QueryParams.PARAM_RESOURCE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val isDeleted = resourceService.deleteResource(resourceId)
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

fun Route.deleteAllResources(resourceService: ResourceService) {
    authenticate("auth-eduCo") {
        delete("/api/user/course/delete_all_resources") {
            val courseId = call.parameters[QueryParams.PARAM_COURSE_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val isDeleted = resourceService.deleteAllResource(courseId)
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