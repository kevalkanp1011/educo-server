package live.kevalkanpariya.data.requests

import kotlinx.serialization.Serializable
import live.kevalkanpariya.util.DurationSerializer
import kotlin.time.Duration

@Serializable
data class CourseRequest(
    val courseTitle: String,
    val description: String,
    val tag: String? = null,
    @Serializable(with = DurationSerializer::class)
    val duration: Duration
)

