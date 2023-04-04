package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable
import live.kevalkanpariya.util.DateSerializer
import java.util.Date

@Serializable
data class Enrollment(
    val userId: String,
    val courseId: String,
    @Serializable(with = DateSerializer::class)
    val dateEnrolled: Date
)
