package live.kevalkanpariya.data.repository.project

import live.kevalkanpariya.data.models.Project
import live.kevalkanpariya.data.requests.ProjectRequest
import org.litote.kmongo.combine
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class ProjectRepositoryImpl(
    db: CoroutineDatabase
): ProjectRepository {

    private val projects = db.getCollection<Project>()
    override suspend fun getProject(projectId: String): Project? {
        return projects.findOne(Project::projectId eq  projectId)
    }

    override suspend fun getAllProject(courseId: String): List<Project>? {
        return projects.find(Project::courseId eq courseId).toList()
    }

    override suspend fun createProject(project: Project): Boolean {
        return projects.insertOne(project).wasAcknowledged()
    }

    override suspend fun updateProject(request: ProjectRequest, projectId: String): Boolean {
        return projects.updateOne(
            filter = Project::projectId eq projectId,
            update = combine(
                setValue(Project::projectName, request.projectName),
                setValue(Project::desc, request.desc),
                setValue(Project::imageUrls, request.imageUrls)
            )
        ).wasAcknowledged()
    }

    override suspend fun deleteProject(projectId: String): Boolean {
        return projects.deleteOne(Project::projectId eq projectId).wasAcknowledged()
    }

    override suspend fun deleteAllProject(courseId: String): Boolean {
        return projects.deleteMany(Project::courseId eq courseId).wasAcknowledged()
    }
}