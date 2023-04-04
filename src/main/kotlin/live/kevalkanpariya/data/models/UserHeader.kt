package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserHeader(
    val name: String?,
    val profilePictureUrl: String
)
