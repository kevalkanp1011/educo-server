package live.kevalkanpariya.domain.model.db

data class Enrollment(
    val username: String,
    val CourseId: String,
    val isEnrolled: Boolean
)
