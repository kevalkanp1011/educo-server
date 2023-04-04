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
import live.kevalkanpariya.data.repository.aws.FileStorage
import live.kevalkanpariya.data.requests.UpdateProfileRequest
import live.kevalkanpariya.data.responses.BasicApiResponse
import live.kevalkanpariya.data.responses.UserResponseItem
import live.kevalkanpariya.service.UserService
import live.kevalkanpariya.util.ApiResponseMessages
import live.kevalkanpariya.util.Constants.PROFILE_PICTURE_PATH
import live.kevalkanpariya.util.QueryParams
import live.kevalkanpariya.util.toFile
import java.io.File

fun Route.searchUser(userService: UserService) {
    authenticate("auth-eduCo") {
        get("/api/user/search") {
            val query = call.parameters[QueryParams.PARAM_QUERY]
            if (query.isNullOrBlank()) {
                call.respond(
                    HttpStatusCode.OK,
                    listOf<UserResponseItem>()
                )
                return@get
            }

            val searchResults = userService.searchForUsers(query, call.userId)
            call.respond(
                HttpStatusCode.OK,
                searchResults
            )



        }
    }
}

fun Route.getUserInfos(userService: UserService) {
    authenticate("auth-eduCo") {
        get("/api/get/users") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val userInfos = userService.getUserInfos(call.userId, page, pageSize)
            userInfos?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = BasicApiResponse(
                        successful = true,
                        data = it
                    )
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    message = "No Users is available"
                )
            }
        }
    }
}

fun Route.getUserHeader(userService: UserService) {
    authenticate("auth-eduCo") {
        get("/api/user/profile_header") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID]
            if (userId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val response = userService.getUserHeaderProfile(userId)
            if (response == null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
                return@get
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = response
                )
            )
        }
    }
}
fun Route.getUserProfile(userService: UserService) {
    authenticate("auth-eduCo") {
        get("/api/user/profile") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val profileResponse = userService.getUserProfile(userId, call.userId)

            profileResponse?.let {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        data = profileResponse
                    )
                )
            }?: kotlin.run {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
                return@get
            }

        }
    }
}

fun Route.updateUserProfile(app: Application, userService: UserService, fileStorage: FileStorage) {
    authenticate("auth-eduCo") {
        put("/api/user/update") {
            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateProfileRequest? = null
            var profilePictureUrl: String? = null
            var bannerImageUrl: String? = null


            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "update_profile_data") {
                            updateProfileRequest = Json.decodeFromString<UpdateProfileRequest>(partData.value)
                        }
                    }
                    is PartData.FileItem -> {

                        if (partData.name == "profile_picture") {
                            profilePictureUrl = fileStorage.saveToAWSBucket(
                                file = partData.toFile(),
                                bucketName = "prof-pictures",
                            )
                            app.log.info("profile Picture Successfully Saved $profilePictureUrl")

                        } else if (partData.name == "banner_image") {
                            bannerImageUrl = fileStorage.saveToAWSBucket(
                                file = partData.toFile(),
                                bucketName = "baner-image",
                            )
                            app.log.info("Banner Image Successfully Saved $bannerImageUrl")
                        }
                    }
                    is PartData.BinaryItem -> Unit
                    else -> Unit
                }
                partData.dispose
            }

            updateProfileRequest?.let { request ->
                val updateAcknowledged = userService.updateUser(
                    userId = call.userId,
                    profileImageUrl = if (profilePictureUrl == null) {
                        null
                    } else {
                        profilePictureUrl
                    },
                    bannerUrl = if(bannerImageUrl == null) {
                        null
                    } else {
                        bannerImageUrl
                    },
                    updateProfileRequest = request,
                    app = app
                )
                if (updateAcknowledged) {
                    app.log.info("profile Successfully Updated : $updateAcknowledged")
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true,
                        )
                    )
                } else {
                    File("${PROFILE_PICTURE_PATH}/$profilePictureUrl").delete()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                val updateAcknowledged = userService.updateUser(
                    userId = call.userId,
                    profileImageUrl = if (profilePictureUrl == null) {
                        null
                    } else {
                        profilePictureUrl
                    },
                    bannerUrl = if(bannerImageUrl == null) {
                        null
                    } else {
                        bannerImageUrl
                    },
                    updateProfileRequest = null,
                    app = app
                )
                if (updateAcknowledged) {
                    app.log.info("profile Successfully Updated : $updateAcknowledged")
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true,
                        )
                    )
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}

fun Route.deleteUser(userService: UserService) {
    authenticate("auth-eduCo") {
        delete("/api/user/delete") {
            val isDeleted = userService.deleteUser(call.userId)
            if (!isDeleted) {
                call.respond(
                    HttpStatusCode.Conflict
                )
                return@delete
            }
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse<Unit>(
                    successful = true,
                    message = "User is Deleted Successfully"
                )
            )
        }
    }
}
