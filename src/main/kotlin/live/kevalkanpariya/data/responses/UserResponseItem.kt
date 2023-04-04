package live.kevalkanpariya.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseItem(
    val userId: String,
    val name: String,
    val username: String,
    val profilePictureUrl: String,
    val bio: String,
    val isFollowing: Boolean
)
