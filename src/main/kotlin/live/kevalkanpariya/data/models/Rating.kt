package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable
import live.kevalkanpariya.util.DateSerializer
import java.util.*

@Serializable
data class Rating(
    val userId: String,
    val courseId: String,
    val ratingValue: Int,
    @Serializable(with = DateSerializer::class)
    val dateRating: Date
)

@Serializable
data class AvgRating(val avg_rating: Double)
