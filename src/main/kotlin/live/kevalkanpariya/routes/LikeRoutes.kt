package live.kevalkanpariya.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.data.requests.LikeUpdateRequest
import live.kevalkanpariya.data.responses.BasicApiResponse
import live.kevalkanpariya.data.util.ParentType
import live.kevalkanpariya.service.ActivityService
import live.kevalkanpariya.service.LikeService
import live.kevalkanpariya.util.ApiResponseMessages
import live.kevalkanpariya.util.QueryParams

fun Route.likeParent(
    likeService: LikeService,
    activityService: ActivityService,
    app: Application
) {
    authenticate("auth-eduCo") {
        post("/api/like") {
            val request = call.receiveOrNull<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userId = call.userId
            val likeSuccessful = likeService.likeParent(userId, request.parentId, request.parentType, app)
            app.log.info("$likeSuccessful")
            if(likeSuccessful) {
                activityService.addLikeActivity(
                    byUserId = userId,
                    parentType = ParentType.fromType(request.parentType),
                    parentId = request.parentId
                )
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.unlikeParent(
    likeService: LikeService,

) {
    authenticate("auth-eduCo") {
        delete("/api/unlike") {
            val parentId = call.parameters[QueryParams.PARAM_PARENT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val parentType = call.parameters[QueryParams.PARAM_PARENT_TYPE]?.toIntOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val unlikeSuccessful = likeService.unlikeParent(call.userId, parentId, parentType)
            if(unlikeSuccessful) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.getLikesForParent(likeService: LikeService) {
    authenticate("auth-eduCo") {
        get("/api/like/parent") {
            val parentId = call.parameters[QueryParams.PARAM_PARENT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val usersWhoLikedParent = likeService.getUsersWhoLikedParent(
                parentId = parentId,
                call.userId
            )
            call.respond(
                HttpStatusCode.OK,
                usersWhoLikedParent
            )
        }
    }
}