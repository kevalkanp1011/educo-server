package live.kevalkanpariya.data.repository.resource

import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import live.kevalkanpariya.data.models.Resource
import live.kevalkanpariya.data.requests.ResourceRequest
import org.litote.kmongo.combine
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import java.net.URL
import java.net.URLConnection
import kotlin.math.roundToInt

class ResourceRepositoryImpl(
    db: CoroutineDatabase
): ResourceRepository {

    private val resources = db.getCollection<Resource>()
    override suspend fun getResourcesForCourse(courseId: String, app: Application): List<Resource>? {
        app.log.info(courseId)
        val resources = resources.find(Resource::courseId eq courseId).toList()
        app.log.info(resources.toString())
        return resources
    }

    override suspend fun createResource(resource: ResourceRequest, app: Application): Boolean {
        return resources.insertOne(
            Resource(
                courseId = resource.courseId,
                resourceName = urlToName(resource.resourceUrl, app),
                resourceUrl = resource.resourceUrl,
                resourceSize = measureFileSize(resource.resourceUrl, app),
                fileType = urlToFileType(resource.resourceUrl)
            )
        ).wasAcknowledged()
    }

    override suspend fun updateResource(resourceId: String, resourceUrl: String?, app: Application): Boolean {
        return resources.updateOne(
            filter = Resource::resourceId eq resourceId,
            update =  combine(
                setValue(Resource::resourceUrl, resourceUrl),
                setValue(Resource::resourceName, resourceUrl?.let { urlToName(it, app) }),
                setValue(Resource::resourceSize, resourceUrl?.let { measureFileSize(it, app) }),
                setValue(Resource::fileType, resourceUrl?.let { urlToFileType(it) }),

            ) ,
        ).wasAcknowledged()
    }

    override suspend fun deleteResource(resourceId: String): Boolean {
        return resources.deleteOne(
            filter = Resource::resourceId eq resourceId
        ).wasAcknowledged()
    }

    override suspend fun deleteAllResources(courseId: String): Boolean {
        return resources.deleteMany(Resource::courseId eq courseId).wasAcknowledged()
    }

}

private fun urlToName(url: String, app: Application): String {
    val fileName: String = url.substring(url.lastIndexOf('/') + 1, url.length)
    app.log.info(fileName)
    return fileName.substring(0, fileName.lastIndexOf('.'))
}

private fun urlToFileType(url: String): String {
    return url.substring(url.lastIndexOf('.') + 1, url.length)
}

private suspend fun measureFileSize(fileUrl: String, app: Application): Double {
    val url = URL(fileUrl)
    val urlConnection: URLConnection = withContext(Dispatchers.IO) {
        url.openConnection()
    }
    withContext(Dispatchers.IO) {
        urlConnection.connect()
    }
    val file_size = urlConnection.contentLength.toDouble()
    val sizeInMb = (file_size/1048576)
    val roundoff = (sizeInMb * 100.0).roundToInt() / 100.0
    app.log.info(roundoff.toString())
    return roundoff
}