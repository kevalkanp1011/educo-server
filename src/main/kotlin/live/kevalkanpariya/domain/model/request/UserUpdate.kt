package live.kevalkanpariya.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdate(
    val firstName: String,
    val lastName: String
)
