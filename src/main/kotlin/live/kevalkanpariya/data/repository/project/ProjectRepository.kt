package live.kevalkanpariya.data.repository.project

import live.kevalkanpariya.data.models.Project
import live.kevalkanpariya.data.requests.ProjectRequest

interface ProjectRepository {

    suspend fun getProject(projectId: String): Project?
    suspend fun getAllProject(courseId: String): List<Project>?
    suspend fun createProject(project: Project): Boolean
    suspend fun updateProject(request: ProjectRequest, projectId: String): Boolean
    suspend fun deleteProject(projectId: String): Boolean
    suspend fun deleteAllProject(courseId: String): Boolean
}