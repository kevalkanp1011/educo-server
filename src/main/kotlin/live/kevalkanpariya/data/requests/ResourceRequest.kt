package live.kevalkanpariya.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class ResourceRequest(
    val courseId: String,
    val resourceUrl: String
)
