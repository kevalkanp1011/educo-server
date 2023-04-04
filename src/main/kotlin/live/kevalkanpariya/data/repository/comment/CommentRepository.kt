package live.kevalkanpariya.data.repository.comment

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Comment
import live.kevalkanpariya.data.responses.CommentResponse

interface CommentRepository {

    suspend fun createComment(comment: Comment, app: Application): Boolean

    suspend fun deleteComment(commentId: String): Boolean

    suspend fun deleteCommentsFromCourse(courseId: String): Boolean

    suspend fun getCommentsForCourse(courseId: String, ownUserId: String): List<CommentResponse>?

    suspend fun getComment(commentId: String): Comment?
}