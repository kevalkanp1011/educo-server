package live.kevalkanpariya.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val userId: String,
    val token: String
)