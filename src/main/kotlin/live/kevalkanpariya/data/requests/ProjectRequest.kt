package live.kevalkanpariya.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class ProjectRequest(
    val projectName: String,
    val desc: String,
    val imageUrls: List<String>
)
