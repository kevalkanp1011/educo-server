package live.kevalkanpariya.routes.aws_s3

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import live.kevalkanpariya.domain.model.Endpoint
import live.kevalkanpariya.domain.model.api_response.ImageApiResponse
import live.kevalkanpariya.storage.exposed.LocalSource
import live.kevalkanpariya.util.toFile
import java.io.File



 fun Route.addModuleImage(localSource: LocalSource) {

    post(Endpoint.ImageUpload.path) {
        val id = call.parameters["id"]!!.toLong()

        var image: File? = null

        call.receiveMultipart().forEachPart {
            when (it) {
                is PartData.FileItem -> image = it.toFile()
                else -> Unit
            }
            it.dispose()
        }
        val imageUrl = localSource.saveImage(
            id,
            image ?: throw BadRequestException("image part is missing")
        )
        image?.delete()
        call.respond(
            message = ImageApiResponse(success = true, moduleImage = imageUrl),
            status = HttpStatusCode.OK
        )
    }
}