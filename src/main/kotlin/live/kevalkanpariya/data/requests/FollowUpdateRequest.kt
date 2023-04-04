package live.kevalkanpariya.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class FollowUpdateRequest(
    val followedUserId: String
)
