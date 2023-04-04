package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val name: String,
    val profileImageUrl: String
)