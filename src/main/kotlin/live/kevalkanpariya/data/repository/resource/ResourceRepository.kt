package live.kevalkanpariya.data.repository.resource

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Resource
import live.kevalkanpariya.data.requests.ResourceRequest

interface ResourceRepository {

    suspend fun getResourcesForCourse(courseId: String, app: Application): List<Resource>?
    suspend fun createResource(resource: ResourceRequest, app: Application): Boolean
    suspend fun updateResource(resourceId: String, resourceUrl: String?, app: Application): Boolean
    suspend fun deleteResource(resourceId: String,): Boolean
    suspend fun deleteAllResources(courseId: String): Boolean
}