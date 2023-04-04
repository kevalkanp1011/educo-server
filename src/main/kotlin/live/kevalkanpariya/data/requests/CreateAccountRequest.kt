package live.kevalkanpariya.data.requests

import kotlinx.serialization.Serializable
import live.kevalkanpariya.data.models.UserType

@Serializable
data class CreateAccountRequest(
    val email: String,
    val username: String,
    val password: String,
    val accountType: UserType? = null
)
