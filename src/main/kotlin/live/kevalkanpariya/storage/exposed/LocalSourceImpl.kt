package live.kevalkanpariya.storage.exposed

import live.kevalkanpariya.domain.model.course.Course
import live.kevalkanpariya.storage.aws.FileStorage
import org.litote.kmongo.coroutine.CoroutineDatabase
import java.io.File

class LocalSourceImpl(
    private val fileStorage: FileStorage,
    database: CoroutineDatabase
): LocalSource {

    private val courses = database.getCollection<Course>("course")


    override suspend fun saveImage(recipeId: Long, image: File): String {


        return fileStorage.save(image)
    }


    override suspend fun uploadCourse(courseThumbnail: File) {
        val courseThumbnail = fileStorage.save(courseThumbnail)
        val course = courses.findOne()
        if (course != null) {
            course.imageUrl = courseThumbnail
        }
    }

}