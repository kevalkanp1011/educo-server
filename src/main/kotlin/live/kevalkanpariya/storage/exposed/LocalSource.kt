package live.kevalkanpariya.storage.exposed

import java.io.File

interface LocalSource {
    suspend fun saveImage(recipeId: Long, image: File): String
    suspend fun uploadCourse( courseThumbnail: File)
}