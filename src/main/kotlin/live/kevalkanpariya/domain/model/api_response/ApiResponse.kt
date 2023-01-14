package live.kevalkanpariya.domain.model.api_response

import live.kevalkanpariya.domain.model.auth.User

@kotlinx.serialization.Serializable
data class ApiResponse(
    val success: Boolean,
    val user: User? = null,
    val message: String? = null
)