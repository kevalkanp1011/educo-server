package live.kevalkanpariya.domain.model.lesson

import kotlinx.serialization.Serializable
import live.kevalkanpariya.domain.model.image.lesson.ModuleImage

@Serializable
data class Lesson(
    val name: String,
    val thumbnail: String,
    val desc: String,
)
