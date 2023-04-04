package live.kevalkanpariya.data.models

import kotlinx.serialization.Serializable

@Serializable
enum class UserType{
    STUDENT,
    TEACHER,
    ADMIN
}