package live.kevalkanpariya.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackRequest(
    val feedback: String,
    val timestamp: Long
)
