package live.kevalkanpariya.domain.model.teacher

import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val name: String,
    val bio: String,
    val instaLink: String
)
