package live.kevalkanpariya.service

import live.kevalkanpariya.data.models.Project
import live.kevalkanpariya.data.repository.project.ProjectRepository
import live.kevalkanpariya.data.requests.ProjectRequest

class ProjectService(
    private val projectRepository: ProjectRepository
) {

    suspend fun getProject(projectId: String): Project? {
        return projectRepository.getProject(projectId)
    }
    suspend fun getAllProject(courseId: String): List<Project>? {
        return projectRepository.getAllProject(courseId)
    }

    suspend fun createProject(courseId: String, request: ProjectRequest): Boolean {
        return projectRepository.createProject(
            Project(
                courseId = courseId,
                projectName = request.projectName,
                desc = request.desc,
                imageUrls = request.imageUrls
            )
        )
    }

    suspend fun updateProject(request: ProjectRequest, projectId: String): Boolean {
        return projectRepository.updateProject(request, projectId)
    }
    suspend fun deleteProject(projectId: String): Boolean {
        return projectRepository.deleteProject(projectId)
    }
    suspend fun deleteAllProject(courseId: String): Boolean {
        return projectRepository.deleteAllProject(courseId)
    }

}