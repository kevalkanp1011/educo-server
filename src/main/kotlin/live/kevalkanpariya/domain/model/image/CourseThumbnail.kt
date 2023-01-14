package live.kevalkanpariya.domain.model.image

import kotlinx.serialization.Serializable
import live.kevalkanpariya.domain.model.request.CourseId

@Serializable
data class CourseThumbnail(
    val courseId: CourseId,
    val thumbnailImage: String
)