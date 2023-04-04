package live.kevalkanpariya.service

import io.ktor.server.application.*
import live.kevalkanpariya.data.models.Comment
import live.kevalkanpariya.data.repository.comment.CommentRepository
import live.kevalkanpariya.data.repository.user.UserRepository
import live.kevalkanpariya.data.requests.CreateCommentRequest
import live.kevalkanpariya.data.responses.CommentResponse
import live.kevalkanpariya.util.Constants

class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {

    suspend fun createComment(createCommentRequest: CreateCommentRequest, userId: String, app: Application): ValidationEvent {
        createCommentRequest.apply {
            if(comment.isBlank() || courseId.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
            if(comment.length > Constants.MAX_COMMENT_LENGTH) {
                return ValidationEvent.ErrorCommentTooLong
            }
        }
        val user = userRepository.getUserById(userId) ?: return ValidationEvent.UserNotFound
        val isCreatedComment = commentRepository.createComment(
            Comment(
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                likeCount = 0,
                comment = createCommentRequest.comment,
                userId = userId,
                courseId = createCommentRequest.courseId,
                timestamp = System.currentTimeMillis()
            ),
            app
        )
        if (!isCreatedComment)
            return ValidationEvent.ServerError

        return ValidationEvent.Success
    }

    suspend fun deleteCommentsForCourse(postId: String) {
        commentRepository.deleteCommentsFromCourse(postId)
    }

    suspend fun deleteComment(commentId: String): Boolean {
        return commentRepository.deleteComment(commentId)
    }

    suspend fun getCommentsForCourse(courseId: String, ownUserId: String): List<CommentResponse>? {
        return commentRepository.getCommentsForCourse(courseId, ownUserId)
    }

    suspend fun getCommentById(commentId: String): Comment? {
        return commentRepository.getComment(commentId)
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object UserNotFound: ValidationEvent()
        object Success : ValidationEvent()

        object ServerError: ValidationEvent()
    }
}