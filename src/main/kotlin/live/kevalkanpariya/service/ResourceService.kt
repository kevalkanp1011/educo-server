package live.kevalkanpariya.service

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Resource
import live.kevalkanpariya.data.repository.resource.ResourceRepository
import live.kevalkanpariya.data.requests.ResourceRequest

class ResourceService(
    private val resourceRepository: ResourceRepository
) {

    suspend fun getResourcesForCourse(courseId: String, app: Application): List<Resource>? {
        return resourceRepository.getResourcesForCourse(courseId, app)
    }

    suspend fun createResource(
        courseId: String,
        resourceUrl: String,
        app: Application
    ): Boolean {
        return resourceRepository.createResource(
            ResourceRequest(courseId = courseId, resourceUrl = resourceUrl),
            app = app
        )
    }

    suspend fun updateResource(
        resourceId: String,
        resourceUrl: String?,
        app: Application
    ): Boolean {
        return resourceRepository.updateResource(
            resourceId,
            resourceUrl,
            app
        )
    }
    suspend fun deleteResource(resourceId: String): Boolean {
        return resourceRepository.deleteResource(resourceId)
    }

    suspend fun deleteAllResource(courseId: String): Boolean {
        return resourceRepository.deleteAllResources(courseId)
    }
}