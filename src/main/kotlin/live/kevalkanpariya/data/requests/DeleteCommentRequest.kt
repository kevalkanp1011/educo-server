package live.kevalkanpariya.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteCommentRequest(
    val commentId: String,
)