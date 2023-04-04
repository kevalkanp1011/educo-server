package live.kevalkanpariya.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val id: String,
    val username: String,
    val profilePictureUrl: String,
    val timestamp: Long,
    val comment: String,
    val isLiked: Boolean,
    val likeCount: Int
)
