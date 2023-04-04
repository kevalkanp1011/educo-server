package live.kevalkanpariya.data.repository.comment

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Comment
import live.kevalkanpariya.data.models.Course
import live.kevalkanpariya.data.models.Like
import live.kevalkanpariya.data.responses.CommentResponse
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.inc

class CommentRepositoryImpl(
    db: CoroutineDatabase
): CommentRepository {

    private val courses = db.getCollection<Course>()
    private val comments = db.getCollection<Comment>()
    private val likes = db.getCollection<Like>()

    override suspend fun createComment(comment: Comment, app: Application): Boolean {
        comments.insertOne(comment)
        return courses.updateOne(
            filter = Course::courseId eq comment.courseId,
            update = inc(Course::commentCount,1)
        ).wasAcknowledged()
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        val deleteCount = comments.deleteOne(Comment::id eq commentId).deletedCount
        return deleteCount > 0
    }

    override suspend fun deleteCommentsFromCourse(courseId: String): Boolean {
        return comments.deleteMany(
            Comment::courseId eq courseId
        ).wasAcknowledged()
    }

    override suspend fun getCommentsForCourse(courseId: String, ownUserId: String): List<CommentResponse>? {
        return comments.find(Comment::courseId eq courseId).toList().map { comment ->
            val isLiked = likes.findOne(
                and(
                    Like::userId eq ownUserId,
                    Like::parentId eq comment.id
                )
            ) != null
            CommentResponse(
                id = comment.id,
                username = comment.username,
                profilePictureUrl = comment.profileImageUrl,
                timestamp = comment.timestamp,
                comment = comment.comment,
                isLiked = isLiked,
                likeCount = comment.likeCount
            )
        }
    }

    override suspend fun getComment(commentId: String): Comment? {
        return comments.findOneById(commentId)
    }

}