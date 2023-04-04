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
import live.kevalkanpariya.data.requests.CategoryRequest
import live.kevalkanpariya.service.CategoryService
import live.kevalkanpariya.util.QueryParams
import live.kevalkanpariya.util.toFile

fun Route.getPopularCategories(categoryService: CategoryService) {
    authenticate("auth-eduCo") {
        get("/api/course/popular_categories") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toInt()?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val popularCategories = categoryService.getPopularCategories(page, pageSize)
            popularCategories?.let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = it
                )
            }?: call.respond(
                HttpStatusCode.OK,
                message = "No categories is available"
            )
        }
    }
}

fun Route.createCategory(categoryService: CategoryService, fileStorage: FileStorage, app: Application) {
    authenticate("auth-eduCo") {
        post("/api/course/category/create") {
            val multipart = call.receiveMultipart()
            var categoryName: String? = null
            var categoryImageUrl: String? = null

            multipart.forEachPart {partData ->
                when(partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "categoryName") {
                            categoryName = Json.decodeFromString(partData.value)
                        }
                    }
                    is PartData.FileItem -> {

                        categoryImageUrl = fileStorage.saveToAWSBucket(
                            file = partData.toFile(),
                            bucketName = "prof-pictures",
                        )
                        app.log.info("Category Image Successfully Saved $categoryImageUrl")

                    }

                    else -> Unit
                }
                partData.dispose

            }

            categoryName?.let {
                val isCreated = categoryService.createCategory(
                    CategoryRequest(
                        categoryName = it,
                        categoryImageUrl = if (categoryImageUrl == null){
                            null
                        } else {
                            categoryImageUrl
                        }
                    )
                )

                if (!isCreated) {
                    call.respond(
                        HttpStatusCode.InternalServerError
                    )
                    return@post
                }
                call.respond(HttpStatusCode.OK)
            }?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }



        }
    }
}

fun Route.updateCategory(categoryService: CategoryService, fileStorage: FileStorage, app: Application) {
    authenticate("auth-eduCo") {
        post("/api/course/category/update") {
            val categoryId = call.parameters[QueryParams.PARAM_CATEGORY_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val multipart = call.receiveMultipart()
            var categoryName: String? = null
            var categoryImageUrl: String? = null

            multipart.forEachPart {partData ->
                when(partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "categoryName") {
                            categoryName = Json.decodeFromString(partData.value)
                        }
                    }
                    is PartData.FileItem -> {

                        categoryImageUrl = fileStorage.saveToAWSBucket(
                            file = partData.toFile(),
                            bucketName = "prof-pictures",
                        )
                        app.log.info("Category Image Successfully Saved $categoryImageUrl")

                    }

                    else -> Unit
                }
                partData.dispose

            }

            val isCreated = categoryService.updateCategory(
                categoryId,
                CategoryRequest(
                    categoryName = categoryName?: "",
                    categoryImageUrl = if (categoryImageUrl == null){
                        null
                    } else {
                        categoryImageUrl
                    }
                )
            )

            if (!isCreated) {
                call.respond(
                    HttpStatusCode.InternalServerError
                )
                return@post
            }
            call.respond(HttpStatusCode.OK)



        }
    }
}

fun Route.deleteCategory(categoryService: CategoryService) {

    authenticate("auth-eduCo") {
        delete("/api/course/category/delete") {
            val categoryId = call.parameters[QueryParams.PARAM_CATEGORY_ID]?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val isDeleted = categoryService.deleteCategory(
                categoryId,
            )

            if (!isDeleted) {
                call.respond(
                    HttpStatusCode.InternalServerError
                )
                return@delete
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}