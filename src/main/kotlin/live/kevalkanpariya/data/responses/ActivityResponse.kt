package live.kevalkanpariya.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponse(
    val timestamp: Long,
    val userId: String,
    val parentId: String,
    val type: Int,
    val username: String,
    val id: String
)
