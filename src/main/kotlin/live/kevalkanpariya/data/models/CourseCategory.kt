package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CourseCategory(
    val imageUrl: String,
    val catName: String
)